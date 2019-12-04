package net.onima.onimaapi.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.manager.ConfigManager;
import net.onima.onimaapi.saver.FileSaver;

public class Warp implements FileSaver {
	
	private static List<Warp> warps;
	private static Config stuffConfig;
	private static FileConfiguration config;
	private static boolean refreshed;
	
	static {
		warps = new ArrayList<>();
		stuffConfig = ConfigManager.getStuffsSerialConfig();
		config = stuffConfig.getConfig();
	}
	
	private String name, creator;
	private Location location;
	private long creation;
	
	public Warp(String name, Location location) {
		this.name = name;
		this.location = location;
		
		creation = System.currentTimeMillis();
		
		save();
	}

	public String getName() {
		return name;
	}
	
	public String getCreator() {
		return creator;
	}
	
	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Location getLocation() {
		return location;
	}

	public long getCreation() {
		return creation;
	}

	@Override
	public void save() {
		warps.add(this);
		OnimaAPI.getShutdownSavers().add(this);
	}

	@Override
	public void remove() {
		warps.remove(this);
		OnimaAPI.getShutdownSavers().remove(this);
	}

	@Override
	public boolean isSaved() {
		return warps.contains(this) && OnimaAPI.getShutdownSavers().contains(this);
	}

	@Override
	public void serialize() {
		String path = "warps." + name + ".";
		
		if (!refreshed)
			refreshFile();
		
		config.set(path + "creator", creator);
		config.set(path + "location", Methods.serializeLocation(location, true));
		config.set(path + "creation", creation);
	}

	@Override
	public void refreshFile() {
		ConfigurationSection section = config.getConfigurationSection("warps");
		
		if (section != null) {
			List<String> warpsName = warps.stream().map(warps -> warps.name).collect(Collectors.toList());
			
			for (String name : section.getKeys(false)) {
				if (!warpsName.contains(name))
					stuffConfig.remove("warps." + name, false);
			}
		}
	}
	
	public static List<Warp> getWarps() {
		return warps;
	}
	
	public static Warp getByName(String name) {
		return warps.stream().filter(warp -> warp.name.equalsIgnoreCase(name)).findFirst().orElse(null);
	}
	
	public static List<String> getWarpNames() {
		return warps.stream().map(Warp::getName).collect(Collectors.toCollection(() -> new ArrayList<>(warps.size())));
	}
	
	public static void deserialize() {
		ConfigurationSection section = config.getConfigurationSection("warps");
		int warps = 0;

		if (section != null) {
			for (String name : section.getKeys(false)) {
				Warp warp = new Warp(name, Methods.deserializeLocation(section.getString(name + ".location"), true));
				
				warp.creator = section.getString(name + ".creator");
				warp.creation = section.getLong(name + ".creation");
				warps++;
			}
		}
		
		OnimaAPI.sendConsoleMessage("§aNous avons chargé " + warps + " warp" + (warps > 1 ? "s" : ""), ConfigurationService.ONIMAAPI_PREFIX);
	}

}
