package net.onima.onimaapi.listener.fixes;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.github.paperspigot.event.block.BeaconEffectEvent;

import net.onima.onimaapi.rank.OnimaPerm;

public class FixesListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPotionEffectAdd(BeaconEffectEvent event) {
        Player entity = event.getPlayer();
        PotionEffect effect = event.getEffect();
        
        if (effect.getAmplifier() >= 1 && effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
            entity.addPotionEffect(new PotionEffect(effect.getType(), effect.getDuration(), 0, effect.isAmbient()), true);
            event.setCancelled(true);
        }
        
        if (effect.getType() == PotionEffectType.SPEED && entity.hasPotionEffect(PotionEffectType.SPEED))
            event.setCancelled(true);
    }
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onProjectileHit(ProjectileHitEvent event) {
		Entity entity = event.getEntity();
		
		if (entity instanceof Arrow) {
			Arrow arrow = (Arrow) entity;

			if (!(arrow.getShooter() instanceof Player) || ((CraftArrow) arrow).getHandle().fromPlayer == 2)
				arrow.remove();
		}
	}
	
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onVehicleCreate(VehicleCreateEvent event) {
        Vehicle vehicle = event.getVehicle();
    
        if (vehicle instanceof Boat) {
            Boat boat = (Boat) vehicle;
            Material belowType = boat.getLocation().add(0.0D, -1.0D, 0.0D).getBlock().getType();
        
            if (belowType != Material.WATER && belowType != Material.STATIONARY_WATER)
                boat.remove();
        }
    }
    
	@EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockPlaceEvent event) {
		if (event.isCancelled()) {
			Player player = event.getPlayer();

			if (player.getGameMode() == GameMode.CREATIVE || player.getAllowFlight() || OnimaPerm.PROTECTION_BYPASS.has(player))
				return;

			Block block = event.getBlockPlaced();
			
			if (block.getType().isSolid() && !(block.getState() instanceof Sign)) {
				int playerY = player.getLocation().getBlockY();
				int blockY = block.getLocation().getBlockY();
				
				if (playerY > blockY) {
					Vector vector = player.getVelocity();
					
					vector.setX(-0.1);
					vector.setZ(-0.1);
					
					player.setVelocity(vector.setY(vector.getY() - 0.41999998688697815D)); // Magic number acquired from EntityLiving#bj()
				}
			}
		}
	}
}
