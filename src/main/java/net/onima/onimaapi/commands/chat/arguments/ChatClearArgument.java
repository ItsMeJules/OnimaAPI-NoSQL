package net.onima.onimaapi.commands.chat.arguments;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class ChatClearArgument extends BasicCommandArgument {

	public ChatClearArgument() {
		super("clear", OnimaPerm.ONIMAAPI_CHAT_CLEAR_ARGUMENT);
		
		usage = new JSONMessage("§7/chat " + name + " (player)", "§d§oClear le chat.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 1, true))
			return false;
		
		if (args.length == 1) {
			OnimaAPI.getInstance().getChatManager().clear(95);
			Bukkit.broadcastMessage("§a[" + Methods.getRealName(sender) + "] §7a clear le chat !");
			OnimaAPI.getInstance().getChatManager().clear(5);
		} else if (args.length > 1) {
			Player target = Bukkit.getPlayer(args[1]);
			
			if (target == null) {
				sender.sendMessage("§c" + args[1] + " n'est pas connecté sur le serveur !");
				return false;
			}
			
			sender.sendMessage("§aVous §eavez clear le chat de §a" + Methods.getName(sender, true));
			OnimaAPI.getInstance().getChatManager().clear(95, target);
			target.sendMessage("§a[" + Methods.getRealName(sender) + "] §7a clear votre chat !");
			OnimaAPI.getInstance().getChatManager().clear(5, target);
		}
		
		return true;
	}

}
