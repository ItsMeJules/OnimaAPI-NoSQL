package net.onima.onimaapi.crates;

import java.time.Instant;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.crates.booster.KeyBooster;
import net.onima.onimaapi.crates.prizes.ItemPrize;
import net.onima.onimaapi.crates.prizes.Prize;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.crates.utils.CrateType;
import net.onima.onimaapi.event.crates.CrateOpenEvent;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.InstantFirework;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.OEffect;
import net.onima.onimaapi.utils.OSound;
import net.onima.onimaapi.utils.Scheduler;
import net.onima.onimaapi.utils.time.Time.LongTime;
import net.onima.onimaapi.workload.type.SchedulerWorkload;

public class SupplyCrate extends Crate implements Scheduler {

	private static List<SupplyCrate> dropped;
	
	static {
		dropped = new ArrayList<>();
	}
	
	private FallingBlock fallingBlock;
	private SupplyCrateState state;
	private Location location;
	private Hologram hologram;
	private BukkitTask task;
	private Temporal temporal;
	private long timeRestart;
	private boolean schedulerEnabled;
	private List<Prize> prizes;
	
	{
		displayName = "Ravitaillement";
	}
	
	public SupplyCrate(String name, int prizeAmount) {
		super(name, CrateType.SUPPLY, prizeAmount);
	}
	
	@Override
	public void open(APIPlayer player, KeyBooster booster) {
		CrateOpenEvent event = new CrateOpenEvent(this, prizes, player.toPlayer(), booster);
		Bukkit.getPluginManager().callEvent(event);
		
		if (event.isCancelled()) {
			Bukkit.getScheduler().runTask(OnimaAPI.getInstance(), () -> player.toPlayer().closeInventory());
			return;
		}
		
		super.open(player, booster);
	}
	
	@Override
	public void close(APIPlayer player) {
		dropped.remove(this);
		super.close(player);
	}
	
	@Override
	public void cancel(APIPlayer player) {
		if (state == null)
			return;
		
		if (state == SupplyCrateState.LANDED)
			location.getBlock().setType(Material.AIR);
		else if (state == SupplyCrateState.DROPPING)
			fallingBlock.remove();
		else
			task.cancel();
			
		Bukkit.broadcastMessage("§cCoffre de ravitaillement annulé !");
		state = null;
		dropped.remove(this);
	}
	
	@Override
	public void sendShow(CommandSender sender) {
		sender.sendMessage(ConfigurationService.STAIGHT_LINE);
		sender.sendMessage("§7Crate : §d§o" + type.name() + " §f- " + (displayName == null ? name : displayName));
		sender.sendMessage("§7Nombre total de prix : §d" + super.prizes.size());
		
		if (OnimaPerm.ONIMAAPI_CRATE_INFO_ARGUMENT.has(sender)) {
			sender.sendMessage("§7Nombre de prix à drop : §d" + prizeAmount);
			sender.sendMessage("§7Pourcentage cumulé des prix : §d" + getTotalWeight() + '%');
			sender.sendMessage("§7Prochain drop dans : §d" + LongTime.setHMSFormat(getStartTimeLeft()));
			sender.sendMessage("§7En cours de drop : " + (dropped.contains(this) ? "§aoui" : "§cnon"));
			
			if (super.prizes != null)
				sender.sendMessage("§7Prix dans ce drop : §d" + Crate.constructMultiPrize(super.prizes));
			
			if (sender instanceof Player && location != null) {
				Location location = this.location.getWorld().getHighestBlockAt(this.location).getLocation();
				
				((Player) sender).spigot().sendMessage(new ComponentBuilder("§7§oSe téléporter au drop.")
						.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + location.getBlockX() + ' ' + location.getBlockY() + ' ' + location.getBlockZ()))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7/tp " + location.getBlockX() + ' ' + location.getBlockY() + ' ' + location.getBlockZ()).create())).create());
			}
		}
		
		sender.sendMessage(ConfigurationService.STAIGHT_LINE);
	}
	
	@SuppressWarnings("deprecation")
	public void destroy() {
		new OSound(Sound.DIG_WOOD, 1F, 1F).play(location);
		new OEffect(Effect.STEP_SOUND, location.getBlock().getTypeId()).play(location);
		
		location.getBlock().setType(Material.AIR);
		hologram.delete();
	}
	
	@SuppressWarnings("deprecation")
	public void drop(Location location) {
		World world = location.getWorld();
		
		dropped.add(this);
		Bukkit.broadcastMessage("§eUn coffre de ravitaillement va être lâché dans 10 minutes en §c" + location.getBlockX() + ", " + location.getBlockZ() + " §edans §fl'Overworld §e.");
		
		task = Bukkit.getScheduler().runTaskLater(OnimaAPI.getInstance(), 
				() -> {
					Chunk chunk = location.getChunk();
					
					if (!chunk.isLoaded())
						chunk.load();
					
					fallingBlock = world.spawnFallingBlock(location, Material.CHEST, (byte) 0);
					state = SupplyCrateState.DROPPING;
					track();
				}, 10 * 20);
		
		state = SupplyCrateState.WAITING;
	}
	
	public void land(Location location) {
		if (location.getBlock().getType() != Material.CHEST)
			location.getBlock().setType(Material.CHEST);
		
		hologram = HologramsAPI.createHologram(OnimaAPI.getInstance(), location.clone().add(0, 1, 0));
		
		Bukkit.broadcastMessage("§eUn coffre de ravitaillement a atteri en §c" + location.getBlockX() + ", " + location.getBlockZ() + " §edans §fl'Overworld §e.");
		location.getWorld().createExplosion(location, 5F, true);
		
		prizes = getPrizes(NO_BOOSTER);
		hologram.appendTextLine(displayName);
		state = SupplyCrateState.LANDED;
		this.location = location;
		
		Chest chest = (Chest) location.getBlock().getState();
		Inventory inv = chest.getBlockInventory();
		List<Integer> pos = new ArrayList<>();
		
		while (pos.size() != prizes.size()) {
			int generated = OnimaAPI.RANDOM.nextInt(0, 27);
			
			if (pos.contains(generated))
				continue;
			
			pos.add(generated);
		}
		
		for (int i = 0; i < pos.size(); i++)
			inv.setItem(pos.get(i), ((ItemPrize) prizes.get(i)).getItem());
	
		chest.update(true);
	}
	
	public void track() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if (!fallingBlock.getLocation().getChunk().isLoaded())
					fallingBlock.getLocation().getChunk().load();
					
				if (!fallingBlock.isDead())
					InstantFirework.spawn(fallingBlock.getLocation(), FireworkEffect.builder().with(FireworkEffect.Type.BALL).withColor(Color.ORANGE).withFade(Color.WHITE).build());
				else {
					land(fallingBlock.getLocation());
					cancel();
				}
			}
			
		}.runTaskTimer(OnimaAPI.getInstance(), 0L, 10L);
	}
	
	public SupplyCrateState getState() {
		return state;
	}

	public Location getLocation() {
		return location;
	}
	
	@Override
	public void remove() {
		super.remove();
		OnimaAPI.getScheduled().remove(this);
	}
	
	@Override
	public Temporal getTemporal() {
		return temporal;
	}

	@Override
	public void setTemporal(Temporal temporal) {
		this.temporal = temporal;
	}

	@Override
	public void scheduleEvery(long timeRestart) {
		this.timeRestart = timeRestart;
	}

	@Override
	public long getResetTimeCycle() {
		return timeRestart;
	}
	
	@Override
	public void startTime(Month month, int day, int hour, int minute) {
		temporal = ZonedDateTime.now().withMonth(month.getValue()).withDayOfMonth(day).withHour(hour).withMinute(minute).withSecond(0);
	}
	
	@Override
	public void action(boolean started) {
		if (started) {
			int spawn = ConfigurationService.SPAWN_RADIUS_ENV.get(Environment.NORMAL);
			int[] exclusion = new int[spawn * 2 + 1];
			int i = 0;
			
			for (int j = -spawn; j <= spawn; j++) {
				exclusion[i] = j;
				i++;
			}
			
			drop(new Location(Bukkit.getWorld("world"), Methods.getRandomWithExclusion(OnimaAPI.RANDOM, -500, 500, exclusion), 250, Methods.getRandomWithExclusion(-500, 500, exclusion)));
		}
	}
	
	@Override
	public boolean isSchedulerEnabled() {
		return schedulerEnabled;
	}
	
	@Override
	public void setSchedulerEnabled(boolean schedulerEnabled) {
		if (schedulerEnabled) {
			long nextDrop = getWhenItStarts();
			
			while (nextDrop <= System.currentTimeMillis())
				nextDrop += timeRestart;
			
			temporal = ZonedDateTime.ofInstant(Instant.ofEpochMilli(nextDrop), OnimaAPI.TIME_ZONE);
			
			OnimaAPI.getScheduled().add(this);
			OnimaAPI.getDistributor().get(OnimaAPI.getInstance().getWorkloadManager().getSchedulerId()).addWorkload(new SchedulerWorkload(this));
		} else
			OnimaAPI.getScheduled().remove(this);
		
		this.schedulerEnabled = schedulerEnabled;
	}
	
	@Override
	public boolean isSchedulerSet() {
		return temporal != null;
	}
	
	@Override
	public long getStartTimeLeft() {
		if (temporal == null) return -1;
		
		return ZonedDateTime.now().until(temporal, ChronoUnit.MILLIS);
	}
	
	@Override
	public long getWhenItStarts() {
		if (temporal == null) return -1;
		
		return Instant.from(temporal).toEpochMilli();
	}
	
	@Override
	public void serialize() {
		super.serialize();
		
		String path = "crates." + type.name() + '.' + name + '.';
		
		crateFile.set(path + "reset-time-cycle", timeRestart);
		crateFile.set(path + "next-start", getWhenItStarts());
		crateFile.set(path + "scheduler-enabled", schedulerEnabled);
	}
	
	public static List<SupplyCrate> getDropped() {
		return dropped;
	}
	
	public static SupplyCrate getDroppedByLocation(Location location) {
		return dropped.stream().filter(crate -> crate.location != null).filter(crate -> Methods.locationEquals(crate.location, location)).findFirst().orElse(null);
	}
	
	public class DropMenu {

		private List<Prize> prizes;
		private Inventory inv;
		
		public DropMenu(List<Prize> prizes) {
			inv = Bukkit.createInventory(null, 3 * PacketMenu.MIN_SIZE, "§6Drop");
			
			for (Prize prize : prizes) {
				if (prize.getEffect() != null)
					prize.firework(location);
			}
		}
		
		public Inventory registerItems() {
			List<Integer> pos = new ArrayList<>();
			
			while (pos.size() != prizes.size()) {
				int generated = OnimaAPI.RANDOM.nextInt(0, 27);
				
				if (pos.contains(generated))
					continue;
				
				pos.add(generated);
			}
			
			for (int i = 0; i < pos.size(); i++)
				inv.setItem(pos.get(i), ((ItemPrize) prizes.get(i)).getItem());
			
			return inv;
		}
		
		public void closeCrate() {
			SupplyCrate.this.close(null);
		}
		
	}
 
	public enum SupplyCrateState {
		WAITING,
		DROPPING,
		LANDED;
	}

}
