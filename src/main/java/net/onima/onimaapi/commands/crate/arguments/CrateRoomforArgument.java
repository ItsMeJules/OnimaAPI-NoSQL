package net.onima.onimaapi.commands.crate.arguments;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.crates.RoomCrate;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.crates.utils.CrateType;
import net.onima.onimaapi.items.Wand;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;
import net.onima.onimaapi.zone.Cuboid;

public class CrateRoomforArgument extends BasicCommandArgument {

	public CrateRoomforArgument() {
		super("roomfor", OnimaPerm.ONIMAAPI_CRATE_ROOMFOR_ARGUMENT);
		
		usage = new JSONMessage("§7/crate " + name + " <crate>", "§d§oDéfinit la salle pour une crate.");
		playerOnly = true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 2, true))
			return false;
		
		Crate crate = null;
		
		if ((crate = Crate.getByName(args[1])) == null) {
			sender.sendMessage("§cLa crate " + args[1] + " n'existe pas !");
			return false;
		}
	
		if (!(crate instanceof RoomCrate)) {
			sender.sendMessage("§cSeulement les crates de type " + CrateType.ROOM.name() + " peuvent avoir une salle !");
			return false;
		}
		
		RoomCrate roomCrate = (RoomCrate) crate;
		Wand wand = APIPlayer.getPlayer((Player) sender).getWand();
		
		if (!wand.hasAllLocationsSet()) {
			sender.sendMessage("§cVous devez sélecionner une zone !");
			sender.sendMessage("  §d§oLocation §7manquante : §d§on°" + (wand.getLocation1() == null ? "1" : "2"));
			return false;
		}
		
		if (!roomCrate.setRoom(new Cuboid(wand.getLocation1(), wand.getLocation2(), true))) {
			sender.sendMessage("§cSoit il n'y a pas de coffre dans la zone, soit il n'y en a pas assez comparé aux nombre de prix donné.");
			return false;
		}
		
		sender.sendMessage("§dVous §7avez défini la salle pour la crate " + crate.getName() + "§7.");
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2)
			return Crate.getCrates().stream().filter(crate -> crate instanceof RoomCrate).map(Crate::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
		else
			return Collections.emptyList();
	}

}
