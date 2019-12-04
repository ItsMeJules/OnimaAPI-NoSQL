package net.onima.onimaapi.commands.warp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.commands.warp.arguments.WarpCreateArgument;
import net.onima.onimaapi.commands.warp.arguments.WarpListArgument;
import net.onima.onimaapi.commands.warp.arguments.WarpRemoveArgument;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.Warp;
import net.onima.onimaapi.utils.commands.ArgumentExecutor;

public class WarpExecutor extends ArgumentExecutor {

	public WarpExecutor() {
		super("warp", OnimaPerm.ONIMAAPI_WARP_COMMAND);
		
		addArgument(new WarpCreateArgument());
		addArgument(new WarpListArgument());
		addArgument(new WarpRemoveArgument());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande.");
			return false;
		}
		
		if (args.length < 1)
 			return super.onCommand(sender, command, label, args);
		else {
			if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("remove"))
				return super.onCommand(sender, command, label, args);
			
			Warp warp;
			
			if ((warp = Warp.getByName(args[0])) == null) {
				sender.sendMessage("§cLe warp " + args[0] + " n'existe pas !");
				return false;
			}
			
			((Player) sender).teleport(warp.getLocation());
			sender.sendMessage("§eVous avez été téléporté au warp §6" + warp.getName());
			return true;
		}
	}

}
