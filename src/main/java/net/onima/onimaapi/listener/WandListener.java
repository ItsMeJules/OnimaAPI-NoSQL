package net.onima.onimaapi.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.items.Wand;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;

public class WandListener implements Listener {
	
	@EventHandler(ignoreCancelled = false, priority = EventPriority.HIGH)
	public void onWandSelect(PlayerInteractEvent event) {
		Player player = event.getPlayer();	
		
		if (!OnimaPerm.ONIMAAPI_WAND.has(player)) return;
		
		Action action = event.getAction();
		ItemStack hand = event.getItem();
		
		if (Wand.isZoneWand(hand)) {
			APIPlayer apiPlayer = APIPlayer.getPlayer(player);
			Wand wand = apiPlayer.getWand();
			
			if (player.isSneaking() && action.toString().endsWith("AIR")) {
				wand.setLocation1(null);
				wand.setLocation2(null);
				player.sendMessage("§d§lLocations réinitialisées !");
				return;
			}
			
			if (!action.toString().endsWith("BLOCK")) return;
			
			Location location = event.getClickedBlock().getLocation();
			
			event.setCancelled(true);
			
			if (action == Action.LEFT_CLICK_BLOCK) {
				
				if (wand.hasAllLocationsSet()) wand.setLocation1(null);
				if (!Wand.validWorlds(player, location, wand.getLocation2())) return;
				
				wand.setLocation1(location);
				ConfigurationService.WAND_SELECT1_SOUND.play(apiPlayer);
				player.sendMessage("§d§oPremière location positionnée en §d§o" + location.getBlockX() + ' ' + location.getBlockY() + ' ' + location.getBlockZ());
			} else {
				if (wand.hasAllLocationsSet()) wand.setLocation2(null);
				if (!Wand.validWorlds(player, location, wand.getLocation1())) return;
				
				wand.setLocation2(location);
				ConfigurationService.WAND_SELECT2_SOUND.play(apiPlayer);
				player.sendMessage("§d§oSeconde location positionnée en §d§o" + location.getBlockX() + ' ' + location.getBlockY() + ' ' + location.getBlockZ());
			}
		}
	}
	
}
