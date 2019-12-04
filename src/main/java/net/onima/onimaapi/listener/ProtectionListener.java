package net.onima.onimaapi.listener;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Recipe;
import org.bukkit.material.EnderChest;

import com.google.common.collect.Lists;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;

public class ProtectionListener implements Listener {
	
	private List<String> blockedCommands;
	
	{
		blockedCommands = Lists.newArrayList("/bukkit:", "/minecraft:");
	}
	
	public ProtectionListener() {
		Iterator<Recipe> iterator = Bukkit.recipeIterator();
		
		while (iterator.hasNext()) {
			if (iterator.next().getResult().getType() == Material.ENDER_CHEST)
				iterator.remove();
		}
		
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		APIPlayer apiPlayer = APIPlayer.getPlayer(event.getPlayer());
		
		if (block.getLocation().getWorld().getEnvironment() == World.Environment.NETHER && block.getState() instanceof CreatureSpawner && !OnimaPerm.PROTECTION_BYPASS.has(apiPlayer.toPlayer())) {
			apiPlayer.sendMessage("§cVous ne pouvez pas détruire de spawner dans le nether !");
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBlockPlace(BlockPlaceEvent event) {
		Block block = event.getBlock();
		APIPlayer apiPlayer = APIPlayer.getPlayer(event.getPlayer());
		
		if (block.getLocation().getWorld().getEnvironment() == World.Environment.NETHER && block.getState() instanceof CreatureSpawner && !OnimaPerm.PROTECTION_BYPASS.has(apiPlayer.toPlayer())) {
			apiPlayer.sendMessage("§cVous ne pouvez pas placer de spawner dans le nether !");
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onEnderChestOpen(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.ENDER_CHEST && !OnimaPerm.PROTECTION_BYPASS.has(event.getPlayer()))
			event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onInventoryOpen(InventoryOpenEvent event) {
		if (event.getInventory() instanceof EnderChest && !OnimaPerm.PROTECTION_BYPASS.has((Player) event.getPlayer()))
			event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommandProcess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		
		if (OnimaPerm.PROTECTION_BYPASS.has(player))
			return;
		
		String lowercase = event.getMessage().toLowerCase();
		
		for (String blocked : blockedCommands) {
			if (!blocked.startsWith(lowercase))
				continue;
			
			player.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			event.setCancelled(true);
			return;
		}
	}
	
}
