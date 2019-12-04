package net.onima.onimaapi.commands.chat.arguments;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class ChatMuteArgument extends BasicCommandArgument {

	public ChatMuteArgument() {
		super("mute", OnimaPerm.ONIMAAPI_CHAT_MUTE_ARGUMENT);
		
		usage = new JSONMessage("§7/chat " + name, "§d§oMute le chat.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 1, true))
			return false;
		
		boolean mute = !OnimaAPI.getInstance().getChatManager().isMuted();
		
		OnimaAPI.getInstance().getChatManager().mute(mute, mute ? sender.getName() : null);
		sender.sendMessage("§a[" + Methods.getRealName(sender) + "] §7a " + (mute ? "§cmuté" : "§adémuté") + " §7le chat !");
		
		return true;
	}

}
