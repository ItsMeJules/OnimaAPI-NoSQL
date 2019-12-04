package net.onima.onimaapi.commands.region.arguments;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;
import net.onima.onimaapi.zone.type.Region;

public class RegionDeathbanMultiplierArgument extends BasicCommandArgument {

	public RegionDeathbanMultiplierArgument() {
		super("deathbanmultiplier", OnimaPerm.ONIMAAPI_REGION_DEATHBANMULTIPLIER_ARGUMENT);
		usage = new JSONMessage("§7/region " + name + " <region> <multiplier>", "§d§oDéfinit le multiplicateur de deathban.");
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
		
		Double multiplier = Methods.toDouble(args[2]);
		
		if (multiplier == null) {
			sender.sendMessage("§c" + args[2] + " n'est pas un nombre !");
			return false;
		}
		
		region.setDeathbanMultiplier(multiplier);
		sender.sendMessage("§dVous §7avez défini le multiplicateur de deathban pour la région §d" + region.getDisplayName(sender) + " §7sur §d" + multiplier + "§7.");
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length != 2 && !OnimaPerm.ONIMAAPI_REGION_DEATHBANMULTIPLIER_ARGUMENT.has(sender))
			return Collections.emptyList();
		
		return Region.getRegions().stream().map(Region::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
	}

}
