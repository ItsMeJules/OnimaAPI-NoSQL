package net.onima.onimaapi.commands.region.arguments;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;
import net.onima.onimaapi.zone.type.Region;

public class RegionNameArgument extends BasicCommandArgument {

	public RegionNameArgument() {
		super("name", OnimaPerm.ONIMAAPI_REGION_NAME_ARGUMENT);
		usage = new JSONMessage("§7/region " + name + " <region> <name>", "§d§oChange le nom d'une région.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 3, true))
			return false;
		
		Region region = null;
		
		if ((region = Region.getByName(args[1])) == null) {
			sender.sendMessage("§cLa région " + args[1] + " n'existe pas !");
			return false;
		}
		
		if (args[2].equalsIgnoreCase(region.getName())) {
			sender.sendMessage("§cOh fils de pute pourquoi tu veux mettre le même nom ?"); 
			return false;
		}
		
		if (Region.getByName(args[2]) != null) {
			sender.sendMessage("§cUne région existe déjà avec le nom " + args[2]);
			return false;
		}

		OnimaAPI.broadcast("§d" + Methods.getRealName(sender) + " §7a changé le nom de la région §d" + region.getName() + " §7en §d" + args[2] + "§7.", OnimaPerm.ONIMAAPI_REGION_NAME_ARGUMENT);
		region.setName(args[2]);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length != 2 && !OnimaPerm.ONIMAAPI_REGION_PRIORITY_ARGUMENT.has(sender))
			return Collections.emptyList();
		
		return Region.getRegions().stream().map(Region::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
	}

}
