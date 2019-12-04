package net.onima.onimaapi.commands.crate.arguments;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.crates.SupplyCrate;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class CrateDisplayNameArgument extends BasicCommandArgument {

	public CrateDisplayNameArgument() {
		super("displayname", OnimaPerm.ONIMAAPI_CRATE_DISPLAYNAME_ARGUMENT);
		
		usage = new JSONMessage("§7/crate " + name + " <crate> <name>", "§d§oDéfinit le nom à afficher de la crate.");
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
		
		if (crate instanceof SupplyCrate) {
			sender.sendMessage("§cLes supply crates ont déjà un display name définit par défaut !");
			return false;
		}
		
		crate.setDisplayName(Methods.colors(args[2]));
		sender.sendMessage("§7Le nom de la crate est maintenant : " + crate.getDisplayName());
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2)
			return Crate.getCrates().stream().filter(crate -> !(crate instanceof SupplyCrate)).map(Crate::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
		else
			return Collections.emptyList();
	}
	
}
