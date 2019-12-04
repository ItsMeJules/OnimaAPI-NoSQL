package net.onima.onimaapi.zone.type.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface MinecraftCuboid {

	/**
	 * This method returns the location1 of this cuboid.
	 * 
	 * @return The location1.
	 */
	public Location getLocation1();
	
	/**
	 * This method sets the location1 of this cuboid.
	 * 
	 * @param loc1 - First location to set. 
	 */
	public void setLocations(Location loc1, Location loc2);
	
	/**
	 * This method returns the location2 of this cuboid.
	 * 
	 * @return The location2.
	 */
	public Location getLocation2();
	
	/**
	 * This method returns if the given profile is in the cuboid.
	 * 
	 * @param profile - Profile to check if he's inside.
	 * @return true if the profile is inside.<br>
	 * false if the profile is outside.
	 */
	public boolean isInside(Player player);
}
