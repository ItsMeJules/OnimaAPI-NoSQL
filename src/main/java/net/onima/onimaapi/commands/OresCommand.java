package net.onima.onimaapi.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;

public class OresCommand implements CommandExecutor {
	
	private JSONMessage usage = new JSONMessage("§7/ores <player>", "§d§oAffiche les minerais minés par un joueur.");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_COMMAND_ORES.has(sender)) {
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
		
		OfflineAPIPlayer.getPlayer(args[0], offline -> {
			if (offline == null) {
				sender.sendMessage("§cLe joueur " + args[0] + " ne s'est jamais connecté sur le serveur !");
				return;
			}
			
			offline.getMinedOres().getMenu().open(APIPlayer.getPlayer((Player) sender));
		});
		
		return true;
	}

}
