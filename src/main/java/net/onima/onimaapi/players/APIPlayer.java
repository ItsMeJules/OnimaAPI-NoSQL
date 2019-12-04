package net.onima.onimaapi.players;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.IInventory;
import net.minecraft.server.v1_7_R4.InventoryLargeChest;
import net.minecraft.server.v1_7_R4.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_7_R4.TileEntityChest;
import net.minecraft.server.v1_7_R4.World;
import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.disguise.DisguiseManager;
import net.onima.onimaapi.fakeblock.FakeBlock;
import net.onima.onimaapi.fakeblock.FakeType;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.menu.DisguiseMenu;
import net.onima.onimaapi.gui.menu.GlobalOptionsMenu;
import net.onima.onimaapi.gui.menu.ModOptionsMenu;
import net.onima.onimaapi.items.Wand;
import net.onima.onimaapi.mod.ModItem;
import net.onima.onimaapi.mod.SilentContainerChest;
import net.onima.onimaapi.mountain.TreasureMountain.TreasureBlock;
import net.onima.onimaapi.players.notes.Note;
import net.onima.onimaapi.players.utils.PlayerOption;
import net.onima.onimaapi.players.utils.SpecialPlayerInventory;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.saver.inventory.InventorySaver;
import net.onima.onimaapi.saver.inventory.PlayerSaver;
import net.onima.onimaapi.saver.inventory.PlayerSaver.SaveType;
import net.onima.onimaapi.signs.SignChange;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.ExperienceManager;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.zone.type.utils.Capable;

public class APIPlayer extends OfflineAPIPlayer {
	
	private static Map<UUID, APIPlayer> players;
	
	static {
		players = new HashMap<>();
	}
	
	private Player player;
	private boolean loaded, safeDisconnect;
	private Capable capable;
	private Wand wand;
	private UUID examinatedUUID;
	private Set<FakeBlock> fakeBlocks;
	private PacketMenu menu;
	private List<SignChange> signsChanged;
	private TreasureBlock treasureblock;
	private CommandSender lastMessageSender;
	private Crate crate;
	private ItemStack freezeHelmet;
	private ExperienceManager experienceManager;
	private DisguiseManager disguiseManager;
	
	{
		fakeBlocks = new HashSet<>();
		signsChanged = new ArrayList<>();
	}
	
	public APIPlayer(Player player) {
		super(player);
		
		this.player = player;

		save();
	}
	
	public Player toPlayer() {
		return player;
	}
	
	public void loadLogin() {
		disguiseManager = new DisguiseManager(name);
		
		setupPermissions();
		loadFromPerms();
		
		menus.add(new GlobalOptionsMenu(this));
		
		Methods.removeKills(this);
		UUIDCache.update(name, uuid);
		lookupAlts();
	}
	
	public void loadJoin() {
		experienceManager = new ExperienceManager(player);
		playTime.setLastPlayTimeUpdate(System.currentTimeMillis());
		SpecialPlayerInventory.onConnect(player);
		
		if (vanish)
			setVanish(true);
		
		if (!notes.isEmpty()) {
			for (Note note : notes) {
				if (note.getPriority().getPriority() == 0)
					continue;
				
				for (APIPlayer apiPlayer : getOnlineAPIPlayers()) {
					if (apiPlayer.getOptions().getBoolean(PlayerOption.GlobalOptions.IMPORTANT_NOTE_NOTIFY_CONNECT))
						note.display(apiPlayer, this);
				}
			}
		}
		
		if (!ipHistory.contains(ip))
			ipHistory.add(ip);
	}
	
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
	
	public boolean isLoaded() {
		return loaded;
	}
	
	public void loadFromPerms() {
		if (OnimaPerm.ONIMAAPI_WAND.has(player))
			wand = new Wand(1);
		
		if (OnimaPerm.MOD_COMMAND.has(player))
			menus.add(new ModOptionsMenu(this));
		
		if (OnimaPerm.ONIMAAPI_DISGUISE_COMMAND.has(player))
			menus.add(new DisguiseMenu(this));
	}
	
	public String getDisplayName(boolean showDisguise) {
		return disguiseManager.isDisguised() ? disguiseManager.getName() + (showDisguise ? " §7(§o" + super.getName() + "§7)" : "") : super.getName();
	}
	
	public String getDisplayName() {
		return getDisplayName(false);
	}
	
	@Override
	public String getColoredName(boolean realName) {
		return rank.getRankType().getNameColor() + (realName ? name : getDisplayName());
	}
	
	/**
	 * This method returns the FakeBlock at the given location.
	 * 
	 * @param location - The location where the fake block is.
	 * @return FakeBlock if the player sees one.<br>
	 * <tt>null</tt> if the player sees no fake block.
	 */
	public FakeBlock getFakeBlock(Location location) {
		return fakeBlocks.parallelStream().filter(fakeBlock -> Methods.locationEquals(fakeBlock.getLocation(), location)).findFirst().orElse(null);
	}
	
	public Set<FakeBlock> getFakeBlocks() {
		return fakeBlocks;
	}
	
	/**
	 * This method sends a block change to the player (client sided).
	 */
	public void addFakeBlock(FakeBlock fakeBlock) {
		fakeBlocks.add(fakeBlock);
		fakeBlock.send(toPlayer());
	}
	
	/**
	 * This method removes the given fake block to the given player.
	 * 
	 * @param fakeBlock - FakeBlock to remove.
	 */
	public void removeFakeBlock(FakeBlock fakeBlock) {
		if (fakeBlocks.remove(fakeBlock))
			fakeBlock.reset(player);
	}
	
	/**
	 * This method removes all the fake block which have this {@link FakeType}
	 * 
	 * @param type - FakeType to remove.
	 */
	public void removeFakeBlockByType(FakeType type) {
		if (type != null) {
			Iterator<FakeBlock> iterator = fakeBlocks.iterator();
			
			while (iterator.hasNext()) {
				FakeBlock fakeBlock = iterator.next();
				
				if (fakeBlock.getType() != type) continue;
				
				fakeBlock.reset(player);
				iterator.remove();
			}
		}
	}
	
	/**
	 * This method is generally for connection purposes. It resends all the fake blocks after 5 ticks.
	 */
	public void updateFakeBlocks() {
		if (fakeBlocks.isEmpty()) return;
			
		Bukkit.getScheduler().scheduleSyncDelayedTask(OnimaAPI.getInstance(), () -> fakeBlocks.parallelStream().forEach(fb -> fb.send(player)), 5L);
	}
	
	public void setViewingMenu(PacketMenu menu) {
		this.menu = menu;
	}
	
	/**
	 * This method returns the menu that the player is currently viewing.
	 */
	public PacketMenu getViewingMenu() {
		return menu;
	}
	
	/**
	 * This method opens the given menu for this player.
	 * 
	 * @param menu - The menu to open.
	 */
	public void openMenu(PacketMenu menu) {
		menu.open(this);
	}
	
	/**
	 * Close the menu that the player is currently viewing.
	 * @param forceClose - Should we call {@link Player#closeInventory()}
	 */
	public void closeMenu(boolean forceClose) {
		if (menu != null)
			menu.close(this, forceClose);
	}
	
	public void closeMenu() {
		closeMenu(false);
	}
	
	/**
	 * This method sets the area which the player is capping.
	 * 
	 * @param capable - The area being capped.
	 */
	public void setCapping(Capable capable) {
		this.capable = capable;
	}
	
	/**
	 * This method returns the area which the player is capping.
	 * 
	 * @return the area the player is capping.
	 */
	public Capable getCapping() {
		return capable;
	}
	
	/**
	 * This method checks if the player is capping the given area.
	 * 
	 * @param capable - The area this profile might be capping.
	 * @return true if the profile is capping the given area.<br>
	 * false if the profile is not capping the given area.
	 */
	public boolean isCapping(Capable capable) {
		return this.capable.equals(capable);
	}
	
	/**
	 * This method returns this player's wand.
	 * 
	 * @return The player's wand.
	 */
	public Wand getWand() {
		return wand;
	}
	
	/**
	 * Gets the ping of the given player.
	 * 
	 * @param player - Player to get the ping from.
	 * @return
	 */
	public int getPing() {
		return ((CraftPlayer) player).getHandle().ping;
	}
	
	public String getFacingDirection() {
		double rotation = (player.getLocation().getYaw() - 90) % 360;
		
		if (rotation < 0)
			rotation += 360.0;
		if (0 <= rotation && rotation < 22.5)
			return "W";
		else if (22.5 <= rotation && rotation < 67.5)
			return "NW";
		else if (67.5 <= rotation && rotation < 112.5)
			return "N";
		else if (112.5 <= rotation && rotation < 157.5)
			return "NE";
		else if (157.5 <= rotation && rotation < 202.5)
			return "E";
		else if (202.5 <= rotation && rotation < 247.5)
			return "SE";
		else if (247.5 <= rotation && rotation < 292.5)
			return "S";
		else if (292.5 <= rotation && rotation < 337.5)
			return "SW";
		else if (337.5 <= rotation && rotation < 360.0)
			return "W";
		else
			return null;
	}
	
	 /**
	  * This method checks if the player has moved more than one block.
	  * 
	  * @param location - The location where the player goes.
	  * @return true if the player has moved more than one block.
	  */
	 public boolean hasMovedOneBlockTo(Location location) {
		 Location from = player.getLocation();
		 return from.getBlockX() != location.getBlockX() 
				 || from.getBlockY() != location.getBlockY() 
				 || from.getBlockZ() != location.getBlockZ();
	 }
	 
	public void setExaminating(UUID examinatedUUID) {
		this.examinatedUUID = examinatedUUID;
	}
	
	public UUID getExaminating() {
		return examinatedUUID; 
	}
	
	public void setModMode(boolean modMode) {
		super.modMode = modMode;
		
		if (modMode) {
			PlayerInventory inventory = player.getInventory();
			
			playerDataSaved.add(new PlayerSaver(this, PlayerSaver.SaveType.MOD_MODE, null));
			Methods.clearInventory(player);
			Methods.clearEffects(player);
			experienceManager.setExp(0);
			player.setFireTicks(0);
			player.setFoodLevel(20);
			player.setSaturation(20);
			player.setHealth(((Damageable) player).getMaxHealth());
			player.setCanPickupItems(!options.getBoolean(PlayerOption.ModOptions.PICKUP_ITEM));
			player.spigot().setAffectsSpawning(false);
			player.spigot().setCollidesWithEntities(false);
			player.spigot().setViewDistance(Bukkit.getViewDistance());
			
			for (ModItem modItem : ModItem.getModItems()) {
				inventory.setItem(modItem.getSlot(), modItem.getItem().toItemStack());
				modItem.update(this);
			}
			
		} else {
			Iterator<PlayerSaver> iterator = playerDataSaved.iterator();
			
			while (iterator.hasNext()) {
				InventorySaver saver = iterator.next();
				
				if (saver instanceof PlayerSaver) {
					PlayerSaver playerSaver = (PlayerSaver) saver;
					
					if (playerSaver.getSaveType() == SaveType.MOD_MODE) {
						playerSaver.restore(player);
						iterator.remove();
					}
				}
			}
			
			player.setCanPickupItems(true);
			player.spigot().setAffectsSpawning(true);
			player.spigot().setCollidesWithEntities(true);
		}
	}
	
	public void setFrozen(boolean frozen) {
		if (frozen) {
			freezeHelmet = player.getInventory().getHelmet();
			
			player.getInventory().setHelmet(new ItemStack(Material.PACKED_ICE));
			openMenu(PacketMenu.getMenu("freeze_menu"));
		} else {
			player.getInventory().setHelmet(freezeHelmet);
			freezeHelmet = null;
			
			closeMenu();
		}
		
		super.setFrozen(frozen);
	}
	
	public void sendMessage(String message) {
		player.sendMessage(message);
	}
	
	public void sendMessage(BaseComponent... components) {
		player.spigot().sendMessage(components);
	}
	
	public void sendMessage(JSONMessage jsonMessage, String prefix) {
		jsonMessage.send(player, prefix);
	}
	
	public void sendMessage(JSONMessage jsonMessage) {
		jsonMessage.send(player);
	}
	
	public void hidePlayer(Player target, long time) {
		player.hidePlayer(target);
		Bukkit.getScheduler().scheduleSyncDelayedTask(OnimaAPI.getInstance(), () -> player.showPlayer(target), time * 20 / 1000); 
	}
	
	public void setSafeDisconnect(boolean safeDisconnect) {
		this.safeDisconnect = safeDisconnect;
	}
	
	public boolean canDisconnectSafely() {
		return safeDisconnect;
	}

	public void sendSignChange(Sign sign, String[] newLines, long time, boolean forceChange) {
		String[] lines = sign.getLines();
		
		if (Arrays.equals(lines, newLines))
			return;
		
		Iterator<SignChange> iterator = signsChanged.iterator();
		
		while (iterator.hasNext()) {
			SignChange signChange = iterator.next();
			if (signChange.getSign().equals(sign)) {
				
				if (!forceChange && Arrays.equals(signChange.getNewLines(), newLines))
					return;

				signChange.getRunnable().cancel();
				iterator.remove();
				break;
			}
		}
		
		Location location = sign.getLocation();
		player.sendSignChange(location, newLines);
		SignChange signChange;
		
		if (signsChanged.add(signChange = new SignChange(sign, newLines))) {
			Block block = sign.getBlock();
			BlockState previous = block.getState();
			BukkitRunnable runnable = new BukkitRunnable() {
				public void run() {
					if (signsChanged.remove(signChange) && previous.equals(block.getState()))
						player.sendSignChange(location, lines);
				}
			};
			
			runnable.runTaskLater(OnimaAPI.getInstance(), time * 20 / 1000);
			signChange.setRunnable(runnable);
		}
	}
	
	public void resetSign(Sign sign) {
		Iterator<SignChange> iterator = signsChanged.iterator();
		
		while (iterator.hasNext()) {
			SignChange signChange = iterator.next();
			
			if (sign == null || signChange.getSign().equals(sign)) {
				signChange.getRunnable().cancel();
				signChange.getSign().update();
				iterator.remove();
			}
		}
	}
	
	public void setTreasureBlock(TreasureBlock treasureBlock) {
		this.treasureblock = treasureBlock;
	}
	
	public TreasureBlock getTreasureBlock() {
		return treasureblock;
	}

	public void setLastMessageSender(CommandSender lastMessageSender) {
		this.lastMessageSender = lastMessageSender;
	}

	public CommandSender getLastMessageSender() {
		return lastMessageSender;
	}

	public void setCrateOpening(Crate crate) {
		this.crate = crate;
	}
	
	public Crate getCrateOpening() {
		return crate;
	}
	
	public boolean silentChest(Location location) {
		int x = location.getBlockX(), y = location.getBlockY(), z = location.getBlockZ();
		
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		World world = entityPlayer.world;
		Object chest = (TileEntityChest) world.getTileEntity(x, y, z);
	    
		if (chest == null)
			return true;
	    
		int id = net.minecraft.server.v1_7_R4.Block.getId(world.getType(x, y, z));
	    
		if (net.minecraft.server.v1_7_R4.Block.getId(world.getType(x - 1, y, z)) == id)
			chest = new InventoryLargeChest("Large chest", (TileEntityChest) world.getTileEntity(x - 1, y, z), (IInventory) chest);

		if (net.minecraft.server.v1_7_R4.Block.getId(world.getType(x + 1, y, z)) == id)
			chest = new InventoryLargeChest("Large chest", (IInventory) chest, (TileEntityChest) world.getTileEntity(x + 1, y, z));
	   
		if (net.minecraft.server.v1_7_R4.Block.getId(world.getType(x, y, z - 1)) == id)
	    	chest = new InventoryLargeChest("Large chest", (TileEntityChest) world.getTileEntity(x, y, z - 1), (IInventory) chest);
		
		if (net.minecraft.server.v1_7_R4.Block.getId(world.getType(x, y, z + 1)) == id)
	    	chest = new InventoryLargeChest("Large chest", (IInventory) chest, (TileEntityChest) world.getTileEntity(x, y, z + 1));
		
		boolean returnValue = true;
	    
		try {
			int windowId = 0;
	
			try {
				Field windowID = entityPlayer.getClass().getDeclaredField("containerCounter");
				windowID.setAccessible(true);
				
				windowId = windowID.getInt(entityPlayer) % 100 + 1;
				windowID.setInt(entityPlayer, windowId);
			} catch (NoSuchFieldException localNoSuchFieldException) {}
			IInventory inventoryImpl = (IInventory) chest;
			
			for (Entity entity : player.getNearbyEntities(16, 16, 16)) {
				if (!(entity instanceof Player))
					continue;
				
				if (entity.getLocation().distanceSquared(location) <= 256)
					entity.setMetadata("silent-open", new FixedMetadataValue(OnimaAPI.getInstance(), "lol"));
			}	
	        
			entityPlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(windowId, 0, inventoryImpl.getInventoryName(), inventoryImpl.getSize(), true));
			entityPlayer.activeContainer = new SilentContainerChest(entityPlayer.inventory, inventoryImpl);
			entityPlayer.activeContainer.windowId = windowId;
			entityPlayer.activeContainer.addSlotListener(entityPlayer);
	        
			player.sendMessage(ModItem.MOD_PREFIX + " §fVous ouvrez un coffre silencieusement.");
			returnValue = false;
		} catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ConfigurationService.ONIMAAPI_PREFIX + "Erreur lors de l'envoi du coffre, veuillez contacter Jestiz.");
		}
	      
		return returnValue;
	}
	
	public ExperienceManager getExperienceManager() {
		return experienceManager;
	}
	
	public void setVanish(boolean vanish) {
		if (vanish) {
			for (Player online : Bukkit.getOnlinePlayers()) {
				if (online.getUniqueId().equals(player.getUniqueId()))
					continue;
				
				online.hidePlayer(player);
			}
		} else {
			for (Player online : Bukkit.getOnlinePlayers()) {
				if (online.getUniqueId().equals(player.getUniqueId()))
					continue;
				
				online.showPlayer(player);
			}
		}
		
		super.setVanish(vanish);
	}
	
	public double getHealth() {
		return ((Damageable) player).getHealth();
	}
	
	public double getMaxHealth() {
		return ((Damageable) player).getMaxHealth();
	}
	
	public void setupPermissions() {
		for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
			if (attachmentInfo.getAttachment() == null)
				continue;

			attachmentInfo.getAttachment().getPermissions().forEach((permission, value) -> attachmentInfo.getAttachment().unsetPermission(permission));
		}

		PermissionAttachment attachment = null;

		for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
			if (attachmentInfo.getAttachment() == null)
				continue;

			if (attachmentInfo.getAttachment().getPlugin() instanceof OnimaAPI) {
				attachment = attachmentInfo.getAttachment();
				break;
			}
		}

		if (attachment == null)
			attachment = player.addAttachment(OnimaAPI.getInstance());

		for (String effectivePermission : rank.getRankType().getPermissions().stream().map(OnimaPerm::getPermission).collect(Collectors.toList()))
			attachment.setPermission(effectivePermission, true);

		if (rank.getRankType().getValue() < 14) {
			attachment.setPermission("bukkit.command.version", false);
			attachment.setPermission("bukkit.command.help", false);
			attachment.setPermission("bukkit.command.me", false);
			attachment.setPermission("bukkit.command.icanhasbukkit", false);
			attachment.setPermission("bukkit.command.tell", false);
		}
		
		player.recalculatePermissions();
	}
	
	public DisguiseManager getDisguiseManager() {
		return disguiseManager;
	}
	
	@Override
	public void save() {
		players.put(uuid, this);
	}

	@Override
	public void remove() {
		super.remove();
		players.remove(uuid);
	}

	@Override
	public boolean isSaved() {
		return players.containsKey(uuid);
	}

	public static APIPlayer getPlayer(UUID uuid) {
		return players.get(uuid);
	}
	
	public static APIPlayer getPlayer(String name) {
		Player online = Bukkit.getPlayer(name);
		
		if (online == null)
			return null;
		else
			return getPlayer(online.getUniqueId());
	}
	
	public static APIPlayer getPlayer(Player player) {
		return players.get(player.getUniqueId());
	}
	
	public static Map<UUID, APIPlayer> getAPIPlayers() {
		return players;
	}
	
	public static Collection<APIPlayer> getOnlineAPIPlayers() {
		return players.values();
	}

}
