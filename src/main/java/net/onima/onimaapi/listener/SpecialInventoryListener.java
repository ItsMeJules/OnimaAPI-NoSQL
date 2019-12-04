package net.onima.onimaapi.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import net.onima.onimaapi.players.utils.SpecialPlayerInventory;
import net.onima.onimaapi.rank.OnimaPerm;

public class SpecialInventoryListener implements Listener {
	
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        
        if (event.getInventory() instanceof SpecialPlayerInventory && !OnimaPerm.ONIMAAPI_COMMAND_INVSEE_EDIT.has(player)) {
            event.setCancelled(true);
            player.sendMessage("§cVous n'avez pas la permission d'éditer un inventaire !");
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
    	Player player = (Player) event.getWhoClicked();
          
    	if (event.getInventory() instanceof SpecialPlayerInventory && !OnimaPerm.ONIMAAPI_COMMAND_INVSEE_EDIT.has(player)) {
    		event.setCancelled(true);
    		player.sendMessage("§cVous n'avez pas la permission d'éditer un inventaire !");
    	}
    }

}
