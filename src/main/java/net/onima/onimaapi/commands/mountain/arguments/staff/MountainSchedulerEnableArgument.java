package net.onima.onimaapi.commands.mountain.arguments.staff;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.mountain.utils.Mountain;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class MountainSchedulerEnableArgument extends BasicCommandArgument {

	public MountainSchedulerEnableArgument() {
		super("enablescheduler", OnimaPerm.ONIMAAPI_MOUNTAIN_ENABLESCHEDULER_ARGUMENT);
		usage = new JSONMessage("§7/mountain " + name + " <mountain>", "§d§oActive/Désactive un scheduler.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 2, true))
			return false;
		
		Mountain mountain = null;
		
		if ((mountain = Mountain.getByName(args[1])) == null) {
			sender.sendMessage("§cLa montagne" + args[1] + " n'existe pas !");
			return false;
		}
		
		mountain.setSchedulerEnabled(!mountain.isSchedulerEnabled());
		OnimaAPI.broadcast("§d§o" + Methods.getRealName(sender) + " §7a " + (mountain.isSchedulerEnabled() ? "§aactivé" : "§cdésactivé") + " §7le scheduler de la montagne de type §d" + mountain.getType().name() + " §7nommée §d" + mountain.getName() + "§7.", OnimaPerm.ONIMAAPI_MOUNTAIN_ENABLESCHEDULER_ARGUMENT);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length != 2) 
			return Collections.emptyList();
		
		return Mountain.getMountains().stream().filter(mountain -> !mountain.isSchedulerEnabled()).map(Mountain::getName).collect(Collectors.toList());
	}
	
}
