package net.onima.onimaapi.fasttiles;

import org.bukkit.Location;

import net.onima.onimaapi.tasks.FastTileTask;

public interface FastTile {

    public Location getLocation();
    public boolean action();
    
    public default void register() {
    	FastTileTask.getFastTiles().add(this);
    }

}
