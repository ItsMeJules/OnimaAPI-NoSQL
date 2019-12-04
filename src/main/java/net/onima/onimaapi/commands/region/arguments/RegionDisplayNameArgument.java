package net.onima.onimaapi.commands.region.arguments;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;
import net.onima.onimaapi.zone.type.Region;

public class RegionDisplayNameArgument extends BasicCommandArgument {

	public RegionDisplayNameArgument() {
		super("displayname", OnimaPerm.ONIMAAPI_REGION_DISPLAYNAME_ARGUMENT);
		usage = new JSONMessage("§7/region " + name + " <region> <name>", "§d§oDéfinit le nom à afficher pour la région.");
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
		
		String displayName = Methods.colors(StringUtils.join(args, ' ', 2, args.length));
		
		if (displayName.equalsIgnoreCase(region.getDisplayName(sender))) {
			sender.sendMessage("§cOh fils de pute pourquoi tu veux mettre le même display name ?"); 
			return false;
		}
		
		OnimaAPI.broadcast("§d" + Methods.getRealName(sender) + " §7a changé le nom d'affichage de la région §d" + region.getDisplayName(sender) + " §7en §d" + displayName + "§7.", OnimaPerm.ONIMAAPI_REGION_DISPLAYNAME_ARGUMENT);
		region.setDisplayName(displayName);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length != 2 && !OnimaPerm.ONIMAAPI_REGION_DISPLAYNAME_ARGUMENT.has(sender))
			return Collections.emptyList();
		
		return Region.getRegions().stream().map(Region::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
	}

}
