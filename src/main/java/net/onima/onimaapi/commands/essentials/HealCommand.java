package net.onima.onimaapi.commands.essentials;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.Methods;

public class HealCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.HEAL_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		Player target;
		
		if (args.length > 0) {
			UUID uuid = UUIDCache.getUUID(args[0]);
			
			if (uuid == null) {
				sender.sendMessage("§cLe joueur " + args[0] + " ne s'est jamais connecté sur le serveur !");
				return false;
			}
			
			target = Bukkit.getPlayer(uuid);
		} else {
			if (!(sender instanceof Player)) {
				sender.sendMessage("§cCette commande est seulement utilisable pour les joueurs !");
				return false;
			}
			
			target = (Player) sender;
		}

        if (target == null || (sender instanceof Player && !((Player) sender).canSee(target))) {
            sender.sendMessage("§c" + args[0] + " n'est pas connecté !");
            return false;
        }
        
        if (target.isDead()) {
            sender.sendMessage("§c" + Methods.getRealName((OfflinePlayer) target) + " est mort. Il ne peut pas être heal.");
            return false;
        }
        
        target.setHealth(((Damageable) target).getMaxHealth());
        
        if (sender.equals(target)) {
            sender.sendMessage("§eVous vous êtes heal.");
            return true;
        }
        
        sender.sendMessage("§7Vous avez heal §e" + Methods.getRealName((OfflinePlayer) target));
        target.sendMessage("§e" + Methods.getRealName(sender) + " §7vous a heal.");
		return true;
	}

}
