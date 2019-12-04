package net.onima.onimaapi.commands.punishment;

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
import net.onima.onimaapi.punishment.TempBan;
import net.onima.onimaapi.punishment.utils.Punishment;
import net.onima.onimaapi.punishment.utils.PunishmentType;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.time.TimeUtils;

public class TempbanCommand implements CommandExecutor, TabCompleter {
	
	private JSONMessage usage = new JSONMessage("§7/tempban <player> <time> <reason>", "§d§oTempban un joueur.");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_TEMPBAN_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}

		if (args.length < 3) {
			usage.send(sender, "§7Utilisation : ");
			return false;
		}
		
		OfflineAPIPlayer.getPlayer(args[0], offline -> {
			if (offline == null) {
				sender.sendMessage("§c" + args[0] + " ne s'est jamais connecté sur le serveur !");
				return;
			}

			if (offline.hasPunishment(PunishmentType.TEMPBAN)) {
				sender.sendMessage("§c" + Methods.getName(offline, true) + " est déjà banni temporairement !");
				return;
			}
			
			long duration = TimeUtils.timeToMillis(args[1]);
			
			if (duration == -1) {
				sender.sendMessage("§cLa valeur " + args[1] + " n'est pas un nombre !");
				return;
			} else if (duration == -2) {
				sender.sendMessage("§cMauvais format pour : " + args[1] + " il faut écrire les deux premières lettres du temps. Exemple : §o/tempban " + args[0] + " §c15mi pour 15 minutes.");
				return; 
			}
			
			String reason = StringUtils.join(args, ' ', 2, args.length);
			Punishment mute = new TempBan(sender instanceof Player ? ((Player) sender).getUniqueId() : ConfigurationService.CONSOLE_UUID, offline.getUUID(), duration);
			
			mute.setSilent(args[args.length - 1].equalsIgnoreCase("-s"));
			mute.setReason(mute.isSilent() ? reason.substring(0, reason.length() - 2) : reason);
			mute.execute();
		});
		
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_TEMPBAN_COMMAND.has(sender) && args.length != 1)
			return Collections.emptyList();
		
		List<String> completions = new ArrayList<>();
		
		for (OfflineAPIPlayer offlineAPIPlayer : OfflineAPIPlayer.getDisconnectedOfflineAPIPlayers()) {
			if (offlineAPIPlayer.hasPunishment(PunishmentType.TEMPBAN))
				continue;
				
			String name = Methods.getName(offlineAPIPlayer);
			
			if (StringUtil.startsWithIgnoreCase(name, args[0]))
				completions.add(name);
		}
		
		return completions;
	}

}
