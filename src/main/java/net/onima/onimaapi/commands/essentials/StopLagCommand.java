package net.onima.onimaapi.commands.essentials;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;

public class StopLagCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.STOPLAG_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		OnimaAPI.setStopLag(!OnimaAPI.hasStopLag());
		Bukkit.broadcastMessage("§6§lToutes les activitées intensives du serveur ont été " + (OnimaAPI.hasStopLag() ? "§c§larrêtées" : "§a§lremise") + " §6§lpar §e§l" + sender.getName() + "§6§l.");
		return true;
	}

}
