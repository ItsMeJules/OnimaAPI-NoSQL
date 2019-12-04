package net.onima.onimaapi.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BookDisenchantListener implements Listener {
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBookDesenchant(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if (event.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE && player.getItemInHand().getType() == Material.ENCHANTED_BOOK && player.isSneaking()) {
			player.setItemInHand(new ItemStack(Material.BOOK));
			event.setCancelled(true);
			player.sendMessage("§aLivre désanchanté avec succès !");
		}
	}

}
