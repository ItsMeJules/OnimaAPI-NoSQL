package net.onima.onimaapi.commands.note.arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.players.notes.NotePriority;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class NotePriorityArgument extends BasicCommandArgument {

	public NotePriorityArgument() {
		super("priority", OnimaPerm.ONIMAAPI_NOTE_PRIORITY_ARGUMENT);
		
		usage = new JSONMessage("§7/note priority <player> <index> <priority>", "§d§oDéfinit la priorité d'une note.");
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
		
		NotePriority priority = NotePriority.fromName(args[3]);
		
		if (priority == null) {
			sender.sendMessage("§cLa priorité de note " + args[3] + " n'existe pas !");
			return false;
		}
		
		OfflineAPIPlayer.getPlayer(uuid, offline -> {
			if (index > offline.getNotes().size() || index < 1) {
				sender.sendMessage("§cL'index " + index + " n'existe pas !");
				return;
			}
			
			offline.getNotes().get(index - 1).setPriority(priority);
			sender.sendMessage("§eVous §7avez définit la priorité de la note §e" + index + " §7du joueur §e" + Methods.getName(offline, true) + " §7sur " + priority.getColor() + priority.name() + "§7.");
		});
		
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length != 4 || args.length != 2 || !OnimaPerm.ONIMAAPI_NOTE_PRIORITY_ARGUMENT.has(sender))
			return Collections.emptyList();
		
		if (args.length == 2)
			return OfflineAPIPlayer.getDisconnectedOfflineAPIPlayers().stream().map(offline -> {return Methods.getName(offline);}).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
		else if (args.length == 4) {
			List<String> completions = new ArrayList<>();
			
			for (NotePriority priority : NotePriority.values()) {
				if (StringUtil.startsWithIgnoreCase(priority.name(), args[3]))
					completions.add(priority.name());
			}
			
			return completions;
		} else
			return Collections.emptyList();
	}

}
