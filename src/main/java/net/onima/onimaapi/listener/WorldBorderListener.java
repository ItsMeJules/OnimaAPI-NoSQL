package net.onima.onimaapi.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.WorldBorder;
import net.onima.onimaapi.utils.time.Time;

public class WorldBorderListener implements Listener {
	
	private Map<UUID, Long> messagesInterval;
	private final long INTERVAL = 10 * Time.SECOND;
	
	{
		messagesInterval = new HashMap<>();
	}

	public void handleMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		APIPlayer apiPlayer = APIPlayer.getPlayer(player);
		Location to = event.getTo();
		
		if (apiPlayer.hasMovedOneBlockTo(to)) return;
		
		if (WorldBorder.border(to) && !OnimaPerm.WORLD_BORDER_BYPASS.has(player)) {
			checkAndSendMessage(player, "§cVous ne pouvez pas aller au-delà de la bordure du monde !");
			
			Entity vehicle = player.getVehicle();
			
			event.setTo(event.getFrom());

			if (vehicle != null) {
				vehicle.eject();
				vehicle.teleport(event.getFrom());
				vehicle.setPassenger(player);
			}
		}
	}
	
	@EventHandler
	public void onEdgeReached(PlayerMoveEvent event) {
		handleMove(event);
	}	
	
	@EventHandler
	public void onEdgeReachedTeleport(PlayerTeleportEvent event) {
		handleMove(event);
	}	
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBucketEmpty(PlayerBucketFillEvent event) {
		Player player = event.getPlayer();
		
		if (WorldBorder.border(event.getBlockClicked().getLocation()) && !OnimaPerm.WORLD_BORDER_BYPASS.has(player)) {
			event.setCancelled(true);
			player.sendMessage("§cVous ne pouvez pas remplir de saut au-delà de la bordure !");
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		
		if (WorldBorder.border(event.getBlock().getLocation()) && !OnimaPerm.WORLD_BORDER_BYPASS.has(player)) {
			event.setCancelled(true);
			player.sendMessage("§cVous ne pouvez pas poser des blocks au-delà de la bordure !");
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		
		if (WorldBorder.border(event.getBlock().getLocation()) && !OnimaPerm.WORLD_BORDER_BYPASS.has(player)) {
			event.setCancelled(true);
			player.sendMessage("§cVous ne pouvez pas casser des blocks au-delà de la bordure !");
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBucketEmpty(PlayerBucketEmptyEvent event) {
		Player player = event.getPlayer();
		
		if (WorldBorder.border(event.getBlockClicked().getLocation()) && !player.hasPermission(OnimaPerm.WORLD_BORDER_BYPASS.getPermission())) {
			event.setCancelled(true);
			player.sendMessage("§cVous ne pouvez pas vider des sauts au-delà de la bordure !");
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onCreaturePreSpawn(CreatureSpawnEvent event) {
		if (WorldBorder.border(event.getLocation())) event.setCancelled(true);
	}
	
	private void checkAndSendMessage(Player player, String message) {
		Long last = messagesInterval.get(player.getUniqueId());
		long millis = System.currentTimeMillis();
		
		if (last != null && (last + INTERVAL) - millis > 0L)
			return;

		messagesInterval.put(player.getUniqueId(), millis);
		player.sendMessage(message);
	}

}
