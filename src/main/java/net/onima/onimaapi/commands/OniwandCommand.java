package net.onima.onimaapi.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;

public class OniwandCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_WAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}
		
		Player player = (Player) sender;
		APIPlayer apiPlayer = APIPlayer.getPlayer(player);
		
		if (apiPlayer.getWand().give(player)) {
			player.sendMessage("§d§oVous §7avez reçu une wand.");
			return true;
		} else {
			player.sendMessage("§cVotre inventaire est plein, videz un slot et réessayez.");
			return false;
		}
	}

}
