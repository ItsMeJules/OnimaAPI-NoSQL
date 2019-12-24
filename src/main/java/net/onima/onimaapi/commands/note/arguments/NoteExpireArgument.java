package net.onima.onimaapi.commands.note.arguments;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;
import net.onima.onimaapi.utils.time.TimeUtils;

public class NoteExpireArgument extends BasicCommandArgument {

	public NoteExpireArgument() {
		super("expire", OnimaPerm.ONIMAAPI_NOTE_EXPIRE_ARGUMENT);
		
		usage = new JSONMessage("§7/note expire <player> <index> <time>", "§d§oAjoute une date d'expiration à une note.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 4, true))
			return false;
		
		UUID uuid = UUIDCache.getUUID(args[1]);
		
		if (uuid == null) {
			sender.sendMessage("§c" + args[1] + " ne s'est jamais connecté sur le serveur !");
			return false;
		}
		
		Integer index = Methods.toInteger(args[2]);
		
		if (index == null) {
			sender.sendMessage("§c" + args[2] + " n'est pas un nombre !");
			return false;
		}
		
		long time = TimeUtils.timeToMillis(args[3]);
		
		if (time == -1) {
			sender.sendMessage("§cLa valeur " + args[3] + " n'est pas un nombre !");
			return false;
		} else if (time == -2) {
			sender.sendMessage("§cMauvais format pour : " + args[3] + " il faut écrire les deux premières lettres du temps. Exemple : §o/note expire " + args[1] + ' ' + args[2] + " §c1we pour dans une semaine.");
			return false; 
		}
		
		OfflineAPIPlayer.getPlayer(uuid, offline -> {
			if (index > offline.getNotes().size() || index < 1) {
				sender.sendMessage("§cL'index " + index + " n'existe pas !");
				return;
			}
			
			offline.getNotes().get(index - 1).setExpire(time);
			sender.sendMessage("§7La note §e" + index + " §7expirera le §e" + Methods.toFormatDate(time + System.currentTimeMillis(), ConfigurationService.DATE_FORMAT_HOURS) + " §7pour le joueur " + Methods.getName(offline, true) + "§7.");
		});
		
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length != 2 || !OnimaPerm.ONIMAAPI_NOTE_REMOVE_ARGUMENT.has(sender))
			return Collections.emptyList();
		
		return OfflineAPIPlayer.getDisconnectedOfflineAPIPlayers().stream().map(offline -> {return Methods.getName(offline);}).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
	}

}
