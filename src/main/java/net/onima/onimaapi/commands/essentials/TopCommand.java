package net.onima.onimaapi.commands.essentials;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.Methods;

public class TopCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.TOP_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande.");
			return false;
		}
		
		Player player = (Player) sender;
		
        Location origin = player.getLocation().clone();
        Location highestLocation = origin.getWorld().getHighestBlockAt(origin).getLocation();
        
        if (highestLocation != null && !Methods.locationEquals(origin, highestLocation) && highestLocation.getBlockY() - origin.getBlock().getY() != 0) {
            player.teleport(highestLocation.add(0.5, 0, 0.5));
            sender.sendMessage("§eVous avez été téléporté au point le plus haut.");
            return true;
        }
        
        player.sendMessage("§cAucun point trouvé.");
		return false;
	}

}
