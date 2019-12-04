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
import net.onima.onimaapi.utils.Methods;

public class RestoreCommand implements CommandExecutor {

	private JSONMessage usage = new JSONMessage("§7/restore <player>", "§d§oAffiche les morts d'un joueur.");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.RESTORE_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande.");
			return false;
		}
		
		if (args.length < 1) {
			usage.send(sender, "§7Utilisation : ");
			return false;
		}
		
		OfflineAPIPlayer.getPlayer(args[0], offline -> {
			if (offline == null) {
				sender.sendMessage("§c" + args[0] + " ne s'est jamais connecté sur le serveur !");
				return;
			}
			
			offline.getMenu("death_historic").open(APIPlayer.getPlayer((Player) sender));
			sender.sendMessage("§dVous §7avez ouvert l'historique des morts de §d" + Methods.getName(offline, true) + "§7.");
		});
		
		return true;
	}

}
