package net.onima.onimaapi.listener;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.signs.ElevatorSign;
import net.onima.onimaapi.signs.HCFSign;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;

public class ElevatorSignListener implements Listener { 
	
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		String[] lines = event.getLines();
		
		if (lines[0].equalsIgnoreCase(ChatColor.stripColor(ConfigurationService.ELEVATOR_SIGN_LINES.get(0)))) {
			ElevatorSign sign = null;
			
			if (lines[1].equalsIgnoreCase(ChatColor.stripColor(ConfigurationService.ELEVATOR_SIGN_UP))) 
				sign = new ElevatorSign((Sign) event.getBlock().getState(), true);
			else if (lines[1].equalsIgnoreCase(ChatColor.stripColor(ConfigurationService.ELEVATOR_SIGN_DOWN)))
				sign = new ElevatorSign((Sign) event.getBlock().getState(), false);
			
			if (sign != null) {
				sign.initLocationToTeleport();
				List<String> newLines = Methods.replacePlaceholder(ConfigurationService.ELEVATOR_SIGN_LINES, "%up%", sign.isDown() ? ConfigurationService.ELEVATOR_SIGN_DOWN : ConfigurationService.ELEVATOR_SIGN_UP, "%usage%", sign.isReadyToUse() ? ConfigurationService.ELEVATOR_SIGN_CAN_USE : ConfigurationService.ELEVATOR_SIGN_CANT_USE);
				
				for (int i = 0; i <  newLines.size(); i++)
					event.setLine(i, newLines.get(i));
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		
		if (block.getState() instanceof Sign) {
			Sign sign = (Sign) block.getState();
			HCFSign hcfSign = HCFSign.getSign(sign);
			
			if (hcfSign instanceof ElevatorSign) {
				ElevatorSign twin = ((ElevatorSign) hcfSign).getTwin();
				
				if (twin != null) {
					List<String> lines = Methods.replacePlaceholder(ConfigurationService.ELEVATOR_SIGN_LINES, "%up%", twin.isDown() ? ConfigurationService.ELEVATOR_SIGN_DOWN : ConfigurationService.ELEVATOR_SIGN_UP, "%usage%", twin.isReadyToUse() ? ConfigurationService.ELEVATOR_SIGN_CAN_USE : ConfigurationService.ELEVATOR_SIGN_CANT_USE);
										
					twin.setReadyToUse(false);
					
					for (int i = 0; i < lines.size(); i++)
						twin.getSign().setLine(i, lines.get(i));
					
					twin.getSign().update();
				}
				
				HCFSign.removeSign(sign);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onElevatorUse(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block clicked = event.getClickedBlock();
		
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		
		if (clicked.getType().toString().contains("SIGN")) {
			APIPlayer apiPlayer = APIPlayer.getPlayer(player);
			HCFSign hcfSign = HCFSign.getSign((Sign) clicked.getState());
			
			if (!(hcfSign instanceof ElevatorSign)) return;
			
			ElevatorSign sign = (ElevatorSign) hcfSign;
			Location loc = ((ElevatorSign) hcfSign).getTeleportLocation();

			if (loc == null || loc != null && !sign.teleportLocaionValid(loc)) {
				apiPlayer.sendMessage("§cImpossible de téléporter à une location correcte, il faut au moins 1 block solide sur le chemin et 2 blocks d'air superposé pour pouvoir être téléporté.");
				return;
			}
			
			Location playerLoc = player.getLocation();
			
			loc.setPitch(playerLoc.getPitch());
			loc.setYaw(playerLoc.getYaw());
			player.teleport(loc);
		}
	}

}
