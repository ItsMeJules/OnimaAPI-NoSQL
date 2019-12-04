package net.onima.onimaapi.zone.type;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.google.common.collect.Sets;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.manager.ConfigManager;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.rank.RankType;
import net.onima.onimaapi.saver.FileSaver;
import net.onima.onimaapi.utils.Config;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.zone.Cuboid;
import net.onima.onimaapi.zone.struct.Flag;
import net.onima.onimaapi.zone.type.utils.Deathbannable;
import net.onima.onimaapi.zone.type.utils.FlagZone;
import net.onima.onimaapi.zone.type.utils.MinecraftCuboid;

public class Region implements MinecraftCuboid, FileSaver, Deathbannable, FlagZone {

	protected static List<Region> regions;
	protected static OnimaAPI plugin;
	protected static boolean refreshed;
	protected static Config areaSerialConfig;
	
	static {
		regions = new ArrayList<>();
		plugin = OnimaAPI.getInstance();
		areaSerialConfig = ConfigManager.getRegionSerialConfig();
	}
	
	protected String name, displayName, creator;
	protected long created;
	protected Cuboid cuboid;
	protected boolean deathban, dtrLoss;
	protected int priority;
	protected double deathbanMultiplier;
	protected List<Flag> flags;
	protected RankType accessRank;
	
	{
		created = System.currentTimeMillis();
		deathban = true;
		dtrLoss = true;
		deathbanMultiplier = 1.0;
		flags = new ArrayList<>();
		accessRank = RankType.DEFAULT;
	}
	
	public Region(String name, String displayName, String creator, Location location1, Location location2) {
		this.name = name;
		this.displayName = Methods.colors(displayName);
		this.creator = creator;
		cuboid = new Cuboid(location1, location2, true);
		save();
	}
	
	public void sendShow(CommandSender sender) {
		boolean hasPerm = sender instanceof ConsoleCommandSender ? true : OnimaPerm.ONIMAAPI_MOUNTAIN_SHOW_ARGUMENT.has(sender);
		
		sender.sendMessage(ConfigurationService.STAIGHT_LINE);
		sender.sendMessage("§7Region : §d§o" + name + " §7- Créateur : §d§o" + creator + " §7- Monde : §d§o" + (cuboid == null ? "§cAucun" : "§a" + cuboid.getWorld().getName()));
		
		if (hasPerm) {
			sender.sendMessage("§7Créé le : §d§o" + Methods.toFormatDate(created, ConfigurationService.DATE_FORMAT_HOURS));
			sender.sendMessage("§7Rank d'accès : " + accessRank.getName());
			JSONMessage json = new JSONMessage("§7Nombre de flags : §a" + flags.size(), "§aCliquez pour afficher les flags.");
			
			json.setClickString("/region flag " + name + " list");
			json.setClickAction(ClickEvent.Action.RUN_COMMAND);
			json.send(sender);
			sender.sendMessage("§7Deathban : " + (deathban ? "§atrue" : "§cfalse"));
			sender.sendMessage("§7Perte de DTR : " + (dtrLoss ? "§atrue" : "§cfalse"));
			sender.sendMessage("§7Multiplicateur de deathban : " + (deathbanMultiplier > 1
						? deathbanMultiplier > 1.5 
							? deathbanMultiplier > 2 ? "§c" : "§6"
						: "§e"
					: "§a") + deathbanMultiplier);
			sender.sendMessage("§7Priorité : §a" + priority);
			if (sender instanceof Player && cuboid != null) {
				Location location = cuboid.getWorld().getHighestBlockAt(cuboid.getMinimumLocation()).getLocation();
				
				((Player) sender).spigot().sendMessage(new ComponentBuilder("§7§oSe téléporter à la région.")
						.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + location.getBlockX() + ' ' + location.getBlockY() + ' ' + location.getBlockZ()))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7/tp " + location.getBlockX() + ' ' + location.getBlockY() + ' ' + location.getBlockZ()).create())).create());
			}
			
			if (cuboid != null) {
				Vector min = cuboid.getMinimum(), max = cuboid.getMaximum();
				
				sender.sendMessage(" §7- Location n°1 : §d§o" + min.getBlockX() + ' ' + min.getBlockY() + ' ' + min.getBlockZ());
				sender.sendMessage(" §7- Location n°2 : §d§o" + max.getBlockX() + ' ' + max.getBlockY() + ' ' + max.getBlockZ());
			}
		}
		
		sender.sendMessage(ConfigurationService.STAIGHT_LINE);
	}
	
	public String getCreator() {
		return creator;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	/**
	 * This method returns the area name.
	 * 
	 * @return The area name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * This method sets the name for this area.
	 * 
	 * @param name - The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * This method returns the area displayName.
	 * 
	 * @return The area displayName.
	 */
	public String getDisplayName(CommandSender sender) {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * This method set the priority for this area. Remember 0 is the default value.
	 * 
	 * @param priority - The priority to set.
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	/**
	 * This method returns the area priority.
	 * 
	 * @return The area priority.
	 */
	public int getPriority() {
		return priority;
	}
	
	/**
	 * This method returns an instance of cuboid which represents this area.
	 * 
	 * @return The cuboid of this area.
	 */
	public Cuboid toCuboid() {
		return cuboid;
	}
	
	/**
	 * This method sets the cuboid of this area.
	 * 
	 * @param cuboid - The cuboid to set.
	 */
	public void setCuboid(Cuboid cuboid) {
		this.cuboid = cuboid;
	}
	
	@Override
	public Location getLocation1() {
		return cuboid.getMinimumLocation();
	}

	@Override
	public void setLocations(Location location1, Location location2) {
		cuboid = new Cuboid(location1, location2, true);
	}

	@Override
	public Location getLocation2() {
		return cuboid.getMaximumLocation();
	}

	@Override
	public boolean isInside(Player player) {
		return cuboid.contains(player.getLocation());
	}
	
	@Override
	public boolean isDeathbannable() {
		return deathban;
	}

	@Override
	public void setDeathban(boolean deathban) {
		this.deathban = deathban;
	}

	@Override
	public void setDTRLoss(boolean dtrLoss) {
		this.dtrLoss = dtrLoss;
	}

	@Override
	public boolean hasDTRLoss() {
		return dtrLoss;
	}
	
	@Override
	public double getDeathbanMultiplier() {
		return deathbanMultiplier;
	}

	@Override
	public void setDeathbanMultiplier(double multiplier) {
		deathbanMultiplier = multiplier;
	}
	
	@Override
	public List<Flag> getFlags() {
		return flags;
	}
	
	@Override
	public void addFlag(Flag flag) {
		flags.add(flag);
	}
	
	@Override
	public void setFlags(Flag... flags) {
		this.flags.clear();
		this.flags.addAll(Sets.newHashSet(flags));
	}
	
	@Override
	public void setFlags(String string) {
		String[] flagsOnString = string.split(";");
		flags.clear();
		
		if (string.isEmpty()) return;
		
		for (String flagName : flagsOnString) {
			Flag flag = Flag.fromName(flagName);
			
			if (flag != null)
				flags.add(flag);
		}
	}
	
	@Override
	public String flagsToString() {
		StringBuilder builder = new StringBuilder();
		
		if (flags.isEmpty()) {
			builder.append("");
			return builder.toString();
		}
		
		for (Flag flag : flags)
			builder.append(flag.getName()+";");
		
		return builder.toString();
	}
	
	@Override
	public boolean hasFlags() {
		return !flags.isEmpty();
	}
	
	@Override
	public boolean hasFlags(Flag... flags) {
		return this.flags.containsAll(Sets.newHashSet(flags));
	}
	
	@Override
	public boolean hasOneOfThisFlags(Flag... flags) {
		for (Flag flag : flags)
			return this.flags.contains(flag);
		
		return false;
	}
	
	@Override
	public boolean hasFlag(Flag flag) {
		return flags.contains(flag);
	}
	
	@Override
	public void removeFlag(Flag flag) {
		flags.remove(flag);
	}
	
	public RankType getAccessRank() {
		return accessRank;
	}
	
	public void setAccessRank(RankType accessRank) {
		this.accessRank = accessRank;
	}
	
	@Override
	public void save() {
		regions.add(this);
		OnimaAPI.getShutdownSavers().add(this);
	}
	
	@Override
	public void remove() {
		regions.remove(this);
		OnimaAPI.getShutdownSavers().remove(this);
		if (regions.size() == 0)
			areaSerialConfig.remove("regions", false);
	}

	@Override
	public void serialize() {
		FileConfiguration config = areaSerialConfig.getConfig();
		
		if (!refreshed)
			refreshFile();
		
		String path = "regions." + name + ".";
			
		config.set(path + "name", name);
		config.set(path + "display-name", displayName);
		config.set(path + "creator", creator);
		config.set(path + "loc1", Methods.serializeLocation(cuboid.getMinimumLocation(), false));
		config.set(path + "loc2", Methods.serializeLocation(cuboid.getMaximumLocation(), false));
		config.set(path + "deathban", deathban);
		config.set(path + "dtr-loss", dtrLoss);
		config.set(path + "priority", priority);
		config.set(path + "death-ban-multiplier", deathbanMultiplier);
		config.set(path + "created", created);
		config.set(path + "flags", flagsToString());
		config.set(path + "access-rank", accessRank == null ? null : accessRank.toString());
	}

	@Override
	public void refreshFile() {
		ConfigurationSection section = areaSerialConfig.getConfig().getConfigurationSection("regions");
		
		if (section != null) {
			List<String> regionsName = regions.stream().map(Region::getName).collect(Collectors.toList());
			for (String name : section.getKeys(false)) {
				if (!regionsName.contains(name))
					areaSerialConfig.remove("regions."+name, false);
			}
		}
		
		refreshed = true;
	}

	@Override
	public boolean isSaved() {
		return regions.contains(this);
	}
	
	public static Region getByName(String name) {
		return regions.stream().filter(region -> region.name.equalsIgnoreCase(name)).findFirst().orElse(null);
	}
	
	public static Region getRegionAt(Location location) {
		List<Region> regions = new LinkedList<>();
		
		for (Region region : Region.regions) {
			if (region.toCuboid().contains(location))
				 regions.add(region);
		}
		
		if (regions.isEmpty())
			return null;
		else {
			Region priorited = regions.get(0);
			
			if (regions.size() > 1) {
				for (Region region : regions) {
					if (region.getPriority() > priorited.getPriority())
						priorited = region;
				}
			}
			
			return priorited;
		}
	}
	
	public static List<Region> getRegions() {
		return regions;
	}
	
	public static void deserialize() {
		FileConfiguration config = areaSerialConfig.getConfig();
		ConfigurationSection section = config.getConfigurationSection("regions");
		int regions = 0;
		
		if (section != null) {
			for (String id : section.getKeys(false)) {
				String path = "regions." + id +".";
				
				regions++;
				Region region = new Region(config.getString(path + "name"), config.getString(path + "display-name"), config.getString(path + "creator"), Methods.deserializeLocation(config.getString(path+"loc1"), false), Methods.deserializeLocation(config.getString(path+"loc2"), false));
				
				region.setDeathban(config.getBoolean(path + "deathban"));
				region.setDTRLoss(config.getBoolean(path + "dtr-loss"));
				region.setPriority(config.getInt(path + "priority"));
				region.setDeathbanMultiplier(config.getDouble(path + "death-ban-multiplier"));
				region.setCreated(config.getLong(path + "created"));
				region.setFlags(config.getString(path + "flags"));
			}
			
			OnimaAPI.sendConsoleMessage("§aNous avons chargé " + regions + " region" + (regions > 1 ? "s" : ""), ConfigurationService.ONIMAAPI_PREFIX);
		}
	}

}
