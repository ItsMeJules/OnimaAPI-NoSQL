package net.onima.onimaapi.commands.region.arguments;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;
import net.onima.onimaapi.zone.type.Region;

public class RegionDTRLossArgument extends BasicCommandArgument {

	public RegionDTRLossArgument() {
		super("dtrloss", OnimaPerm.ONIMAAPI_REGION_DTRLOSS_ARGUMENT);
		usage = new JSONMessage("§7/region " + name + " <name>", "§d§oActive ou non la perte de DTR.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 2, true))
			return false;
		
		Region region = null;
		
		if ((region = Region.getByName(args[1])) == null) {
			sender.sendMessage("§cLa région " + args[1] + " n'existe pas !");
			return false;
		}
		
		region.setDTRLoss(!region.hasDTRLoss());
		sender.sendMessage("§7La région §d" + region.getDisplayName(sender) + " §7a maintenant la perte de DTR " + (region.hasDTRLoss() ? "§aactivé" : "§cdésactivé") + " §7.");
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length != 2 && !OnimaPerm.ONIMAAPI_REGION_DEATHBAN_ARGUMENT.has(sender))
			return Collections.emptyList();
		
		return Region.getRegions().stream().map(Region::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
	}

}
