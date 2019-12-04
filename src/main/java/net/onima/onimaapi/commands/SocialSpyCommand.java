package net.onima.onimaapi.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.utils.PlayerOption;
import net.onima.onimaapi.rank.OnimaPerm;

public class SocialSpyCommand implements CommandExecutor {
	
	private static boolean consoleSpy;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_COMMAND_SOCIALSPY.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		String change = "";
		
		if (sender instanceof Player)
			change = APIPlayer.getPlayer((Player) sender).getOptions().reverseBoolean(PlayerOption.GlobalOptions.SOCIAL_SPY) ? "§aactivé" : "§cdésactivé";
		else
			change = (consoleSpy = !consoleSpy) ? "§aactivé" : "§cdésactivé";
		
		sender.sendMessage("§eVous avez " + change + " §ele social spy !");
		return true;
	}
	
	public static boolean isConsoleSpying() {
		return consoleSpy;
	}

}
