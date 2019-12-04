package net.onima.onimaapi.commands.crate.arguments;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class CrateDropAmountArgument extends BasicCommandArgument {

	public CrateDropAmountArgument() {
		super("dropamount", OnimaPerm.ONIMAAPI_CRATE_DROPAMOUNT_ARGUMENT, new String[] {"winamount"});
		
		usage = new JSONMessage("§7/crate " + name + " <crate> <amount>", "§d§oDéfinit le nombre de prix.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 3, true))
			return false;
		
		Crate crate = null;
		
		if ((crate = Crate.getByName(args[1])) == null) {
			sender.sendMessage("§cLa crate " + args[1] + " n'existe pas !");
			return false;
		}
		
		Integer amount = Methods.toInteger(args[2]);
		
		if (amount == null) {
			sender.sendMessage("§c" + args[2] + " n'est pas un nombre !");
			return false;
		}
		
		
		sender.sendMessage("§dVous §7avez définit le nombre d'items drop sur §d" + amount + " §7pour la crate " + crate.getName() + ".");
		crate.setPrizeAmount(amount);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2)
			return Crate.getCrates().stream().map(Crate::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
		else
			return Collections.emptyList();
	}

}
