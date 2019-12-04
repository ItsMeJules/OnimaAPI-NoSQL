package net.onima.onimaapi.mountain.utils;

import java.time.Instant;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.manager.ConfigManager;
import net.onima.onimaapi.mountain.GlowstoneMountain;
import net.onima.onimaapi.mountain.OreMountain;
import net.onima.onimaapi.mountain.TreasureMountain;
import net.onima.onimaapi.mountain.TreasureMountain.TreasureBlock;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.saver.FileSaver;
import net.onima.onimaapi.utils.CasualFormatDate;
import net.onima.onimaapi.utils.Config;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.Scheduler;
import net.onima.onimaapi.utils.time.Time.LongTime;
import net.onima.onimaapi.zone.Cuboid;
import net.onima.onimaapi.zone.type.utils.MinecraftCuboid;

public abstract class Mountain implements FileSaver, MinecraftCuboid, Scheduler {
	
	protected static List<Mountain> mountains;
	protected static Config mountainSerialConfig;
	protected static FileConfiguration config;
	protected static boolean refreshed;
	
	static {
		mountains = new ArrayList<>();
		mountainSerialConfig = ConfigManager.getMountainSerialConfig();
		config = mountainSerialConfig.getConfig();
	}
	
	protected String creator, name, id;
	protected long created, timeRestart;
	protected Cuboid cuboid;
	protected MountainGenerator generator;
	protected List<Location> generated;
	protected Temporal temporal;
	protected boolean schedulerEnabled, schedulerSet, blocksRegistered;
	protected MountainType type;
	
	{
		created = System.currentTimeMillis();
	}
	
	public Mountain(String id, String name, Location location1, Location location2, String creator) {
		this(id, name, creator);
		
		cuboid = new Cuboid(location1, location2, true);
	}
	
	public Mountain(String id, String name, String creator) {
		this.id = id;
		this.name = name;
		this.creator = creator;
		save();
	}
	
	public Mountain() {
	}
	
	/** Uses default generator.
	 * 
	 */
	public abstract String getTimeAnnouncement();
	public abstract void generate();
	public abstract void generate(MountainGenerator generator);
	public abstract double getPercentageLeft();
	public abstract boolean canBuild(Block block);
	public abstract boolean areBlocksRegistered();
	public abstract void registerBlocks();
	
	public void sendShow(CommandSender sender) {
		sender.sendMessage(ConfigurationService.STAIGHT_LINE);
		sender.sendMessage("§7Montagne : §d§o" + type.name() + ' ' + name + " §7- Créateur : §d§o" + creator + " §7- Monde : §d§o" + (cuboid == null ? "§cAucun" : "§a" + cuboid.getWorld().getName()));
		
		if (OnimaPerm.ONIMAAPI_MOUNTAIN_SHOW_ARGUMENT.has(sender)) {
			sender.sendMessage("§7Créé le : §d§o" + Methods.toFormatDate(created, ConfigurationService.DATE_FORMAT_HOURS));
			sender.sendMessage("§7Nombre actuel de blocks lootable : §d§o" + (generated == null ? 0 : generated.size()));
			
			if (sender instanceof Player && cuboid != null) {
				Location location = cuboid.getWorld().getHighestBlockAt(cuboid.getMinimumLocation()).getLocation();
				
				((Player) sender).spigot().sendMessage(new ComponentBuilder("§7§oSe téléporter à la montagne.")
						.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + location.getBlockX() + ' ' + location.getBlockY() + ' ' + location.getBlockZ()))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7/tp " + location.getBlockX() + ' ' + location.getBlockY() + ' ' + location.getBlockZ()).create())).create());
			}
			
			sender.sendMessage("§7Zone des loots : " + (cuboid != null ? "§acréée" : "§cnon-créée") + "§7.");
			if (cuboid != null) {
				Vector min = cuboid.getMinimum(), max = cuboid.getMaximum();
				
				sender.sendMessage(" §7- Location n°1 : §d§o" + min.getBlockX() + ' ' + min.getBlockY() + ' ' + min.getBlockZ());
				sender.sendMessage(" §7- Location n°2 : §d§o" + max.getBlockX() + ' ' + max.getBlockY() + ' ' + max.getBlockZ());
			}
		}
		
		if (isSchedulerEnabled() && isSchedulerSet()) {
			sender.sendMessage("§7Prochain resest pour : §d§o" + new CasualFormatDate("d hi").toNormalDate(getWhenItStarts()));
			sender.sendMessage("§7Dernier reset : §d§o" + new CasualFormatDate("d hi").toNormalDate(System.currentTimeMillis() - timeRestart));
		}
		
		
		sender.sendMessage(ConfigurationService.STAIGHT_LINE);
	}
	
	public void timeAnnouncement(String message) {
		if (temporal == null) return;
		
		ZonedDateTime now = ZonedDateTime.now();
		long secondsLeft = now.until(temporal, ChronoUnit.SECONDS);
		
		if (secondsLeft == 3600 || (secondsLeft % 900 == 0 && secondsLeft != 0) || secondsLeft == 60)
			Bukkit.broadcastMessage(message.replace("%time%", LongTime.setYMDWHMSFormat(getStartTimeLeft())));
	}
	
	public void generatedAnnouncement(String message) {
		Location middle = cuboid.getCenterLocation();
		
		Bukkit.broadcastMessage(message.replace("%X%", String.valueOf(middle.getBlockX())).replace("%Y%", String.valueOf(middle.getBlockY())).replace("%Z%", String.valueOf(middle.getBlockZ())));
	}
	
	public void breakAnnouncement(String message) {
		int breakPercentage = (int) getPercentageLeft();
		
		if (breakPercentage % 25 == 0)
			Bukkit.broadcastMessage(message.replace("%percent%", String.valueOf(breakPercentage)));
	}
	
	public String getCreator() {
		return creator;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getCreated() {
		return created;
	}
	
	public Cuboid getCuboid() {
		return cuboid;
	}
	
	public void setCuboid(Cuboid cuboid) {
		this.cuboid = cuboid;
	}
	
	public MountainGenerator getGenerator() {
		return generator;
	}
	
	public void setGenerator(MountainGenerator generator) {
		this.generator = generator;
	}
	
	public List<Location> getGenerated() {
		return generated;
	}
	
	public MountainType getType() {
		return type;
	}
	
	@Override
	public void refreshFile() {
		ConfigurationSection section = config.getConfigurationSection("mountains");
		
		if (section != null) {
			List<String> mountainsId = mountains.stream().map(Mountain::getId).collect(Collectors.toList());
			for (String id : section.getKeys(false)) {
				if (!mountainsId.contains(id))
					mountainSerialConfig.remove("mountains." + id, false);
			}
		}
		refreshed = true;
	}
	
	@Override
	public void save() {
		mountains.add(this);
		OnimaAPI.getShutdownSavers().add(this);
	}
	
	@Override
	public void remove() {
		mountains.remove(this);
		OnimaAPI.getShutdownSavers().remove(this);
	}
	
	@Override
	public void serialize() {
		if (!refreshed)
			refreshFile();
		
		String path = "mountains." + type.name() + '.' + name + '.';
		
		config.set(path + "id", id);
		config.set(path + "creator", creator);
		config.set(path + "name", name);
		config.set(path + "created", created);
		config.set(path + "loc1", Methods.serializeLocation(cuboid.getMinimumLocation(), false));
		config.set(path + "loc2", Methods.serializeLocation(cuboid.getMaximumLocation(), false));
		config.set(path + "scheduler-enabled", schedulerEnabled);
		config.set(path + "reset-time-cycle", timeRestart);
		config.set(path + "next-start", getWhenItStarts());
	}
	
	@Override
	public boolean isSaved() {
		return mountains.contains(this) && OnimaAPI.getShutdownSavers().contains(this);
	}	
	
	@Override
	public Location getLocation1() {
		return cuboid.getMinimumLocation();
	}

	@Override
	public void setLocations(Location loc1, Location loc2) {
		cuboid = new Cuboid(loc1, loc2, true);
	}

	@Override
	public Location getLocation2() {
		return cuboid.getMaximumLocation();
	}

	@Override
	public boolean isInside(Player player) {
		return cuboid.contains(player);
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
		temporal = ZonedDateTime.now().withMonth(month.getValue()).withDayOfMonth(day).withHour(hour).withMinute(minute);
	}
	
	@Override
	public void action(boolean started) {
		if (!Bukkit.isPrimaryThread()) {
			Bukkit.getScheduler().runTask(OnimaAPI.getInstance(), () -> {
				if (started)
					generate();
				else
					timeAnnouncement(getTimeAnnouncement());
			});
		} else {
			if (started)
				generate();
			else
				timeAnnouncement(getTimeAnnouncement());
		}
	}
	
	@Override
	public boolean isSchedulerEnabled() {
		return schedulerEnabled;
	}
	
	@Override
	public void setSchedulerEnabled(boolean schedulerEnabled) {
		if (schedulerEnabled)
			OnimaAPI.getScheduled().add(this);
		else
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
	
	public static List<Mountain> getMountains() {
		return mountains;
	}

	public static Mountain getByName(String name) {
		return mountains.stream().filter(mountain -> mountain.name.equalsIgnoreCase(name)).findFirst().orElse(null);
	}
	
	public static Mountain getByLocation(Location location) {
		return mountains.stream().filter(mountain -> mountain.cuboid.contains(location)).findFirst().orElse(null);
	}
	
	public static void deserialize() {
		ConfigurationSection section = config.getConfigurationSection("mountains");
		
		if (section != null) {
			int mountains = 0, glowstoneMountains = 0, oreMountains = 0, treasureMountains = 0;
			
			for (String typeStr : section.getKeys(false)) {
				ConfigurationSection nameSection = config.getConfigurationSection("mountains." + typeStr);
				MountainType type = MountainType.fromString(typeStr);
				
				if (nameSection != null) {
					for (String name : nameSection.getKeys(false)) {
						Mountain mountain = null;
						
						mountains++;
						
						switch (type) {
						case GLOWSTONE:
							GlowstoneMountain glowstoneMountain = new GlowstoneMountain();
							
							for (String str : nameSection.getStringList(name + ".blocks"))
								glowstoneMountain.getBlocksToSet().add(Methods.deserializeLocation(str, false));
							
							mountain = glowstoneMountain;
							glowstoneMountains++;
							break;
						case ORES:
							OreMountain oreMountain = new OreMountain();
							
							for (String str : nameSection.getStringList(name + ".blocks")) {
								String[] parts = str.split("#");
								
								oreMountain.getBlocksToSet().put(Methods.deserializeLocation(parts[0], false), Material.getMaterial(parts[1]));
							}
							
							mountain = oreMountain;
							oreMountains++;
							break;
						case TREASURE:
							TreasureMountain treasureMountain = new TreasureMountain();
							
							for (String line : nameSection.getStringList(name + ".blocks")) {
								String[] infos = line.split("_");
								TreasureBlock block = new TreasureBlock(Methods.deserializeLocation(infos[0], false), Material.getMaterial(infos[1]), treasureMountain);
								
								if (infos.length > 2)
									block.setFace(BlockFace.valueOf(infos[2]));
									
								if (infos.length > 3)
									block.setItems(Methods.deserializeItems(infos[3]));
								
								treasureMountain.getBlocks().put(block.getLocation(), block);
							}
							
							for (String line : nameSection.getStringList(name + ".double-chest")) {
								String[] infos = line.split("_");
								TreasureBlock block = new TreasureBlock(Methods.deserializeLocation(infos[0], false), Methods.deserializeLocation(infos[1], false), Material.getMaterial(infos[2]), treasureMountain);
								
								block.setFace(BlockFace.valueOf(infos[3]));
								block.setItems(Methods.deserializeItems(infos[4]));
								
								treasureMountain.getBlocks().put(block.getLocation(), block);
							}
							
							mountain = treasureMountain;
							treasureMountains++;
							break;
						default:
							break;
						}

						Long resetTime = nameSection.getLong(name + ".reset-time-cycle");
						Long nextStart = nameSection.getLong(name + ".next-start");
						
						mountain.name = name;
						mountain.id = nameSection.getString(name + ".id");
						mountain.creator = nameSection.getString(name + ".creator");
						mountain.created = nameSection.getLong(name + ".created");
						mountain.cuboid = new Cuboid(Methods.deserializeLocation(nameSection.getString(name + ".loc1"), false), Methods.deserializeLocation(nameSection.getString(name + ".loc2"), false), true);
						mountain.schedulerEnabled = nameSection.getBoolean(name + ".scheduler-enabled");

						if (resetTime != null && nextStart != null) {
							
							if (nextStart != -1) {
								while (nextStart <= System.currentTimeMillis())
									nextStart += resetTime;
								
								mountain.setTemporal(ZonedDateTime.ofInstant(Instant.ofEpochMilli(nextStart), OnimaAPI.TIME_ZONE));
								OnimaAPI.sendConsoleMessage("§fProchain lancement de la montagne " + mountain.name + ' ' + new CasualFormatDate("d u z hi").toNormalDate(nextStart), ConfigurationService.ONIMAAPI_PREFIX);
							}
							
							mountain.scheduleEvery(resetTime);
						}
						
						mountain.generate();
						mountain.save();
					}
				}
			}
			
			OnimaAPI.sendConsoleMessage("§aNous avons chargé " + mountains + " mountain" + (mountains > 1 ? "s" : "") + " (Glowstone Mountain: " + glowstoneMountains + ") (Ore Mountain: " + oreMountains + ") (Treasure Mountain: " + treasureMountains + ")", ConfigurationService.ONIMAAPI_PREFIX);
		}
	}
	
}
