package net.onima.onimaapi.commands.mountain.arguments.staff;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.mountain.utils.Mountain;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;
import net.onima.onimaapi.utils.time.Time.LongTime;
import net.onima.onimaapi.utils.time.TimeUtils;

public class MountainScheduleArgument extends BasicCommandArgument {

	public MountainScheduleArgument() {
		super("schedule", OnimaPerm.ONIMAAPI_MOUNTAIN_SCHEDULE_ARGUMENT);
		usage = new JSONMessage("§7/mountain " + name + " <name> <1we | 1da | 1mo>", "§d§oProgramme une montagne. Exemple : \n - /mountain schedule Glowstone 1we.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 3, true))
			return false;
		
		Mountain mountain = null;
		
		if ((mountain = Mountain.getByName(args[1])) == null) {
			sender.sendMessage("§cLa montagne " + args[1] + " n'existe pas !");
			return false;
		}
		
		long time = TimeUtils.timeToMillis(args[2]);
		
		if (time == -1) {
			sender.sendMessage("§cLa valeur " + args[2] + " n'est pas un nombre !");
			return false;
		} else if (time == -2) {
			sender.sendMessage("§cMauvais format pour : " + args[2] + " il faut écrire les deux premières lettres du temps. Exemple : §o/mountain schedule §c1we pour toutes les semaines.");
			return false; 
		}
		
		mountain.scheduleEvery(time);
		OnimaAPI.broadcast("§d§o" + Methods.getRealName(sender) + " §7a programmé la montagne de type §d" + mountain.getType().name() + " §7nommée §d" + mountain.getName() + " §7pour chaque §d§o" + LongTime.setYMDWHMSFormat(time) + "§7.", OnimaPerm.ONIMAAPI_MOUNTAIN_SCHEDULE_ARGUMENT);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2 && OnimaPerm.ONIMAAPI_MOUNTAIN_SCHEDULE_ARGUMENT.has(sender))
			return Mountain.getMountains().stream().map(Mountain::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
			
		return Collections.emptyList();
	}

}
