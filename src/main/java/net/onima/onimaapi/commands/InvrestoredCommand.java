package net.onima.onimaapi.commands;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.Methods;

public class InvrestoredCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.INVRESTORED_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande.");
			return false;
		}
			
		APIPlayer apiPlayer = APIPlayer.getPlayer((Player) sender);
		
		if (args.length < 1) {
			apiPlayer.openMenu(apiPlayer.getMenu("pending_restores"));
			sender.sendMessage("§dVous §7avez ouvert vos inventaires en attente.");
			return true;
		} else if (args.length >= 1 && OnimaPerm.INVRESTORED_COMMAND_OTHER.has(sender)) {
			UUID uuid = UUIDCache.getUUID(args[0]);
			
			if (uuid == null) {
				sender.sendMessage("§c" + args[0] + " ne s'est jamais connecté sur le serveur !");
				return false;
			}
			
			OfflineAPIPlayer.getPlayer(uuid, offline -> {
				apiPlayer.getMenu("pending_restores").open(apiPlayer);
				sender.sendMessage("§dVous §7avez ouvert les inventaires en attente de §9" + Methods.getName(offline, true));
			});
			
			return true;
		} else {
			apiPlayer.openMenu(apiPlayer.getMenu("pending_restores"));
			sender.sendMessage("§dVous §7avez ouvert vos inventaires en attente.");
			return true;
		}
	}

}
