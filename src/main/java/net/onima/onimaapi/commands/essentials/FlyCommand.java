package net.onima.onimaapi.commands.essentials;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.Methods;

public class FlyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_FLY_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		Player target;
		
		if (args.length > 0 && OnimaPerm.ONIMAAPI_FLY_OTHERS_COMMAND.has(sender))
			target = Bukkit.getPlayer(args[0]);
		else {
			if (!(sender instanceof Player)) {
				sender.sendMessage("§cSeulement les joueurs peuvent se mettre en fly !");
				return false;
			}
			
			target = (Player) sender;
		}
		
		if (target == null) {
			sender.sendMessage("§c" + args[0] + " n'est pas connecté !");
			return false;
		}
		
		boolean newFlight = !target.getAllowFlight();
		String msg = newFlight ? "§aactivé" : "§cdésactivé";
		 
		target.setAllowFlight(newFlight);
         
		if (newFlight)
			target.setFlying(true);

		if (!target.getName().equalsIgnoreCase(sender.getName())) {
			sender.sendMessage("§6Vous avez " + msg + " §6le fly de §e" + Methods.getName(target, true) + "§6.");
			target.sendMessage("§e" + Methods.getRealName(sender) + " §6a " + msg + " §6votre fly.");
		} else
			sender.sendMessage("§6Vous avez " + msg + " §6votre fly.");
		
		return true;
	}

}
