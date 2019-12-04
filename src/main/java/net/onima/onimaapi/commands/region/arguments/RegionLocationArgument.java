package net.onima.onimaapi.commands.region.arguments;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.event.region.RegionChangeEvent;
import net.onima.onimaapi.items.Wand;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;
import net.onima.onimaapi.zone.type.Region;

public class RegionLocationArgument extends BasicCommandArgument {

	public RegionLocationArgument() {
		super("location", OnimaPerm.ONIMAAPI_REGION_LOCATION_ARGUMENT);
		
		usage = new JSONMessage("§7/region " + name + " <region>", "§d§oDéfinit la location d'une région.");
		playerOnly = true;
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
		
		Wand wand = APIPlayer.getPlayer((Player) sender).getWand();
		
		if (!wand.hasAllLocationsSet()) {
			sender.sendMessage("§cVous devez sélecionner une zone !");
			sender.sendMessage("  §d§oLocation §7manquante : §d§on°" + (wand.getLocation1() == null ? "1" : "2"));
			return false;
		}
		
		region.setLocations(wand.getLocation1(), wand.getLocation2());
		
		RegionChangeEvent event = new RegionChangeEvent(region, APIPlayer.getPlayer((Player) sender), RegionChangeEvent.RegionChangementCause.RESIZE);
		Bukkit.getPluginManager().callEvent(event);
		
		OnimaAPI.broadcast("§d" + Methods.getRealName(sender) + "§7a redéfinit la location de la région §d" + region.getDisplayName(sender), OnimaPerm.ONIMAAPI_REGION_LOCATION_ARGUMENT);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length != 2 && !OnimaPerm.ONIMAAPI_REGION_LOCATION_ARGUMENT.has(sender))
			return Collections.emptyList();
		
		return Region.getRegions().stream().map(Region::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
	}

}
