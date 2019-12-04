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
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class CrateRoomTPArgument extends BasicCommandArgument {

	public CrateRoomTPArgument() {
		super("roomtp", OnimaPerm.ONIMAAPI_CRATE_ROOMTP_ARGUMENT);
		
		usage = new JSONMessage("§7/crate " + name + " <crate>", "§d§oDéfinit la location où le joueur sera téléporté dans la salle.");
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
			sender.sendMessage("§cSeulement les room crates peuvent avoir une location !");
			return false;
		}
		
		RoomCrate roomCrate = (RoomCrate) crate;
		
		roomCrate.setTeleportLocation(((Player) sender).getLocation());
		sender.sendMessage("§dVous §7avez déifinit la location de téléportation pour la crate " + crate.getName() + "§7.");
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
