package net.onima.onimaapi.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.md_5.bungee.api.chat.ClickEvent;
import net.onima.onimaapi.event.menu.PacketMenuCloseEvent;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;

public class FreezeListener implements Listener {
	
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent event) {
		Player player = Methods.getLastAttacker(event);
		
		if (player != null && APIPlayer.getPlayer(player.getUniqueId()).isFrozen())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player && APIPlayer.getPlayer(event.getEntity().getUniqueId()).isFrozen())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (APIPlayer.getPlayer(event.getWhoClicked().getUniqueId()).isFrozen())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (APIPlayer.getPlayer(event.getPlayer().getUniqueId()).isFrozen())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		if (APIPlayer.getPlayer(event.getPlayer().getUniqueId()).isFrozen())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		if (APIPlayer.getPlayer(event.getPlayer().getUniqueId()).isFrozen())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onFreezeMenuClose(PacketMenuCloseEvent event) {
		if (event.getApiPlayer().isFrozen())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (APIPlayer.getPlayer(event.getPlayer().getUniqueId()).isFrozen()) {
			APIPlayer apiPlayer = APIPlayer.getPlayer(event.getPlayer());
			JSONMessage message = new JSONMessage("§e" + apiPlayer.getDisplayName(true) + " §7s'est déconnecté en étant freeze, passez votre souris sur ce message.", "§7Cliquez ici pour §cbannir §e" + apiPlayer.getDisplayName(true) + '.', true, "/ban " + apiPlayer.getName(), ClickEvent.Action.RUN_COMMAND);
			
			for (APIPlayer connected : APIPlayer.getOnlineAPIPlayers()) {
				if (OnimaPerm.FREEZE_COMMAND.has(connected.toPlayer()))
					apiPlayer.sendMessage(message);
			}
		}
	}

}
