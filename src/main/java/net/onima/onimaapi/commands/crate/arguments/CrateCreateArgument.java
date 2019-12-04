package net.onima.onimaapi.commands.crate.arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.crates.PhysicalCrate;
import net.onima.onimaapi.crates.RoomCrate;
import net.onima.onimaapi.crates.SupplyCrate;
import net.onima.onimaapi.crates.VirtualCrate;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.crates.utils.CrateType;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class CrateCreateArgument extends BasicCommandArgument {

	public CrateCreateArgument() {
		super("create", OnimaPerm.ONIMAAPI_CRATE_CREATE_ARGUMENT);
		
		usage = new JSONMessage("§7/crate " + name + " <name> <type>" , "§d§oCréé une crate.");
		playerOnly = true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 3, true))
			return false;
		
		if (Crate.getByName(args[1]) != null) {
			sender.sendMessage("§cUne crate avec le nom " + args[1] + " existe déjà !");
			return false;
		}
		
		CrateType type = CrateType.fromName(args[2]);
		
		if (type == null) {
			sender.sendMessage("§cLe type de crate " + args[2] + " n'existe pas !");
			return false;
		}
		
		Crate crate = null;
		
		switch (type) {
		case PHYSICAL:
			crate = new PhysicalCrate(args[1], 0);
			break;
		case ROOM:
			crate = new RoomCrate(args[1], 0);
			break;
		case VIRTUAL:
			crate = new VirtualCrate(args[1], 0);
			break;
		case SUPPLY:
			crate = new SupplyCrate(args[1], 0);
		default:
			break;
		}
		
		sender.sendMessage("§dVous §7avez créé la crate §d" + crate.getName() + "§7.");
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 3) {
			List<String> completions = new ArrayList<>();
			
			for (CrateType type : CrateType.values()) {
				if (StringUtil.startsWithIgnoreCase(type.name(), args[2]))
					completions.add(type.name());
			}
			
			return completions;
		}
		else
			return Collections.emptyList();
	}

}
