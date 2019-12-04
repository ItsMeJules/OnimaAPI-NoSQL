package net.onima.onimaapi.listener;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.signs.DeathSign;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;

public class DeathSignListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onSignChange(SignChangeEvent event) {
		if (DeathSign.isDeathSign(event.getBlock()))
			event.setCancelled(true);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player victim = event.getEntity();
		Player killer = victim.getKiller();
		
		if (killer != null && !killer.equals(victim))
			event.getDrops().add(new DeathSign(Methods.getRealName((OfflinePlayer) killer), Methods.getRealName((OfflinePlayer) victim)).getItem().toItemStack());
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		ItemStack hand = event.getItemInHand();
		BlockState state = event.getBlock().getState();
		
		if (state instanceof Sign && hand.getItemMeta().hasDisplayName() && hand.getItemMeta().getDisplayName().equalsIgnoreCase(ConfigurationService.DEATH_SIGN_NAME)) {
			Sign sign = (Sign) state;
			List<String> lores = hand.getItemMeta().getLore();
			
			for (int i = 0; i < lores.size(); i++)
				sign.setLine(i, lores.get(i));

			sign.update();
			sign.setEditable(false);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE && DeathSign.isDeathSign(block)) {
			event.setCancelled(true);
		    DeathSign sign = new DeathSign(((Sign) block.getState()).getLines());
		    sign.drop(block.getLocation());
		    block.setType(Material.AIR);
		}
	}
 	
}
