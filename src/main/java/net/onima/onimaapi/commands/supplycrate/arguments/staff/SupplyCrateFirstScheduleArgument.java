package net.onima.onimaapi.commands.supplycrate.arguments.staff;

import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.CasualFormatDate;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.Scheduler;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class SupplyCrateFirstScheduleArgument extends BasicCommandArgument {
	
	private Pattern pattern;
	
	{
		pattern = Pattern.compile("[0-9]+");
	}

	public SupplyCrateFirstScheduleArgument() {
		super("firstschedule", OnimaPerm.ONIMAAPI_SUPPLYCRATE_FIRSTSCHEDULE_ARGUMENT);
		
		usage = new JSONMessage("§7/supplycrate " + name + " <crate> <dd-mm:HH-MM>", "§d§oDéfinit le jour où le drop se lancera pour la première fois. Exemple : \n §d§o- /supplycrate firstschedule 14-06:16-00.");
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
		
		Matcher matcher = pattern.matcher(args[2]);
		Integer[] date = {0, 0, 0, 0};
	    
	    for (int i = 0; matcher.find() && i < 4; i++) {
	    	String group = matcher.group();
	    	Integer digits = Methods.toInteger(group);
	    	
	    	if (digits == null) {
	    		sender.sendMessage("§c" + group + " n'est pas un nombre !");
	    		return false;
	    	}
	    	
	    	date[i] = digits;
	    }
		
	    scheduler.startTime(Month.of(date[1]), date[0], date[2], date[3]);
		OnimaAPI.broadcast("§d§o" + Methods.getRealName(sender) + " §7a définit la date du premier lancement du drop pour §d§o" + new CasualFormatDate("d u z h:i").toNormalDate(scheduler.getWhenItStarts()) + "§7.", OnimaPerm.ONIMAAPI_SUPPLYCRATE_FIRSTSCHEDULE_ARGUMENT);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2 && OnimaPerm.ONIMAAPI_SUPPLYCRATE_FIRSTSCHEDULE_ARGUMENT.has(sender))
			return Crate.getCrates().stream().filter(crate -> crate instanceof Scheduler).map(Crate::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
		else 
			return Collections.emptyList();
	}


}
