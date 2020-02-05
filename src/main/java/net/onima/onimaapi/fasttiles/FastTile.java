package net.onima.onimaapi.fasttiles;

import org.bukkit.Location;

import net.onima.onimaapi.tasks.FastTileTask;

public interface FastTile {
	
    Location getLocation();
    boolean action();
    
    default void register() {
    	FastTileTask.getFastTiles().add(this);
    }

}
