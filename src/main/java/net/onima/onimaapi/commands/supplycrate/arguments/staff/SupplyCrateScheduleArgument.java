package net.onima.onimaapi.commands.supplycrate.arguments.staff;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.Scheduler;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;
import net.onima.onimaapi.utils.time.Time.LongTime;
import net.onima.onimaapi.utils.time.TimeUtils;

public class SupplyCrateScheduleArgument extends BasicCommandArgument {

	public SupplyCrateScheduleArgument() {
		super("schedule", OnimaPerm.ONIMAAPI_SUPPLYCRATE_SCHEDULE_ARGUMENT);
		
		usage = new JSONMessage("§7/supplycrate " + name + " <crate> <1we | 1da | 1mo>", "§d§oProgramme un drop. Exemple : \n - /supplycrate schedule 1we.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 3, true))
			return false;
		
		Crate crate = null;
		
		if ((crate = Crate.getByName(args[1])) == null) {
			sender.sendMessage("§cLa crate " + args[1] + " n'existe pas !");
			return false;
		}	
		
		if (!(crate instanceof Scheduler)) {
			sender.sendMessage("§cVous ne pouvez pas programmer ce genre de crate !");
			return false;
		}
		
		Scheduler scheduler = (Scheduler) crate;
		long time = TimeUtils.timeToMillis(args[2]);
		
		if (time == -1) {
			sender.sendMessage("§cLa valeur " + args[2] + " n'est pas un nombre !");
			return false;
		} else if (time == -2) {
			sender.sendMessage("§cMauvais format pour : " + args[2] + " il faut écrire les deux premières lettres du temps. Exemple : §o/supplycrate schedule §c1we pour toutes les semaines.");
			return false; 
		}
		
		scheduler.scheduleEvery(time);
		OnimaAPI.broadcast("§d§o" + Methods.getRealName(sender) + " §7a programmé un drop §7pour chaque §d§o" + LongTime.setYMDWHMSFormat(time) + "§7.", OnimaPerm.ONIMAAPI_SUPPLYCRATE_SCHEDULE_ARGUMENT);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2 && OnimaPerm.ONIMAAPI_SUPPLYCRATE_SCHEDULE_ARGUMENT.has(sender))
			return Crate.getCrates().stream().filter(crate -> crate instanceof Scheduler).map(Crate::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
		else 
			return Collections.emptyList();
	}

}
