package net.onima.onimaapi.commands.crate.arguments;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.crates.PhysicalCrate;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.items.Wand;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class CrateLocationArgument extends BasicCommandArgument {

	public CrateLocationArgument() {
		super("location", OnimaPerm.ONIMAAPI_CRATE_LOCATION_ARGUMENT);
		
		usage = new JSONMessage("§7/crate " + name + " <crate>", "§d§oDéfinit la location d'une crate physique.");
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
	
		if (!(crate instanceof PhysicalCrate)) {
			sender.sendMessage("§cSeulement les crates physique peuvent avoir une location !");
			return false;
		}
		
		PhysicalCrate physicalCrate = (PhysicalCrate) crate;
		Wand wand = APIPlayer.getPlayer((Player) sender).getWand();
		
		if (wand.getLocation1() == null && wand.getLocation2() == null) {
			sender.sendMessage("§cVous devez sélectionner un block avec la wand !");
			return false;
		}
		
		physicalCrate.setBlock(wand.getLocation1() == null ? wand.getLocation2().getBlock() : wand.getLocation1().getBlock());
		sender.sendMessage("§dVous §7avez définit la location de la crate " + crate.getName() + "§7.");
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2)
			return Crate.getCrates().stream().filter(crate -> crate instanceof PhysicalCrate).map(Crate::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
		else
			return Collections.emptyList();
	}

}
