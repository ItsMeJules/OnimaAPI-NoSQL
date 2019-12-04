	package net.onima.onimaapi.listener;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.mountain.TreasureMountain;
import net.onima.onimaapi.mountain.TreasureMountain.TreasureBlock;
import net.onima.onimaapi.mountain.utils.Mountain;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.OSound;

public class MountainListener implements Listener {
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		final Mountain mountain = Mountain.getByLocation(block.getLocation());
		
		if (mountain != null && mountain.canBuild(block))
			Bukkit.getScheduler().runTaskAsynchronously(OnimaAPI.getInstance(), () -> mountain.breakAnnouncement("§6La montagne §e" + mountain.getName() + " §6est looté à §e%percent%%§6."));
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		APIPlayer apiPlayer = APIPlayer.getPlayer(player);
		TreasureBlock treasureBlock = null;
		
		if ((treasureBlock = apiPlayer.getTreasureBlock()) != null && Methods.isEmpty(event.getInventory())) {
			Location location = treasureBlock.getLocation();
			Block block = location.getBlock();
			boolean announcement = true;
			
			player.playEffect(location, Effect.STEP_SOUND, block.getType().getId());
			new OSound(block.getType() == Material.CHEST ? Sound.STEP_WOOD : Sound.STEP_STONE, 1.0F, 1.0F).play(location);
			block.setType(Material.AIR);
			
			if (treasureBlock.isDoubleChest()) {
				Location location2 = treasureBlock.getLocation2();
				Block block2 = location2.getBlock();
				announcement = false;
				
				player.playEffect(location2, Effect.STEP_SOUND, block2.getType().getId());
				new OSound(block2.getType() == Material.CHEST ? Sound.STEP_WOOD : Sound.STEP_STONE, 1.0F, 1.0F).play(location2);
				block2.setType(Material.AIR);
				treasureBlock.getMountain().breakAnnouncement("§6La montagne §e" + treasureBlock.getMountain().getName() + " §6est looté à §e%percent%%§6.");
			}
			
			if (announcement)
				treasureBlock.getMountain().breakAnnouncement("§6La montagne §e" + treasureBlock.getMountain().getName() + " §6est looté à §e%percent%%§6.");
		}
	}
	
	@EventHandler
	public void onInventoryOpenLocation(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		
		Block block = event.getClickedBlock();
		Mountain mountain = null;
		
		if ((mountain = Mountain.getByLocation(block.getLocation())) != null && mountain instanceof TreasureMountain && block.getState() instanceof InventoryHolder)
			APIPlayer.getPlayer(event.getPlayer()).setTreasureBlock(((TreasureMountain) mountain).getTreasureBlock(block.getLocation()));
	}

}
