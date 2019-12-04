package net.onima.onimaapi.crates;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

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
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.OEffect;
import net.onima.onimaapi.utils.OSound;
import net.onima.onimaapi.zone.Cuboid;

public class RoomCrate extends PhysicalCrate {
											  
	private static boolean busy;
	
	private Location teleportLocation;
	private List<RoomChest> chests;
	private List<Prize> winPrizes;
	private boolean canOpen;
	private int chestsLeftToOpen;
	private Cuboid cuboid;
	
	{
		chestsLeftToOpen = prizeAmount;
	}
	
	public RoomCrate(Block block, String name, int prizeAmount) {
		super(block, name, prizeAmount);
		
		type = CrateType.ROOM;
	}

	public RoomCrate(Location location, String name, int prizeAmount) {
		super(location, name, prizeAmount);
		
		type = CrateType.ROOM;
	}
	
	public RoomCrate(String name, int prizeAmount) {
		super(name, prizeAmount);
		
		type = CrateType.ROOM;
	}
	
	public boolean setRoom(Cuboid cuboid) {
		this.cuboid = cuboid;
		
		chests = StreamSupport.stream(Spliterators.spliteratorUnknownSize(cuboid.iterator(), Spliterator.ORDERED), true)
		.filter(block -> block.getState() instanceof Chest)
		.map(chest -> new RoomChest(chest.getLocation()))
		.collect(Collectors.toCollection(() -> new ArrayList<>(10)));
		
		return !chests.isEmpty() && chests.size() >= prizeAmount;
	}
	
	@Override
	public void open(APIPlayer player, KeyBooster booster) {
		winPrizes = getPrizes(booster);
		
		CrateOpenEvent event = new CrateOpenEvent(this, winPrizes, player.toPlayer(), booster);
		
		Bukkit.getPluginManager().callEvent(event);
		
		if (event.isCancelled())
			return;
		
		player.toPlayer().teleport(teleportLocation);
		busy = true;
		canOpen = false;
		chestsLeftToOpen = prizeAmount;
		
		new BukkitRunnable() {
			int toRemove = chests.size() - prizeAmount;
			
			@Override
			public void run() {
				if (toRemove != 0) {
					RoomChest chest = null;
					
					do
						chest = chests.get(OnimaAPI.RANDOM.nextInt(chests.size()));
					while (chest.isRemoved());
					
					if (chest != null)
						chest.playRemove();
				} else {
					cancel();
					
					canOpen = true;
					int i = 0;
					
					for (RoomChest chest : chests) {
						if (!chest.isRemoved()) {
							chest.setPrize(winPrizes.get(i));
							i++;
						}
					}
					
					player.sendMessage("§aCliquez sur les coffres pour recevoir vos prix !");
				}
				
				toRemove--;
			}
			
		}.runTaskTimer(OnimaAPI.getInstance(), 0L, 10L);
		
		super.open(player, booster);
	}
	
	@Override
	public void cancel(APIPlayer player) {
		player.toPlayer().teleport(block.getLocation().add(0, 1, 0));
		
		for (RoomChest roomChest : chests)
			roomChest.reset();
		
		busy = false;
		super.close(player);
	}
	
	@Override
	public void sendShow(CommandSender sender) {
		sender.sendMessage(ConfigurationService.STAIGHT_LINE);
		sender.sendMessage("§7Crate : §d§o" + type.name() + " §f- " + (displayName == null ? name : displayName));
		sender.sendMessage("§7Nombre total de prix : §d" + prizes.size());
		sender.sendMessage("§7Salle d'ouverture : " + (busy ? "§coccupé" : "§alibre"));
		
		if (OnimaPerm.ONIMAAPI_CRATE_INFO_ARGUMENT.has(sender)) {
			sender.sendMessage("§7Nombre de prix à drop : §d" + prizeAmount);
			sender.sendMessage("§7Pourcentage cumulé des prix : §d" + getTotalWeight() + '%');
			sender.sendMessage("§7Nombre de coffres dans la salle d'ouverture : §d" + chests.size());
			
			if (sender instanceof Player && block != null) {
				Location location = block.getWorld().getHighestBlockAt(block.getLocation()).getLocation();
				
				((Player) sender).spigot().sendMessage(new ComponentBuilder("§7§oSe téléporter à la crate.")
						.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + location.getBlockX() + ' ' + location.getBlockY() + ' ' + location.getBlockZ()))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7/tp " + location.getBlockX() + ' ' + location.getBlockY() + ' ' + location.getBlockZ()).create())).create());
			
				if (teleportLocation != null) {
					((Player) sender).spigot().sendMessage(new ComponentBuilder("§7§oSe téléporter à la salle d'ouverture.")
							.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + teleportLocation.getBlockX() + ' ' + teleportLocation.getBlockY() + ' ' + teleportLocation.getBlockZ()))
							.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7/tp " + teleportLocation.getBlockX() + ' ' + teleportLocation.getBlockY() + ' ' + teleportLocation.getBlockZ()).create())).create());
				}
				
			}
		}
		
		sender.sendMessage(ConfigurationService.STAIGHT_LINE);
	}
	
	public boolean loot(Player player, Location location) {
		if (!canOpen)
			return false;
		
		boolean cancelOpening = false;
		
		for (RoomChest roomChest : chests) {
			if (Methods.locationEquals(roomChest.location, location)) {
				if (roomChest.isOpened())
					return true;
				
				roomChest.open();
				chestsLeftToOpen--;
				cancelOpening = true;
			}
		}
		
		if (chestsLeftToOpen == 0) {
			APIPlayer apiPlayer = APIPlayer.getPlayer(player);
			
			Bukkit.getPluginManager().callEvent(new CrateWinEvent(RoomCrate.this, prizes, apiPlayer));
			player.sendMessage("§aVous avez ouvert tous les coffre, toutes les récompenses vous ont été donnée. \n§cVous serez téléporté en-dehors de la salle dans 15 secondes.");
			Bukkit.broadcastMessage("§6" + apiPlayer.getName() + " a obtenu " + Crate.constructMultiPrize(winPrizes) + " §6dans la crate " + displayName + '.');
			
			for (Prize prize : winPrizes)
				prize.give(player);
			
			Bukkit.getScheduler().runTaskLater(OnimaAPI.getInstance(), () -> cancel(apiPlayer), 15 * 20L);
			
			canOpen = false;
		}
		
		return cancelOpening;
	}
	
	public Location getTeleportLocation() {
		return teleportLocation;
	}

	public void setTeleportLocation(Location teleportLocation) {
		this.teleportLocation = teleportLocation;
	}
	
	public List<RoomChest> getRoomChests() {
		return chests;
	}
	
	public boolean canOpen() {
		return canOpen;
	}
	
	@Override
	public void serialize() {
		super.serialize();
		
		String path = "crates." + type.name() + '.' + name + '.';
		
		crateFile.set(path + "tp-location", Methods.serializeLocation(teleportLocation, true));
		
		if (cuboid != null) {
			crateFile.set(path + "cuboid-loc-min", Methods.serializeLocation(cuboid.getMinimumLocation(), false));
			crateFile.set(path + "cuboid-loc-max", Methods.serializeLocation(cuboid.getMaximumLocation(), false));
		}
	
	}
	
	public static boolean isBusy() {
		return busy;
	}

	public static void setBusy(boolean busy) {
		RoomCrate.busy = busy;
	}
	
	private static class RoomChest {
		
		private Location location;
		private Prize prize;
		private boolean opened;
		private BlockFace face;
		
		public RoomChest(Location location) {
			this.location = location;
			
			MaterialData data = location.getBlock().getState().getData();
			
			if (data instanceof Directional)
				face = ((Directional) data).getFacing();
		}
		
		public void open() {
			opened = true;
			
			((CraftWorld) location.getWorld()).getHandle().playBlockAction(location.getBlockX(), location.getBlockY(), location.getBlockZ(), CraftMagicNumbers.getBlock(location.getBlock()), 1, 1);
			prize.hologram(location.clone().add(0.5, 1.4, 0.5));
		}
		
		public void close() {
			opened = false;
			
			((CraftWorld) location.getWorld()).getHandle().playBlockAction(location.getBlockX(), location.getBlockY(), location.getBlockZ(), CraftMagicNumbers.getBlock(location.getBlock()), 1, 0);
			prize.clearHologram();
		}
		
		@SuppressWarnings("deprecation")
		public void playRemove() {
			new OSound(Sound.DIG_WOOD, 1F, 1F).play(location);
			new OEffect(Effect.STEP_SOUND, location.getBlock().getTypeId()).play(location);
			location.getBlock().setType(Material.AIR);
		}
		
		public void reset() {
			if (!isRemoved())
				close();
			
			Block block = location.getBlock();
			
			block.setType(Material.CHEST);
			
			if (face != null && face != BlockFace.SOUTH) {
				BlockState state = block.getState();
				Directional dir = (Directional) block.getState().getData();
				
				dir.setFacingDirection(face);
				state.setData((MaterialData) dir);
				state.update(true);
			}
		}
		
		public void setPrize(Prize prize) {
			this.prize = prize;
		}
		
		public boolean isOpened() {
			return opened;
		}
		
		public boolean isRemoved() {
			return location.getBlock().getType() != Material.CHEST;
		}
		
	}

}
