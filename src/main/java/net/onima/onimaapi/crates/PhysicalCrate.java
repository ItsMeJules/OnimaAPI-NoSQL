package net.onima.onimaapi.crates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.crates.booster.KeyBooster;
import net.onima.onimaapi.crates.prizes.Prize;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.crates.utils.CrateType;
import net.onima.onimaapi.event.crates.CrateOpenEvent;
import net.onima.onimaapi.event.crates.CrateWinEvent;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.CallbackButton;
import net.onima.onimaapi.gui.buttons.DisplayButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.menu.CrateOpeningMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.OSound;

public class PhysicalCrate extends Crate {
	
	public static final OSound TICK_SOUND;
	
	private static int[] exclusion;
	private static List<Integer> slowPick;
	
	static {
		TICK_SOUND = new OSound(Sound.CLICK, 1F, 1F);
		exclusion = new int[] {10, 11, 12, 13, 14, 15, 16};
		slowPick = Lists.newArrayList(180, 160, 150, 140, 139, 131, 123, 115, 107, 100, 93, 86, 79, 73, 67, 61, 55, 50, 45, 40, 35, 31, 27, 23, 19, 16, 13, 10, 7, 5, 3, 1);
	}
	
	protected Block block;
	
	public PhysicalCrate(Block block, String name, int prizeAmount) {
		super(name, CrateType.PHYSICAL, prizeAmount);
		
		this.block = block;
	}
	
	@Override
	public void sendShow(CommandSender sender) {
		sender.sendMessage(ConfigurationService.STAIGHT_LINE);
		sender.sendMessage("§7Crate : §d§o" + type.name() + " §f- " + (displayName == null ? name : displayName));
		sender.sendMessage("§7Nombre total de prix : §d" + prizes.size());
		
		if (OnimaPerm.ONIMAAPI_CRATE_INFO_ARGUMENT.has(sender)) {
			sender.sendMessage("§7Nombre de prix à drop : §d" + prizeAmount);
			sender.sendMessage("§7Pourcentage cumulé des prix : §d" + getTotalWeight() + '%');
			
			if (sender instanceof Player && block != null) {
				Location location = block.getWorld().getHighestBlockAt(block.getLocation()).getLocation();
				
				((Player) sender).spigot().sendMessage(new ComponentBuilder("§7§oSe téléporter à la crate.")
						.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + location.getBlockX() + ' ' + location.getBlockY() + ' ' + location.getBlockZ()))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7/tp " + location.getBlockX() + ' ' + location.getBlockY() + ' ' + location.getBlockZ()).create())).create());
			}
		}
		
		sender.sendMessage(ConfigurationService.STAIGHT_LINE);
	}
	
	public PhysicalCrate(Location location, String name, int prizeAmount) {
		this(location.getBlock(), name, prizeAmount);
	}
	
	public PhysicalCrate(String name, int prizeAmount) {
		super(name, CrateType.PHYSICAL, prizeAmount);
	}

	@Override
	public void open(APIPlayer player, KeyBooster booster) {
		if (!(this instanceof RoomCrate)) {
			List<Prize> prizes = getPrizes(booster);
			CrateOpenEvent event = new CrateOpenEvent(this, prizes, player.toPlayer(), booster);
			
			Bukkit.getPluginManager().callEvent(event);
			
			if (event.isCancelled())
				return;
			
			OpeningMenu menu = new OpeningMenu(name, booster, prizes);
			
			if (block.getState() instanceof Chest)
				((CraftWorld) block.getWorld()).getHandle().playBlockAction(block.getX(), block.getY(), block.getZ(), CraftMagicNumbers.getBlock(block), 1, 1);
		
			menu.open(player);
			menu.startRoll(player);
		}
		
		super.open(player, booster);
	}
	
	@Override
	public void close(APIPlayer player) {
		if (block.getState() instanceof Chest)
			((CraftWorld) block.getWorld()).getHandle().playBlockAction(block.getX(), block.getY(), block.getZ(), CraftMagicNumbers.getBlock(block), 1, 0);
	
		super.close(player);
	}
	
	@Override
	public void cancel(APIPlayer player) {
		PacketMenu menu = player.getViewingMenu();
		
		if (menu instanceof OpeningMenu) {
			OpeningMenu openingMenu = (OpeningMenu) menu;
			
			openingMenu.getTask().cancel();
			openingMenu.close(player, true);
		}
		
		super.close(player);
	}
	
	public void setBlock(Block block) {
		this.block = block;
	}
	
	public Block getBlock() {
		return block;
	}
	
	
	@Override
	public void serialize() {
		super.serialize();
		
		if (block != null)
			crateFile.set("crates." + type.name() + '.' + name + '.' + "location", Methods.serializeLocation(block.getLocation(), false));
	}
	
	public static PhysicalCrate getByBlock(Block block) {
		return crates.stream().filter(crate -> crate instanceof PhysicalCrate).map(crate -> (PhysicalCrate) crate).filter(crate -> Methods.locationEquals(crate.block.getLocation(), block.getLocation())).findFirst().orElse(null);
	}
	
	public static PhysicalCrate getByLocation(Location location) {
		return crates.stream().filter(crate -> crate instanceof PhysicalCrate).map(crate -> (PhysicalCrate) crate).filter(crate -> crate.block != null).filter(crate -> Methods.locationEquals(crate.block.getLocation(), location)).findFirst().orElse(null);
	}
	
	public class OpeningMenu extends CrateOpeningMenu {
		
		private KeyBooster booster;

		public OpeningMenu(String crateName, KeyBooster booster, List<Prize> prizes) {
			super(crateName, prizes, MIN_SIZE * 3);
			
			this.booster = booster;
		}

		@Override
		public void registerItems() {
			for (int i = 0; i < size; i++) {
				if (i >= 10 && i <= 16)
					continue;
				
				BetterItem item = null;
				
				if (rolling)
					item = new BetterItem(Material.STAINED_GLASS_PANE, 1, 14, "§oEn cours...");
				else
					item = new BetterItem(Material.STAINED_GLASS_PANE, 1, 5, "§aGagné !");
				
				buttons.put(i, new DisplayButton(item));
			}
		}
		
		@Override
		public void closeCrate(APIPlayer player) {
			PhysicalCrate.this.close(player);
		}
		
		@Override
		public void renderInventory(APIPlayer player, List<Prize> prizes) {
			registerItems();
			renderPrizes(prizes);
			inventory.clear();
			
			Player online = player.toPlayer();
			
			for (Entry<Integer, Button> entry : buttons.entrySet())
				inventory.setItem(entry.getKey(), createItemStack(online, entry.getValue()));
		}
		
		@Override
		public void renderPrizes(List<Prize> prizes) {
			Map<Integer, Prize> pos = new HashMap<Integer, Prize>(prizes.size());
			int index = 0;
			
			while (pos.size() != prizes.size()) {
				int generated = Methods.getRandomWithExclusion(OnimaAPI.RANDOM, 0, 26, exclusion);
				
				if (pos.containsKey(generated))
					continue;
				else {
					pos.put(generated, prizes.get(index));
					index++;
				}
			}
			
			for (Entry<Integer, Prize> entry : pos.entrySet())
				buttons.put(entry.getKey(), new DisplayButton(entry.getValue().getDisplayItem()));
		}
		
		@Override
		public void startRoll(APIPlayer player) {
			rolling = true;
			task = new BukkitRunnable() {
				int time = 0;
				int buffer = 0;
				
				@Override
				public void run() {
					if (buffer > 40) {
						
						if (time >= 200) {
							Bukkit.getPluginManager().callEvent(new CrateWinEvent(PhysicalCrate.this, prizes, player));
							
							CallbackButton<Crate> button = new CallbackButton<Crate>(new BetterItem(Material.RECORD_10, 1, 0, "§e§oRécupérer le loot.", "§7(TIP) §oVous pouvez aussi récupérer le loot en fermant l'inventaire."), PhysicalCrate.this);
							
							button.setCallBack((crate) -> {
								dropped = true;
								
								Player online = player.toPlayer();
								
								for (Prize prize : prizes)
									prize.give(online);
								
								crate.close(player);
								button.getEvent().setCancelled(true);
								return true;
							});
							
							updateTitle("§bLoot", player, false);
							renderInventory(player, prizes);
							
							buttons.put(13, button);
							inventory.setItem(13, createItemStack(player.toPlayer(), button));
							WIN_SOUND.play(block.getLocation());
							Bukkit.broadcastMessage("§6" + player.getName() + " a obtenu " + Crate.constructMultiPrize(prizes) + " §6dans la crate " + displayName + '.');

							rolling = false;
							cancel();
						} else if (slowPick.contains(time)) {
							renderInventory(player, PhysicalCrate.super.getPrizes(booster));
							TICK_SOUND.play(block.getLocation());
						} 
						
					} else {
						renderInventory(player, PhysicalCrate.super.getPrizes(booster));
						TICK_SOUND.play(block.getLocation());
					}
					
					buffer++;
					time++;
				}
			}.runTaskTimer(OnimaAPI.getInstance(), 0L, 1L);
		}
		
	}
	
}
