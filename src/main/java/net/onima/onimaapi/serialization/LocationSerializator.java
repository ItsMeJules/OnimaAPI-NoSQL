package net.onima.onimaapi.serialization;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerializator {
	
	private Location location;
	private Location deserialize;
	private String serialization;
	
	public LocationSerializator(Location location) {
		this.location = location;
	}
	
	public LocationSerializator(String serialization) {
		this.serialization = serialization;
	}
	
	public void serialize() {
		serialization = new StringBuilder().append(location.getWorld().getName()).append("/")
				.append(location.getBlockX()).append("/")
				.append(location.getBlockY()).append("/")
				.append(location.getBlockZ()).append("/").toString();
	}
	
	public void deserialize() {
		String[] details = serialization.split("/");
		
		World world = Bukkit.getWorld(details[0]);
		int x = Integer.valueOf(details[1]);
		int y = Integer.valueOf(details[2]);
		int z = Integer.valueOf(details[3]);
		
		deserialize = new Location(world, x, y, z);
	}
	
	public void serializeWithDetails() {
		serialization = new StringBuilder().append(location.getWorld().getName()).append("/")
				.append(location.getBlockX()).append("/")
				.append(location.getBlockY()).append("/")
				.append(location.getBlockZ()).append("/")
				.append(location.getPitch()).append("/")
				.append(location.getYaw()).append("/").toString();
	}
	
	public void deserializeWithDetails() {
		String[] details = serialization.split("/");
		
		World world = Bukkit.getWorld(details[0]);
		int x = Integer.valueOf(details[1]);
		int y = Integer.valueOf(details[2]);
		int z = Integer.valueOf(details[3]);
		float pitch = Float.valueOf(details[4]);
		float yaw = Float.valueOf(details[5]);
		
		deserialize = new Location(world, x, y, z, yaw, pitch);
	}
	
	public String getSerialized() {
		return serialization;
	}

	public Location getDeserialized() {
		return deserialize;
	}

}
