package net.onima.onimaapi.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;

public class PayCommand implements CommandExecutor {
	
	private JSONMessage usage = new JSONMessage("§7/pay <player> <amount>", "§d§oPaye un joueur.");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_BALANCE_PAY_ARGUMENT.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (args.length == 2)
			return OnimaAPI.getInstance().getCommandManager().getBalanceExecutor().getArgument("pay").onCommand(sender, cmd, label, new String[] {"F", args[0], args[1]});
		else
			usage.send(sender, "§7Utilisation : ");
		
		return false;
	}

}
