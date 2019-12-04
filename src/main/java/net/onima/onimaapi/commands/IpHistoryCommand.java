package net.onima.onimaapi.commands;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;

public class IpHistoryCommand implements CommandExecutor {

	private JSONMessage usage = new JSONMessage("§7/iphistory <player>", "§d§oAffiche l'historique des ips d'un joueur.");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_IPHISTORY_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}

		if (args.length < 1) {
			usage.send(sender, "§7Utilisation : ");
			return false;
		}
		
		UUID uuid = UUIDCache.getUUID(args[0]);
		
		if (uuid == null) {
			sender.sendMessage("§c" + args[0] + " ne s'est jamais connecté sur le serveur.");
			return false;
		}
		
		OfflineAPIPlayer.getPlayer(uuid, offline -> {
			sender.sendMessage(ConfigurationService.STAIGHT_LINE);
			
			for (String ip : offline.getIpHistory()) {
				if (ip.equalsIgnoreCase(offline.getIP()))
					sender.sendMessage("§a" + ip + " §7(ip actuelle)");
				else
					sender.sendMessage("§9" + ip);
			}
		});
		
		return true;
	}

}
