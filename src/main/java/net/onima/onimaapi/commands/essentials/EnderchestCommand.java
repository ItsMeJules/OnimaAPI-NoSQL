package net.onima.onimaapi.commands.essentials;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.rank.OnimaPerm;

public class EnderchestCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ENDERCHEST_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cCette commande est seulement utilisable par des joueurs !");
			return false;
		}
		
		Player player = (Player) sender;
		Player target = null;
		
		if (args.length == 0)
			target = (Player) sender;
		else if (args.length > 0) {
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
		
		if (player.getUniqueId().equals(target.getUniqueId()))
			sender.sendMessage("§7Vous avez ouvert votre §eenderchest.");
		else
			sender.sendMessage("§7Vous avez ouvert l'enderchest de §e" + target.getName() + "§7.");
		
		player.openInventory(target.getEnderChest());
		return true;
	}

}
