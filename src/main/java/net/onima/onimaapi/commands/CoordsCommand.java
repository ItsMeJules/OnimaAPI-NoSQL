package net.onima.onimaapi.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;

public class CoordsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (OnimaPerm.ONIMAAPI_COORDS_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		sender.sendMessage("§8" + ConfigurationService.STAIGHT_LINE);
		sender.sendMessage("  §7» §eSpawn §7: §60, 100, 0");
		sender.sendMessage("  §7» §5End §7: §6200, 0, 200");
		sender.sendMessage("  §9Koth §7:");
		sender.sendMessage("    §7» Fire §7: §6500, 100, 500");
		sender.sendMessage("    §7» Ice §7: §6§6-500, 100, 500");
		sender.sendMessage("    §7» Island §7: §6500, 100, -500");
		sender.sendMessage("    §7» Air §7: §6-500, 100, -500");
		sender.sendMessage("  §7» §eDestroy The Core §7: §61000, 100, 1000");
		sender.sendMessage("  §7» §2Conquest §7: §6750, 100, 750");
		sender.sendMessage("§8" + ConfigurationService.STAIGHT_LINE);
		
		return true;
	}

}
