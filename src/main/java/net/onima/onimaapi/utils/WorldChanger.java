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

public class WorldChanger implements FileSaver {
	
	private static List<WorldChanger> worlds;
	private static Config stuffsSerialConfig;
	private static FileConfiguration config;
	
	private String toWorldName, fromWorldName;
	private Location spawnLocation;
	private boolean useTravelAgent;
	
	static {
		worlds = new ArrayList<>();
		stuffsSerialConfig = ConfigManager.getStuffsSerialConfig();
		config = stuffsSerialConfig.getConfig();
	}
	
	public WorldChanger(String fromWorldName, String toWorldName, Location spawnLocation, boolean useTravelAgent) {
		this.toWorldName = toWorldName;
		this.fromWorldName = fromWorldName;
		this.spawnLocation = spawnLocation;
		this.useTravelAgent = useTravelAgent;
		save();
	}
	
	public WorldChanger(String fromWorldName, String toWorldName) {
		this(fromWorldName, toWorldName, null, false);
	}
	
	public String getFromWorldName() {
		return fromWorldName;
	}
	
	public String getToWorldName() {
		return toWorldName;
	}
	
	public Location getSpawnLocation() {
		return spawnLocation;
	}
	
	public void setSpawnLocation(Location spawnLocation) {
		this.spawnLocation = spawnLocation;
	}
	
	public boolean shouldUseTravelAgent() {
		return useTravelAgent;
	}

	public void setUseTravelAgent(boolean useTravelAgent) {
		this.useTravelAgent = useTravelAgent;
	}
	
	@Override
	public void save() {
		worlds.add(this);
		OnimaAPI.getShutdownSavers().add(this);
	}

	@Override
	public void remove() {
		worlds.remove(this);
		OnimaAPI.getShutdownSavers().remove(this);
	}

	@Override
	public void serialize() {
		FileConfiguration config = stuffsSerialConfig.getConfig();
		
		config.set("world-changer."+toWorldName+'/'+fromWorldName+".location", spawnLocation == null ? "" : Methods.serializeLocation(spawnLocation, true));
		config.set("world-changer."+toWorldName+'/'+fromWorldName+".useTravelAgent", useTravelAgent);
	}

	@Override
	public void refreshFile() {
		ConfigurationSection section = config.getConfigurationSection("world-changer");
		
		if (section != null) {
			List<String> fromToWorlds = worlds.stream().map(worldChanger -> {
				return new StringBuilder().append(worldChanger.toWorldName).append('/').append(worldChanger.fromWorldName).toString();
			}).collect(Collectors.toList());
			
			for (String fromToWorldName : section.getKeys(false)) {
				if (!fromToWorlds.contains(fromToWorldName))
					stuffsSerialConfig.remove("world-changer."+fromToWorldName, false);
			}
		}
	}

	@Override
	public boolean isSaved() {
		return worlds.contains(this);
	}
	
	public static List<WorldChanger> getWorldChangers() {
		return worlds;
	}
	
	public static WorldChanger getChanger(String fromWorldName, String toWorldName) {
		for (WorldChanger changer : worlds) {
			if (changer.fromWorldName.equalsIgnoreCase(fromWorldName) && changer.toWorldName.equalsIgnoreCase(toWorldName))
				return changer;
		}
		return null;
	}
	
	public static List<WorldChanger> getChanger(String fromWorldName) {
		return worlds.parallelStream().filter(changer -> changer.fromWorldName.equalsIgnoreCase(fromWorldName)).collect(Collectors.toList());
	}

	public static void deserialize() {
		FileConfiguration config = stuffsSerialConfig.getConfig();
		ConfigurationSection section = config.getConfigurationSection("world-changer");
		
		if (section == null) return;
		
		for (String worlds : section.getKeys(false)) {
			String[] world = worlds.split("/");
			WorldChanger.worlds.add(new WorldChanger(world[1], world[0], Methods.deserializeLocation(config.getString("world-changer."+worlds+".location"), true), config.getBoolean("world-changer."+worlds+".useTravelAgent")));
		}
	}
	
}
