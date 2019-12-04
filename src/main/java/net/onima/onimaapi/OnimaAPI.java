package net.onima.onimaapi;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SplittableRandom;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.disguise.PlayerDisplayModifier;
import net.onima.onimaapi.event.mongo.DatabasePreUpdateEvent;
import net.onima.onimaapi.fakeblock.FakeBlocksFix;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.menu.FreezeMenu;
import net.onima.onimaapi.gui.menu.OnlineStaffMenu;
import net.onima.onimaapi.gui.menu.RankMenu;
import net.onima.onimaapi.limiter.EnchantLimiter;
import net.onima.onimaapi.limiter.PotionLimiter;
import net.onima.onimaapi.manager.ChatManager;
import net.onima.onimaapi.manager.CommandManager;
import net.onima.onimaapi.manager.ConfigManager;
import net.onima.onimaapi.manager.ListenerManager;
import net.onima.onimaapi.manager.TaskManager;
import net.onima.onimaapi.mod.SilentChest;
import net.onima.onimaapi.mongo.OnimaMongo;
import net.onima.onimaapi.mountain.utils.Mountain;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.utils.PlayerOption;
import net.onima.onimaapi.punishment.utils.Punishment;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.saver.FileSaver;
import net.onima.onimaapi.saver.Saver;
import net.onima.onimaapi.saver.inventory.PlayerSaver;
import net.onima.onimaapi.saver.mongo.NoSQLSaver;
import net.onima.onimaapi.signs.HCFSign;
import net.onima.onimaapi.tasks.CooldownEntryTask;
import net.onima.onimaapi.tasks.RankEntryTask;
import net.onima.onimaapi.utils.Config;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Options;
import net.onima.onimaapi.utils.Scheduler;
import net.onima.onimaapi.utils.Warp;
import net.onima.onimaapi.zone.type.Region;

/**
 * Main class of the plugin OnimaAPI.
 */
public class OnimaAPI extends JavaPlugin {
	
	public static final SplittableRandom RANDOM;
	public static final ZoneId TIME_ZONE;
	
	private static OnimaAPI instance;
	private static List<Scheduler> scheduled;
	private static List<Saver> savers, shutDownSavers;
	
	public static String UNKNOWN_COMMAND;
	
	private ListenerManager listenerManager;
	private EnchantLimiter enchantLimiter;
	private PotionLimiter potionLimiter;
	private ChatManager chatManager;
	private CommandManager commandManager;
	private boolean loaded;
	private PlayerDisplayModifier factory;
	
	static {
		RANDOM = new SplittableRandom();
		TIME_ZONE = ZoneId.of("Europe/Paris");
		
		scheduled = new ArrayList<>();
		savers = Collections.synchronizedList(new ArrayList<>());
		shutDownSavers = new ArrayList<>();
	}
	
	@Override
	public void onEnable() {
		if (!Bukkit.getOnlineMode()) {
			sendConsoleMessage("§cIMPOSSIBLE DE DEMARRER LE PLUGIN TANT QUE LE SERVEUR ACCEPTE LES CRACKS", ConfigurationService.ONIMAAPI_PREFIX);
			getPluginLoader().disablePlugin(this);
			return;
		}
		
		long started = System.currentTimeMillis();
		instance = this;
		
		sendConsoleMessage("====================§6[§3ACTIVATION§6]§r====================", ConfigurationService.ONIMAAPI_PREFIX);
		
		registerManager();
		
		Bukkit.getScheduler().runTaskLater(this, () -> loaded = true, 100L);
		sendConsoleMessage("====================§6[§3ACTIVE EN (" + (System.currentTimeMillis() - started) + "ms)§6]§r====================", ConfigurationService.ONIMAAPI_PREFIX);
	}
	
	public void registerManager() {
		OnimaMongo.connect();
		ConfigManager.loadConfigs();
		FakeBlocksFix.hook(this);
		SilentChest.hook(this);
		UUIDCache.load();
		
		Crate.deserialize();
		Mountain.deserialize();
		HCFSign.deserialize();
		Warp.deserialize();
		Region.deserialize();
		
		enchantLimiter = new EnchantLimiter();
		potionLimiter = new PotionLimiter();
		
		(listenerManager = new ListenerManager(this)).registerListener();
		(commandManager = new CommandManager(this)).registerCommands();
		new TaskManager(this).registerTasks();
		chatManager = new ChatManager();
		factory = new PlayerDisplayModifier(this);
		
		CooldownEntryTask.init(this);
		RankEntryTask.init(this);
		
		PacketMenu.getStaticMenus().add(new OnlineStaffMenu());
		PacketMenu.getStaticMenus().add(new FreezeMenu());
		PacketMenu.getStaticMenus().add(new RankMenu());
		
		Options.register(PlayerOption.ModOptions.ATTACK_PLAYER, false);
		Options.register(PlayerOption.ModOptions.BREAK_BLOCK, false);
		Options.register(PlayerOption.ModOptions.CHAT_MESSAGE, false);
		Options.register(PlayerOption.ModOptions.DROP_ITEM, false);
		Options.register(PlayerOption.ModOptions.ENTITY_BOOSTER, 3.0D);
		Options.register(PlayerOption.ModOptions.HIDE_PLAYER_TIME, 10);
		Options.register(PlayerOption.ModOptions.PICKUP_ITEM, false);
		Options.register(PlayerOption.ModOptions.PLACE_BLOCK, false);
		Options.register(PlayerOption.ModOptions.PLAYER_BOOSTER, 1.5D);
		Options.register(PlayerOption.ModOptions.TELEPORT_LAYER, 25);
		Options.register(PlayerOption.ModOptions.SILENT_CHEST, false);
		
		Options.register(PlayerOption.GlobalOptions.PRIVATE_MESSAGE, true);
		Options.register(PlayerOption.GlobalOptions.SOCIAL_SPY, false);
		Options.register(PlayerOption.GlobalOptions.SOUNDS, true);
		Options.register(PlayerOption.GlobalOptions.COBBLE_DROP, true);
		Options.register(PlayerOption.GlobalOptions.DEATH_MESSAGES, true);
		Options.register(PlayerOption.GlobalOptions.FOUND_DIAMONDS, true);
		Options.register(PlayerOption.GlobalOptions.ALTS_CONNECT_MESSAGE, false);
		Options.register(PlayerOption.GlobalOptions.IMPORTANT_NOTE_NOTIFY_CONNECT, false);
		Options.register(PlayerOption.GlobalOptions.SHOW_PLAYERS_WHEN_IN_SPAWN, true);
		
        File file = new File("spigot.yml");
        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
            UNKNOWN_COMMAND = config.getString("messages.unknown-command");
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
		
		FileConfiguration configuration = ConfigManager.getStuffsSerialConfig().getConfig();
		
		Punishment.ID = configuration.getInt("punishments-id");
		PlayerSaver.ID = configuration.getInt("player_saver-id");
	}

	@Override
	public void onDisable() {
		savers.forEach(saver -> {
			if (saver instanceof NoSQLSaver)
				Bukkit.getPluginManager().callEvent(new DatabasePreUpdateEvent((NoSQLSaver) saver));
			else if (saver instanceof FileSaver)
				((FileSaver) saver).serialize();
		});
		
		shutDownSavers.forEach(saver -> {
			if (saver instanceof NoSQLSaver)
				Bukkit.getPluginManager().callEvent(new DatabasePreUpdateEvent((NoSQLSaver) saver));
			else if (saver instanceof FileSaver)
				((FileSaver) saver).serialize();
		});
		
		FileConfiguration configuration = ConfigManager.getStuffsSerialConfig().getConfig();
		
		configuration.set("punishments-id", Punishment.ID);
		configuration.set("player_saver-id", PlayerSaver.ID);
		
		for (HCFSign hcfSign : HCFSign.getHCFSigns())
			hcfSign.serialize();
		
		Config.getConfigs().forEach(config -> config.saveConfig());
		OnimaMongo.disconnect();
		sendConsoleMessage("====================§6[§cDESACTIVATION§6]§r====================", ConfigurationService.ONIMAAPI_PREFIX);
	}
	
	public static OnimaAPI getInstance() {
		return instance;
	}
	
	public static void sendConsoleMessage(String message, String prefix) {
		Bukkit.getConsoleSender().sendMessage(prefix == null ? message : prefix + ' ' + message);
	}
	
	public static List<Scheduler> getScheduled() {
		return scheduled;
	}
	
	public static List<Saver> getSavers() {
		return savers;
	}
	
	public static List<Saver> getShutdownSavers() {
		return shutDownSavers;
	}
	
	public ListenerManager getListenerManager() {
		return listenerManager;
	}

	public EnchantLimiter getEnchantLimiter() {
		return enchantLimiter;
	}

	public PotionLimiter getPotionLimiter() {
		return potionLimiter;
	}
	
	public ChatManager getChatManager() {
		return chatManager;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}
	
	public boolean isLoaded() {
		return loaded;
	}
	
	public PlayerDisplayModifier getDisguiseFactory() {
		return factory;
	}
	
	public static void broadcast(String message, OnimaPerm permission) {
		sendConsoleMessage("[Broadcast - " + permission.getPermission() + "] " + message, null);
		for (APIPlayer apiPlayer : APIPlayer.getOnlineAPIPlayers()) {
			if (permission.has(apiPlayer.toPlayer()))
				apiPlayer.sendMessage(message);
		}
	}
	
	public static void broadcast(JSONMessage message, OnimaPerm permission) {
		sendConsoleMessage("[Broadcast - " + permission.getPermission() + "] " + message.getMessage(), null);
		for (APIPlayer apiPlayer : APIPlayer.getOnlineAPIPlayers()) {
			if (permission.has(apiPlayer.toPlayer()))
				apiPlayer.sendMessage(message);
		}
	}
	
}
