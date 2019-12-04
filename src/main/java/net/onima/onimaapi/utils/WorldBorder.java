package net.onima.onimaapi.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

public class WorldBorder {
	
	private static Map<String, Double> borders;
	
	static {
		borders = new HashMap<>();
		borders.put("world", 4750D);
		borders.put("world_nether", 2000D);
		borders.put("world_the_end", 2000D);
	}
	
	public static Map<String, Double> getBorders() {
		return borders;
	}
	
	public static boolean border(Location location) {
		Double border = borders.get(location.getWorld().getName());
		
		if (border == null) return false;
		
		return Math.abs(location.getBlockX()) > border || Math.abs(location.getBlockZ()) > border;
	}

	public static void resetBorders() {
		borders.put("world", 4750D);
		borders.put("world_nether", 2000D);
		borders.put("world_the_end", 2000D);
	}
	
}
