package net.onima.onimaapi.commands.mountain.arguments.staff;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.mountain.utils.Mountain;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class MountainRemoveArgument extends BasicCommandArgument {

	public MountainRemoveArgument() {
		super("remove", OnimaPerm.ONIMAAPI_MOUNTAIN_REMOVE_ARGUMENT);
		usage = new JSONMessage("§7/mountain " + name + " <mountain>", "§d§oSupprime une montagne.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 2, true))
			return false;
		
		Mountain mountain = null;
		
		if ((mountain = Mountain.getByName(args[1])) == null) {
			sender.sendMessage("§cLa montagne " + args[1] + " n'existe pas !");
			return false;
		}
		
		mountain.remove();
		OnimaAPI.broadcast("§d§o" + Methods.getRealName(sender) + " §7a supprimé la montagne de type §d" + mountain.getType().name() + " §7nommée §d" + mountain.getName() + "§7.", OnimaPerm.ONIMAAPI_MOUNTAIN_REMOVE_ARGUMENT);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length != 2 || OnimaPerm.ONIMAAPI_MOUNTAIN_REMOVE_ARGUMENT.has(sender))
			return Collections.emptyList();
			
		return Mountain.getMountains().stream().map(Mountain::getName).filter(name -> StringUtil.startsWithIgnoreCase(args[1], name)).collect(Collectors.toList());
	}

}
