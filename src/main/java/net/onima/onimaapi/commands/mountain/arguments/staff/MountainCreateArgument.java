package net.onima.onimaapi.commands.mountain.arguments.staff;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.items.Wand;
import net.onima.onimaapi.mountain.GlowstoneMountain;
import net.onima.onimaapi.mountain.OreMountain;
import net.onima.onimaapi.mountain.TreasureMountain;
import net.onima.onimaapi.mountain.utils.Mountain;
import net.onima.onimaapi.mountain.utils.MountainType;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class MountainCreateArgument extends BasicCommandArgument {

	public MountainCreateArgument() {
		super("create", OnimaPerm.ONIMAAPI_MOUNTAIN_CREATE_ARGUMENT);

		usage = new JSONMessage("§7/mountain " + name + " <name> <type>", "§d§oCréée une montagne");
		playerOnly = true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 3, true))
			return false;
		
		if (Mountain.getByName(args[1]) != null) {
			sender.sendMessage("§cLa montagne " + name + " existe déjà !");
			return false;
		}
		
		MountainType type = MountainType.fromString(args[2]);
		
		if (type == null) {
			sender.sendMessage("§cLe type de montagne " + args[2] + " n'existe pas !");
			return false;
		}
		
		Wand wand = APIPlayer.getPlayer((Player) sender).getWand();
		
		if (!wand.hasAllLocationsSet()) {
			sender.sendMessage("§cVous devez sélecionner une zone !");
			sender.sendMessage("  §d§oLocation §7manquante : §d§on°" + (wand.getLocation1() == null ? "1" : "2"));
			return false;
		}
		
		Mountain mountain = null;
		
		switch (type) {
		case GLOWSTONE:
			mountain = new GlowstoneMountain(args[1], wand.getLocation1(), wand.getLocation2(), Methods.getRealName(sender));
			break;
		case ORES:
			mountain = new OreMountain(args[1], wand.getLocation1(), wand.getLocation2(), Methods.getRealName(sender));
			break;
		case TREASURE:
			mountain = new TreasureMountain(args[1], wand.getLocation1(), wand.getLocation2(), Methods.getRealName(sender));
			break;
		default:
			break;
		}
		
		OnimaAPI.broadcast("§d" + mountain.getCreator() + " §7a créé une montagne de type §d" + type.name() + " §7nommée §d" + mountain.getName() + "§7.", permission);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> completions = new ArrayList<>();
		
		if (args.length == 3) {
			for (MountainType type : MountainType.values()) {
				if (StringUtil.startsWithIgnoreCase(type.name(), args[2]));
					completions.add(type.name());
			}
		}
		
		return completions;
	}

}
