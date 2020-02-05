package net.onima.onimaapi.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;

public class SettingsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.SETTINGS_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}
		
		APIPlayer target = null;
		
		if (args.length > 0) {
			target = APIPlayer.getPlayer(UUIDCache.getUUID(args[0]));
			
			if (target == null) {
				sender.sendMessage(args[0] + " n'est pas en ligne !");
				return false;
			}
		} else
			target = APIPlayer.getPlayer((Player) sender);
		
		APIPlayer player = APIPlayer.getPlayer((Player) sender);

		player.openMenu(target.getMenu("global_options"));
		return true;
	}

}
