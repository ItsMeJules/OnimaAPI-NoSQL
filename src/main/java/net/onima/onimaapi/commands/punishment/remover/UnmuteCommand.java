package net.onima.onimaapi.commands.punishment.remover;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.punishment.utils.Punishment;
import net.onima.onimaapi.punishment.utils.PunishmentType;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;

public class UnmuteCommand implements CommandExecutor, TabCompleter {
	
	private JSONMessage usage = new JSONMessage("§7/unmute <player> <reason>", "§d§oPermet de démuter un joueur");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_UNMUTE_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (args.length < 2) {
			usage.send(sender, "§7Utilisation : ");
			return false;
		}
		
		OfflineAPIPlayer.getPlayer(args[0], offline -> {
			if (offline == null) {
				sender.sendMessage("§cLe joueur " + args[0] + " n'existe pas !");
				return;
			}
			
			if (!offline.hasPunishment(PunishmentType.MUTE)) {
				sender.sendMessage("§c" + Methods.getName(offline, true) + " n'est pas mute !");
				return;
			}
			
			String reason = StringUtils.join(args, ' ', 1, args.length);
			Punishment punishment = offline.getActivePunishments().stream()
					.filter(mute -> mute.getType() == PunishmentType.MUTE)
					.findFirst().orElse(null);
			
			punishment.setRemover(sender instanceof Player ? ((Player) sender).getUniqueId() : ConfigurationService.CONSOLE_UUID);
			punishment.setRemoveReason(reason);
			punishment.execute();
		});
		
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_UNMUTE_COMMAND.has(sender) || args.length != 1)
			return Collections.emptyList();
		
		List<String> completions = new ArrayList<>();
		
		for (OfflineAPIPlayer offline : OfflineAPIPlayer.getDisconnectedOfflineAPIPlayers()) {
			String name = Methods.getName(offline);
			
			if (offline.hasPunishment(PunishmentType.MUTE) && StringUtil.startsWithIgnoreCase(name, args[0]))
				completions.add(name);
		}
		
		return completions;
	}
	
}
