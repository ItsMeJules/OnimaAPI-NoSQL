package net.onima.onimaapi.commands.region.arguments;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.event.region.RegionOverridedEvent;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;
import net.onima.onimaapi.zone.type.Region;

public class RegionPriorityArgument extends BasicCommandArgument {

	public RegionPriorityArgument() {
		super("priority", OnimaPerm.ONIMAAPI_REGION_PRIORITY_ARGUMENT);
		
		usage = new JSONMessage("§7/region " + name + " <region> <priority>", "§d§oDéfinit la priorité d'une région.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 3, true))
			return false;
		
		final Region region = Region.getByName(args[1]);
		
		if (region == null) {
			sender.sendMessage("§cLa région " + args[1] + " n'existe pas !");
			return false;
		}
		
		Integer priority = Methods.toInteger(args[2]);
		
		if (priority == null) {
			sender.sendMessage("§c" + args[2] + " n'est pas un nombre !");
			return false;
		}
		
		Region.getRegions().parallelStream()
		.filter(other -> other.getPriority() < priority)
		.filter(other -> region.toCuboid().contains(other.toCuboid()))
		.forEach(other -> Bukkit.getPluginManager().callEvent(new RegionOverridedEvent(other, region, sender)));
		
		region.setPriority(priority);
		sender.sendMessage("§dVous §7avez défini la priorité de la région §d" + region.getDisplayName(sender) + " §7sur §d" + priority + "§7.");
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length != 2 && !OnimaPerm.ONIMAAPI_REGION_PRIORITY_ARGUMENT.has(sender))
			return Collections.emptyList();
		
		return Region.getRegions().stream().map(Region::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
	}

}
