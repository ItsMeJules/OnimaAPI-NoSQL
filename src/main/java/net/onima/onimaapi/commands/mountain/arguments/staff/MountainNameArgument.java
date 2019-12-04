package net.onima.onimaapi.commands.mountain.arguments.staff;

import java.util.ArrayList;
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

public class MountainNameArgument extends BasicCommandArgument {

	public MountainNameArgument() {
		super("name", OnimaPerm.ONIMAAPI_MOUNTAIN_NAME_ARGUMENT, new String[] {"setname"});
		usage = new JSONMessage("§7/mountain " + name + " <name> <new-name>", "§d§oChange le nom d'une montagne.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 3, true))
			return false;
		
		Mountain mountain = null;

		if ((mountain = Mountain.getByName(args[1])) == null) {
			sender.sendMessage("§cLa montagne " + args[1] + " n'existe pas !");
			return false;
		}
		
		OnimaAPI.broadcast("§d§o" + Methods.getRealName(sender) + "§7a renommé la montagne §d§o" + mountain.getName() + " §7en §d§o" + args[2] + "§7.", OnimaPerm.ONIMAAPI_MOUNTAIN_NAME_ARGUMENT);
		mountain.setName(args[2]);
		return true;
	}

	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length != 2)
			return Collections.emptyList();
		
		return Mountain.getMountains().stream().map(Mountain::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toCollection(() -> new ArrayList<>(Mountain.getMountains().size())));
	}
	
}
