package net.onima.onimaapi.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

import net.onima.onimaapi.utils.ConfigurationService;

public class EntityLimitListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SLIME_SPLIT)
            return;
        
        switch (event.getSpawnReason()) {
            case NATURAL:
                if (event.getLocation().getChunk().getEntities().length > ConfigurationService.MAX_NATURAL_CHUNK_ENTITIES)
                    event.setCancelled(true);
                break;
            case CHUNK_GEN:
                if (event.getLocation().getChunk().getEntities().length > ConfigurationService.MAX_CHUNK_GENERATED_ENTITIES)
                    event.setCancelled(true);
                break;
            case SPAWNER:
                if (event.getLocation().getChunk().getEntities().length > ConfigurationService.MAX_SPAWNED_CHUNK_ENTITIES)
                    event.setCancelled(true);
                break;
		default:
			break;
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onItemSpawn(ItemSpawnEvent event) {
        List<Item> items = new ArrayList<>();
        for (Entity entity : event.getLocation().getChunk().getEntities()) {
            if (entity instanceof Item)
                items.add((Item) entity);
        }
        
        while (items.size() > ConfigurationService.MAX_ITEM_CHUNK_ENTITIES)
            items.remove(ThreadLocalRandom.current().nextInt(items.size())).remove();
    }
}
