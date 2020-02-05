package net.onima.onimaapi.tasks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import net.onima.onimaapi.fasttiles.FastTile;
import net.onima.onimaapi.utils.Methods;

public class FastTileTask extends BukkitRunnable {

	private static List<FastTile> fastTiles;
	
	{
		fastTiles = new ArrayList<>(500);
	}
	
    @Override
    public void run() {
        Iterator<FastTile> iterator = fastTiles.iterator();
        
        while (iterator.hasNext()) {
        	if (iterator.next().action())
        		iterator.remove();
        }
    }

	public static FastTile getByLocation(Location location) {
		for (FastTile fastTile : fastTiles) {
			if (Methods.locationEquals(fastTile.getLocation(), location))
				return fastTile;
		}
		
		return null;
	}
		
	public static List<FastTile> getFastTiles() {
		return fastTiles;
	}
	
}