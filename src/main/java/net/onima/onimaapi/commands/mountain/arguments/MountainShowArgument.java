package net.onima.onimaapi.commands.mountain.arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.mountain.utils.Mountain;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class MountainShowArgument extends BasicCommandArgument {

	public MountainShowArgument() {
		super("show", null, new String[] {"info"});
		usage = new JSONMessage("§7/mountain " + name + " <mountain>", "§d§oAffiche les informations d'une montagne.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 2, true))
			return false;
		
		Mountain mountain = null;
		
		if ((mountain = Mountain.getByName(args[1])) == null) {
			sender.sendMessage("§cL'event " + args[1] + " n'existe pas !");
			return false;
		}
		
		mountain.sendShow(sender);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length != 2)
			return Collections.emptyList();
		
		return Mountain.getMountains().stream().map(Mountain::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toCollection(() -> new ArrayList<>(Mountain.getMountains().size())));
	}

}
