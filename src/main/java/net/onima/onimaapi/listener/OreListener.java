package net.onima.onimaapi.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

import net.md_5.bungee.api.chat.ClickEvent;
import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.gui.menu.OresMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.utils.MinedOres;
import net.onima.onimaapi.players.utils.PlayerOption;
import net.onima.onimaapi.utils.JSONMessage;

public class OreListener implements Listener {
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onBlockMined(BlockBreakEvent event) {
		int ores = 0;
		Block block = event.getBlock();
		String name = APIPlayer.getPlayer(event.getPlayer()).getName();
		
		if (block.getType() == Material.DIAMOND_ORE) {
			for (int x = -2; x < 2; x++) {
				for (int y = -2; y < 2; y++) {
					for (int z = -2; z < 2; z++) {
						Block nextBlock = block.getLocation().clone().add(x, y, z).getBlock();

						if (nextBlock.getType() != Material.DIAMOND_ORE) continue;
						if (nextBlock.hasMetadata("Placed") || nextBlock.hasMetadata("Counted")) continue;
						
						ores++;
						nextBlock.setMetadata("Counted", new FixedMetadataValue(OnimaAPI.getInstance(), "pd"));
					}
				}
			}
		}
		
		if (ores != 0) {
			String msg = "§f[FD] §b" + name + " a trouvé " + ores + " diamant" + (ores > 1 ? "s" : "") + '.';
			
			OnimaAPI.sendConsoleMessage(msg, null);
			for (APIPlayer player : APIPlayer.getOnlineAPIPlayers()) {
				if (player.getOptions().getBoolean(PlayerOption.GlobalOptions.FOUND_DIAMONDS))
					player.sendMessage(new JSONMessage(msg, "§aCliquez ici pour désactiver les notifications.", true, "/settings found_ore off", ClickEvent.Action.RUN_COMMAND));
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onOreBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		
		if (block.hasMetadata("Placed") || !block.getType().toString().endsWith("ORE")) return;
		
		Material material = block.getType();
		MinedOres minedOres = APIPlayer.getPlayer(event.getPlayer()).getMinedOres();
		OresMenu menu = minedOres.getMenu();
		
		minedOres.updateOres(material);
		
		if (!menu.getViewers().isEmpty())
			menu.getItems().get(material).update(menu);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onOrePlace(BlockPlaceEvent event) {
		Block block = event.getBlock();
		
		if (block.hasMetadata("Placed")) return;
		else block.setMetadata("Placed", new FixedMetadataValue(OnimaAPI.getInstance(), "pd"));
	}

}
