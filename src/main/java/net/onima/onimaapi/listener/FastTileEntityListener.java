package net.onima.onimaapi.listener;

import java.util.Iterator;

import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import net.onima.onimaapi.fasttiles.FastBrewingStand;
import net.onima.onimaapi.fasttiles.FastFurnace;
import net.onima.onimaapi.fasttiles.FastTile;
import net.onima.onimaapi.tasks.FastTileTask;

public class FastTileEntityListener implements Listener {
	
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            
            if (block.getState() instanceof Furnace || block.getState() instanceof BrewingStand) {
            	FastTile fastTile = FastTileTask.getByLocation(block.getLocation());
            	
            	if (fastTile == null) {
                	if (block.getState() instanceof Furnace) new FastFurnace((Furnace) block.getState()).register();
                	else new FastBrewingStand((BrewingStand) block.getState()).register();
            	}
            }
        }
    }

    @EventHandler
    public void onChunkUnloadEvent(ChunkUnloadEvent event) {
        Iterator<FastTile> iterator = FastTileTask.getFastTiles().iterator();
        
        while (iterator.hasNext()) {
        	FastTile tile = iterator.next();
        	
            if (tile.getLocation().getChunk().equals(event.getChunk()))
                iterator.remove();
        }
    }
    
}

