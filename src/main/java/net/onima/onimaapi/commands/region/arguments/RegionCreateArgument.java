package net.onima.onimaapi.commands.region.arguments;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.event.region.RegionChangeEvent;
import net.onima.onimaapi.items.Wand;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;
import net.onima.onimaapi.zone.type.Region;

public class RegionCreateArgument extends BasicCommandArgument {

	public RegionCreateArgument() {
		super("create", OnimaPerm.ONIMAAPI_REGION_CREATE_ARGUMENT);
		
		usage = new JSONMessage("§7/region " + name + " <name>", "§d§oPermet de créer une région.");
		playerOnly = true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 2, true))
			return false;
		
		if (Region.getByName(args[1]) != null) {
			sender.sendMessage("§cLa région " + args[1] + " existe déjà !");
			return false;
		}
		
		APIPlayer apiPlayer = APIPlayer.getPlayer((Player) sender);
		Wand wand = apiPlayer.getWand();

		if (!wand.hasAllLocationsSet()) {
			sender.sendMessage("§cVous devez sélectionner une zone !");
			sender.sendMessage("  §d§oLocation §7manquante : §d§on°" + (wand.getLocation1() == null ? "1" : "2"));
			return false;
		}
		
		RegionChangeEvent event = new RegionChangeEvent(new Region(args[1], args[1], Methods.getRealName(sender), wand.getLocation1(), wand.getLocation2()), apiPlayer, RegionChangeEvent.RegionChangementCause.CREATED);
		Bukkit.getPluginManager().callEvent(event);
		
		if (event.isCancelled()) {
			event.getRegion().remove();
			return false;
		}
			
		OnimaAPI.broadcast("§d§o" + event.getRegion().getCreator() + " §7a créé la région §d§o" + args[1] + "§7.", permission);
		return true;
	}

}
