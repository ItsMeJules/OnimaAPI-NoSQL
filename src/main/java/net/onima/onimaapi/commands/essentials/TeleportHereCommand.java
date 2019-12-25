package net.onima.onimaapi.commands.essentials;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;

public class TeleportHereCommand implements CommandExecutor {
	
	private JSONMessage usage = new JSONMessage("§7/tph <player>", "§d§oTéléporte un joueur à votre location.");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_TPHERE_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}
        
        if (args.length < 1) {
        	usage.send(sender, "§7Utilisation : ");
        	return false;
        }
        
        return ((Player) sender).performCommand("tp " + args[0] + ' ' + sender.getName());
	}

}
