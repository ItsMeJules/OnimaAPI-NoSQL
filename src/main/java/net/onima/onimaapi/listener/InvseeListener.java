package net.onima.onimaapi.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.onima.onimaapi.players.utils.SpecialPlayerInventory;
import net.onima.onimaapi.rank.OnimaPerm;

public class InvseeListener implements Listener {
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getClickedInventory() instanceof SpecialPlayerInventory) {
			if (!OnimaPerm.ONIMAAPI_COMMAND_INVSEE_EDIT.has((Player) event.getWhoClicked()))
				event.setCancelled(true);
		}
	}

}
