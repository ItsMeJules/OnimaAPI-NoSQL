package net.onima.onimaapi.commands.ignore.arguments;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class IgnoreAddArgument extends BasicCommandArgument {

	public IgnoreAddArgument() {
		super("add", OnimaPerm.ONIMAAPI_IGNORE_ADD_ARGUMENT);
		
		usage = new JSONMessage("§7/ignore " + name + " <player> (-n)", "§d§oAjoute un joueur à la liste des joueurs ignorés.\n"
				+ "§d§oL'argument -n permet de notifier le joueur que vous ignorez.");
		playerOnly = true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 2, true))
			return false;
		
		UUID uuid = UUIDCache.getUUID(args[1]);
		
		if (uuid == null) {
			sender.sendMessage("§cLe joueur " + args[1] + " ne s'est jamais connecté sur le serveur !");
			return false;
		}
		
		APIPlayer apiPlayer = APIPlayer.getPlayer((Player) sender);
		
		OfflineAPIPlayer.getPlayer(uuid, offline -> {
			if (offline.getRank().getRankType().hasPermisssion(OnimaPerm.ONIMAAPI_IGNORE_EXEMPT)) {
				sender.sendMessage("§cVous ne pouvez pas ignorer " + Methods.getNameFromArg(offline, args[1]) + '.');
				return;
			}
			
			if (apiPlayer.getIgnored().add(uuid)) {
				sender.sendMessage("§7Vous ignorez maintenant §d" + Methods.getNameFromArg(offline, args[1]) + "§7.");
				
				if (offline.isOnline() && args.length > 2 && args[3].equalsIgnoreCase("-n"))
					((APIPlayer) offline).sendMessage("§d" + Methods.getRealName(sender) + " §7a décidé de vous ignorer.");
			} else
				sender.sendMessage("§cVous ignorez déjà " + Methods.getNameFromArg(offline, args[1]) + '.');
		});
				
		return true;
	}

}
