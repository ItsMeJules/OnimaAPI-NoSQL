package net.onima.onimaapi.commands.essentials;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.Methods;

public class ClearCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_CLEAR_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		Player target = null;
		
		if (args.length == 0) {
			if (!(sender instanceof Player))  {
				sender.sendMessage("§cLa console ne peut pas clear son inventaire.");
				return false;
			}
			
			target = (Player) sender;
		} else if (args.length > 0) {
			UUID uuid = UUIDCache.getUUID(args[0]);
			
			if (uuid == null) {
				sender.sendMessage("§cLe joueur " + args[0] + " ne s'est jamais connecté sur le serveur !");
				return false;
			}
			
			target = Bukkit.getPlayer(uuid);
		}
		
		if (target == null) {
			sender.sendMessage("§c" + args[0] + " n'est pas connecté !");
			return false;
		}
		
		Methods.clearInventory(target);
		
		if (sender.getName().equalsIgnoreCase(target.getName())) {
			sender.sendMessage("§eVous §7avez clear votre inventaire.");
			return true;
		} else {
			sender.sendMessage("§eVous §7avez clear l'inventaire de §e" +Methods.getRealName((OfflinePlayer) target));
			target.sendMessage("§e" + Methods.getRealName(sender) + " §7a clear votre inventaire !");
			return true;
		}
	}

}
