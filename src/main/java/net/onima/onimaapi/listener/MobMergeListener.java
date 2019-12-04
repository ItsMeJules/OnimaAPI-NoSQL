package net.onima.onimaapi.listener;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.MobStack;

public class MobMergeListener implements Listener {
	
	@EventHandler
	public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
            return;
        }
        
		LivingEntity entity = event.getEntity();
		MobStack stack = MobStack.getByNearby(entity);

        if (MobStack.isValid(entity.getType())) {
        	if (stack == null || stack.getCount() >= ConfigurationService.ENTITY_MAX_STACK)
        		new MobStack(entity);
        	else {
        		stack.setCount(stack.getCount() + 1);
            	event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        MobStack stack = MobStack.getByEntity(entity);

        if (stack != null)
        	stack.setCount(stack.getCount() - 1);
    }

    @EventHandler
    public void onChunkUnloadEvent(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();
        for (Entity entity : chunk.getEntities()) {
            if (entity instanceof LivingEntity  && MobStack.isValid(entity.getType())) {
            	
                MobStack stack = MobStack.getByEntity((LivingEntity) entity);
                if (stack != null) {

                    for (int i = 0; i < stack.getCount(); i++)
                        stack.getEntity().getWorld().spawnEntity(stack.getEntity().getLocation(), stack.getEntity().getType());

                    stack.getEntity().remove();
                    MobStack.getStacks().remove(stack);
                }
            }
        }
    }

    @EventHandler
    public void onChunkLoadEvent(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        for (Entity entity : chunk.getEntities()) {
            if (entity instanceof LivingEntity && !(entity instanceof Player) && MobStack.isValid(entity.getType())) {
                MobStack stack = MobStack.getByNearby((LivingEntity) entity);

                if (stack != null && stack.getCount() < ConfigurationService.ENTITY_MAX_STACK) {
                    entity.remove();
                    stack.setCount(stack.getCount() + 1);
                } else
                    new MobStack((LivingEntity) entity);
            }
        }
    }
	
}
