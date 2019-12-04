package net.onima.onimaapi.commands.chat.arguments;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class ChatSlowArgument extends BasicCommandArgument {

	public ChatSlowArgument() {
		super("slow", OnimaPerm.ONIMAAPI_CHAT_SLOW_ARGUMENT);
		
		usage = new JSONMessage("§7/chat " + name + " <time>", "§d§oRalentit le chat.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 2, true))
			return false;
		
		Integer time = Methods.toInteger(args[1]);
		
		if (time == null) {
			sender.sendMessage("§c" + args[1] + " n'est pas un nombre !");
			return false;
		}
		
		boolean slow = time != 0;
		
		if (!OnimaAPI.getInstance().getChatManager().isSlowed() && !slow) {
			sender.sendMessage("§cLe chat n'est pas ralentit !");
			return false;
		}
		
		OnimaAPI.getInstance().getChatManager().slow(slow, time, slow ? sender.getName() : null);
		Bukkit.broadcastMessage("§a[" + Methods.getRealName(sender) + "] §7a " + (slow 
				? "ralentit le chat, vous pouvez envoyer un message toutes les " + time + " seconde" + (time > 1 ? 's' : "") 
				: "remis le chat normal") + '.');
		return true;
	}

}
