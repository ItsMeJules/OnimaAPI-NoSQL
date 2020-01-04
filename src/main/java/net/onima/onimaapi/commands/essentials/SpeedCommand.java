package net.onima.onimaapi.commands.essentials;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;

public class SpeedCommand implements CommandExecutor {
	
	private JSONMessage usage = new JSONMessage("§7/speed <speed> (player) (type)", "§d§oDéfinit la vitesse.");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player target;
		
		if (args.length < 1) {
			usage.send(sender, "§7Utilisation : ");
			return false;
		}
		
		if (args.length > 1 && OnimaPerm.SPEED_COMMAND.has(sender)) {
			UUID uuid = UUIDCache.getUUID(args[1]);
			
			if (uuid == null) {
				sender.sendMessage("§cLe joueur " + args[1] + " ne s'est jamais connecté sur le serveur !");
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
            sender.sendMessage("§c" + args[1] + " n'est pas connecté !");
            return false;
        }
        
        Float speed = Methods.toFloat(args[0]);
        
        
        if (speed == null) {
        	sender.sendMessage("§c" + args[0] + " n'est pas un nombre !");
        	return false;
        }

        speed /= 10;
        
        if (speed > 1) {
        	sender.sendMessage("§cVitesse max pour le vol et la marche : 10");
        	return false;
        }
        	
        boolean fly = target.isFlying();

        if (args.length > 2) {
        	if (args[2].equalsIgnoreCase("fly") || args[2].equalsIgnoreCase("f"))
        		fly = true;
        	else if (args[2].equalsIgnoreCase("walk") || args[2].equalsIgnoreCase("w"))
        		fly = false;
        }
        
        if (fly)
        	target.setFlySpeed(speed);
        else
        	target.setWalkSpeed(speed);
        
        if (sender.equals(target)) {
            sender.sendMessage("§7Vous avez défini votre vitesse de §e" + (fly ? "vol" : "marche") + " §7sur §e" + speed * 10 + "§7.");
            return true;
        }
        
        sender.sendMessage("§7Vous avez défini la vitesse de §e" + (fly ? "vol" : "marche") + " §7de §e" + target.getName() + " §7sur §e" + speed * 10 + "§7.");
		target.sendMessage("§e" + sender.getName() + "§7a défini votre vitesse de §e" + (fly ? "vol" : "marche") + " §7sur §e" + speed * 10 + "§7.");
        return true;
	}

}
