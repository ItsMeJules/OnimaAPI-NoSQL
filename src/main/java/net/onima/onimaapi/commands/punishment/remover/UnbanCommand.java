package net.onima.onimaapi.commands.punishment.remover;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.punishment.utils.Punishment;
import net.onima.onimaapi.punishment.utils.PunishmentType;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;

public class UnbanCommand implements CommandExecutor, TabCompleter {
	
	private JSONMessage usage = new JSONMessage("§7/unban <player> <reason>", "§d§oPermet d'unban un joueur");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_UNBAN_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (args.length < 2) {
			usage.send(sender, "§7Utilisation : ");
			return false;
		}
		
		UUID uuid = UUIDCache.getUUID(args[0]);
		
		if (uuid == null) {
			sender.sendMessage("§cLe joueur " + args[0] + " n'existe pas !");
			return false;
		}
		
		OfflineAPIPlayer.getPlayer(uuid, offline -> {
			if (!offline.hasPunishment(PunishmentType.BAN) && !offline.hasPunishment(PunishmentType.TEMPBAN)) {
				sender.sendMessage("§c" + Methods.getName(offline, true) + " n'est pas banni !");
				return;
			}
			
			String reason = StringUtils.join(args, ' ', 1, args.length);
			Punishment punishment = offline.getActivePunishments().stream()
					.filter(ban -> ban.getType() == PunishmentType.BAN || ban.getType() == PunishmentType.TEMPBAN)
					.findFirst().orElse(null);
			
			punishment.setRemover(sender instanceof Player ? ((Player) sender).getUniqueId() : ConfigurationService.CONSOLE_UUID);
			punishment.setRemoveReason(reason);
			punishment.execute();
		});
		
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_UNBAN_COMMAND.has(sender) || args.length != 1)
			return Collections.emptyList();
		
		List<String> completions = new ArrayList<>();
		
		for (OfflineAPIPlayer offline : OfflineAPIPlayer.getDisconnectedOfflineAPIPlayers()) {
			String name = Methods.getName(offline);
			
			if ((offline.hasPunishment(PunishmentType.BAN) || offline.hasPunishment(PunishmentType.TEMPBAN)) && StringUtil.startsWithIgnoreCase(name, args[0]))
				completions.add(name);
		}
		
		return completions;
	}
	
}
