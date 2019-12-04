package net.onima.onimaapi.listener;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.onima.onimaapi.crates.PhysicalCrate;
import net.onima.onimaapi.crates.RoomCrate;
import net.onima.onimaapi.crates.VirtualCrate;
import net.onima.onimaapi.crates.openers.PhysicalKey;
import net.onima.onimaapi.crates.prizes.Prize;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.event.crates.CrateWinEvent;
import net.onima.onimaapi.event.menu.PacketMenuCloseEvent;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.menu.CrateOpeningMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.OSound;

public class CrateListener implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onCrateClick(PlayerInteractEvent event) {
		Action action = event.getAction();
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		
		if (block == null)
			return;
		
		APIPlayer apiPlayer = APIPlayer.getPlayer(player);
		PhysicalCrate crate = PhysicalCrate.getByLocation(block.getLocation());
		
		if (crate != null) {
			event.setCancelled(true);
			
			if (crate instanceof RoomCrate && RoomCrate.isBusy()) {
				player.sendMessage("§cQuelqu'un est déjà entrain d'utiliser la salle pour la crate. Veuillez attendre votre tour.");
				return;
			}
			
			if (action == Action.RIGHT_CLICK_BLOCK) {
				
				if (crate.getPrizeAmount() <= 0) {
					player.sendMessage("§cCette crate n'a pas de nombre de prix définit, veuillez contacter un administrateur.");
					return;
				}
				
				PhysicalKey key = PhysicalKey.fromItem(player.getItemInHand());
					
				if (key != null && key.match(crate)) {
					Methods.removeOneItem(player);
					crate.open(apiPlayer, key.getBooster());
				} else {
					new OSound(Sound.VILLAGER_NO, 1F, 1F).play(player);
				
					if (player.isOnGround()) 
						Methods.boost(player, -0.5);
				}

			} else if (action == Action.LEFT_CLICK_BLOCK)
				crate.preview(apiPlayer);
		}
	}
	
	@EventHandler
	public void onRoomChestClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		
		if (block == null)
			return;
		
		Crate currentCrate = APIPlayer.getPlayer(player).getCrateOpening();
		
		if (currentCrate instanceof RoomCrate && ((RoomCrate) currentCrate).loot(player, block.getLocation()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onWinningMenuClose(PacketMenuCloseEvent event) {
		PacketMenu menu = event.getPacketMenu();
		
		if (menu instanceof CrateOpeningMenu) {
			CrateOpeningMenu openingMenu = (CrateOpeningMenu) menu;
		
			if (openingMenu.arePrizesDropped())
				return;
		
			APIPlayer apiPlayer = event.getApiPlayer();
			Player player = apiPlayer.toPlayer();
		
			openingMenu.closeCrate(apiPlayer);
		
			for (Prize prize : openingMenu.getPrizes())
				prize.give(player);
		}
	}
	
	@EventHandler
	public void onCrateWin(CrateWinEvent event) {
		Crate crate = event.getCrate();
		
		if (!(crate instanceof VirtualCrate)) {
			Location location = ((PhysicalCrate) crate).getBlock().getLocation().add(0.5, 1, 0.5);
			
			for (Prize prize : event.getPrizes()) {
				if (prize.getEffect() != null)
					prize.firework(location);
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		APIPlayer apiPlayer = APIPlayer.getPlayer(event.getPlayer());
		Crate crate = apiPlayer.getCrateOpening();
		
		if (crate != null)
			crate.cancel(apiPlayer);
	}
	
}
