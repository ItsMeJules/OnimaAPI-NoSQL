package net.onima.onimaapi.commands.essentials;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.OSound;

public class BroadcastCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.BROADCAST_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		Methods.playServerSound(new OSound(Sound.LEVEL_UP, 1.5F, 2F));
		Bukkit.broadcastMessage("§4Alerte §7» §9" + StringUtils.replaceChars(StringUtils.join(args, ' ', 0, args.length), '&', '§') + " §6#" + Methods.getRealName(sender));
		return true;
	}

}
