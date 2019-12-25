package net.onima.onimaapi.commands.essentials;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.Methods;

public class TeleportAllCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_TPALL_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}
		
        Player player = (Player) sender;
        
        Bukkit.broadcastMessage("§e" + Methods.getRealName(sender) + " §a a téléporté tous les joueurs à lui !");
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!target.getUniqueId().equals(player.getUniqueId()))
                target.teleport(player, PlayerTeleportEvent.TeleportCause.COMMAND);
        }
        
        return true;
	}

}
