package net.onima.onimaapi.commands.supplycrate.arguments.staff;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.Scheduler;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class SupplyCrateSchedulerEnableArgument extends BasicCommandArgument {

	public SupplyCrateSchedulerEnableArgument() {
		super("enablescheduler", OnimaPerm.ONIMAAPI_SUPPLYCRATE_ENABLESCHEDULER_ARGUMENT);
		
		usage = new JSONMessage("§7/supplycrate " + name + " <crate>", "§d§oActive/Désactive un scheduler.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 2, true))
			return false;
		
		Crate crate = null;
		
		if ((crate = Crate.getByName(args[1])) == null) {
			sender.sendMessage("§cLa crate " + args[1] + " n'existe pas !");
			return false;
		}	
		
		if (!(crate instanceof Scheduler)) {
			sender.sendMessage("§cVous ne pouvez pas programmer ce genre de crate !");
			return false;
		}
		
		Scheduler scheduler = (Scheduler) crate;
		
		scheduler.setSchedulerEnabled(!scheduler.isSchedulerEnabled());
		OnimaAPI.broadcast("§d§o" + Methods.getRealName(sender) + " §7a " + (scheduler.isSchedulerEnabled() ? "§aactivé" : "§cdésactivé") + " §7le scheduler du prochain drop.", OnimaPerm.ONIMAAPI_SUPPLYCRATE_ENABLESCHEDULER_ARGUMENT);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2 && OnimaPerm.ONIMAAPI_SUPPLYCRATE_ENABLESCHEDULER_ARGUMENT.has(sender))
			return Crate.getCrates().stream().filter(crate -> crate instanceof Scheduler).map(Crate::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
		else 
			return Collections.emptyList();
	}
	
}
