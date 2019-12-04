package net.onima.onimaapi.commands.mountain.arguments.staff;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.items.Wand;
import net.onima.onimaapi.mountain.utils.Mountain;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class MountainLocationArgument extends BasicCommandArgument {

	public MountainLocationArgument() {
		super("location", OnimaPerm.ONIMAAPI_MOUNTAIN_LOCATION_ARGUMENT);

		usage = new JSONMessage("§7/mountain " + name + " <mountain>", "§d§oRedéfinit la location d'une montagne.");
		playerOnly = true;
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
		
		Wand wand = APIPlayer.getPlayer((Player) sender).getWand();
		
		if (!wand.hasAllLocationsSet()) {
			sender.sendMessage("§cVous devez sélecionner une zone !");
			sender.sendMessage("  §d§oLocation §7manquante : §d§on°" + (wand.getLocation1() == null ? "1" : "2"));
			return false;
		}
		
		mountain.setLocations(wand.getLocation1(), wand.getLocation2());
		OnimaAPI.broadcast("§d" + Methods.getRealName(sender) + "§7a redéfinit la location de la montagne §d" + mountain.getName(), OnimaPerm.ONIMAAPI_MOUNTAIN_LOCATION_ARGUMENT);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2 && OnimaPerm.ONIMAAPI_MOUNTAIN_LOCATION_ARGUMENT.has(sender))
			return Mountain.getMountains().stream().map(Mountain::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
			
		return Collections.emptyList();
	}

}
