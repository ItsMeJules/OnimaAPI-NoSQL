package net.onima.onimaapi.commands.essentials;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;

public class SudoCommand implements CommandExecutor {
	
	private JSONMessage usage = new JSONMessage("§7/sudo <player> <command args>", "§d§oForce un joueur a exécuter une commande.");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.SUDO_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (args.length < 2) {
			usage.send(sender, "§7Utilisation : ");
			return false;
		}
		
		String executingCommand = StringUtils.join(args, ' ', 1, args.length);
		UUID uuid = UUIDCache.getUUID(args[0]);
		
		if (uuid == null) {
			sender.sendMessage("Le joueur " + args[0] + " n'existe pas !");
			return false;
		}
		
		Player target = Bukkit.getPlayer(uuid);
		
		target.performCommand(executingCommand);
		sender.sendMessage("§6Vous §7avez forcé §6" + Methods.getName(target, true) + " §7à exécuter la commande §6" + executingCommand + "§7.");
		return true;
	}

}
