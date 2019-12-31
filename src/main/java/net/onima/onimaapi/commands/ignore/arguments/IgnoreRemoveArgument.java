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

public class IgnoreRemoveArgument extends BasicCommandArgument {

	public IgnoreRemoveArgument() {
		super("remove", OnimaPerm.ONIMAAPI_IGNORE_REMOVE_ARGUMENT, new String[] {"delete"});
		
		usage = new JSONMessage("§7/ignore " + name + " <player> (-n)", "§d§oSupprime un joueur à la liste des joueurs ignorés.\n"
				+ "§d§oL'argument -n permet de notifier le joueur concerné.");
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
			if (apiPlayer.getIgnored().remove(uuid)) {
				sender.sendMessage("§7Vous n'ignorez plus §d" + Methods.getNameFromArg(offline, args[1]) + "§7.");
				
				if (offline.isOnline() && args.length > 2 && args[3].equalsIgnoreCase("-n"))
					((APIPlayer) offline).sendMessage("§d" + Methods.getRealName(sender) + " §7ne vous ignore plus.");
			} else
				sender.sendMessage("§cVous n'ignorez pas " + Methods.getNameFromArg(offline, args[1]) + '.');
		});
				
		return true;

	}

}
