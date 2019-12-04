package net.onima.onimaapi.listener;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;

import net.onima.onimaapi.utils.Methods;

public class HorseProtectListener implements Listener {
	
	@EventHandler
	public void onVehicleEnter(VehicleEnterEvent event) {
		Entity entered = event.getEntered();
		
		if (entered instanceof Player) {
			Vehicle vehicle = event.getVehicle();
			
			if (vehicle instanceof Horse) {
				AnimalTamer owner = ((Horse) event.getVehicle()).getOwner();
				
				if (owner != null && !owner.getUniqueId().equals(entered.getUniqueId())) {
					event.setCancelled(true);
					((Player) entered).sendMessage("§cCe cheval appartient à" + Methods.getRealName((OfflinePlayer) owner) + ", vous ne pouvez pas le monter !");
				}
			}
		}
	}

}
