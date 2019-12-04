package net.onima.onimaapi.commands.warp.arguments;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.Warp;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class WarpCreateArgument extends BasicCommandArgument {

	public WarpCreateArgument() {
		super("create", OnimaPerm.ONIMAAPI_WARP_CREATE_ARGUMENT);
		
		usage = new JSONMessage("§7/warp " + name + " <name>", "§d§oPermet de créer un warp.");
		
		playerOnly = true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 2, true))
			return false;
		
		if (Warp.getByName(args[1]) != null) {
			sender.sendMessage("§cUn warp avec le nom " + args[1] + " existe déjà.");
			return false;
		}
		
		if (args[1].equalsIgnoreCase("create") || args[1].equalsIgnoreCase("remove")) {
			sender.sendMessage("§cImpossible de créer un warp avec ce nom.");
			return false;
		}
		
		Warp warp = new Warp(args[1], ((Player) sender).getLocation());
		
		warp.setCreator(Methods.getRealName(sender));
		
		sender.sendMessage("§dVous §7avez créé le warp §d" + warp.getName() + "§7.");
		return true;
	}

}
