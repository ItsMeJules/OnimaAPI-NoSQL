package net.onima.onimaapi.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import net.onima.onimaapi.event.region.PlayerRegionChangeEvent;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.utils.PlayerOption;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.zone.type.Region;

public class GlobalOptionsListener implements Listener {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event) {
		Material type = event.getBlock().getType();
		
		if (!APIPlayer.getPlayer(event.getPlayer()).getOptions().getBoolean(PlayerOption.GlobalOptions.COBBLE_DROP) && (type == Material.STONE || type == Material.COBBLESTONE)) {
			event.setCancelled(true);
			event.getBlock().setType(Material.AIR);
		}
	}
	
	@EventHandler
	public void onRegionEnter(PlayerRegionChangeEvent event) {
		APIPlayer apiPlayer = event.getAPIPlayer();
		
		if (!apiPlayer.getOptions().getBoolean(PlayerOption.GlobalOptions.SHOW_PLAYERS_WHEN_IN_SPAWN)) {
			Region to = event.getNewRegion(), from = event.getRegion();
			Player player = apiPlayer.toPlayer();
			
			boolean fromSpawn = from.getDisplayName(player).equalsIgnoreCase(ConfigurationService.SAFEZONE_NAME);
			boolean toSpawn = to.getDisplayName(player).equalsIgnoreCase(ConfigurationService.SAFEZONE_NAME);
			
			if (toSpawn && !fromSpawn) { //TODO est-ce que je dois cacher les membres du staff ?
				for (Player inside : to.toCuboid().getPlayers()) {
					if (player.canSee(inside))
						player.hidePlayer(inside);
					
					if (inside.canSee(player))
						inside.hidePlayer(player);
				}
			} else if (fromSpawn && !toSpawn) {
				for (Player inside : from.toCuboid().getPlayers()) {
					if (!player.canSee(inside) && !APIPlayer.getPlayer(inside).isVanished())
						player.showPlayer(inside);
					
					if (!inside.canSee(player) && !APIPlayer.getPlayer(player).isVanished())
						inside.showPlayer(player);
				}
			}
		}
	}
	
}
