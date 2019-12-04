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
import net.onima.onimaapi.players.notes.Note;
import net.onima.onimaapi.players.notes.NotePriority;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class NoteListArgument extends BasicCommandArgument {

	public NoteListArgument() {
		super("list", OnimaPerm.ONIMAAPI_NOTE_LIST_ARGUMENT);
		
		usage = new JSONMessage("§7/note list <player> (priority)", "§d§oAffiche toutes les notes.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 2, true))
			return false;

		UUID uuid = UUIDCache.getUUID(args[1]);
		
		if (uuid == null) {
			sender.sendMessage("§c" + args[1] + " ne s'est jamais connecté sur le serveur !");
			return false;
		}
		
		NotePriority priority = args.length > 2 ? NotePriority.fromName(args[2]) : null;
		
		OfflineAPIPlayer.getPlayer(uuid, offline -> {
			List<Note> notes = offline.getNotes().stream().filter(note -> priority == null ? true : priority.getPriority() == note.getPriority().getPriority()).collect(Collectors.toList());
			int size = notes.size(), index = 1;
			
			sender.sendMessage("§e" + Methods.getName(offline, true) + " §7a §e" + size + " §7note" + (size > 1 ? "s" : "") + (priority == null ? "" : " de type " + priority.getColor() + priority.name()) + " §7:");
			for (Note note : notes) {
				Methods.sendJSON(sender, note.getComponNote(index, offline.getName()));
				index++;
			}
		});
		
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 2 || !OnimaPerm.ONIMAAPI_NOTE_LIST_ARGUMENT.has(sender))
			return Collections.emptyList();
		
		if (args.length == 2)
			return OfflineAPIPlayer.getDisconnectedOfflineAPIPlayers().stream().map(offline -> {return Methods.getName(offline);}).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
		else if (args.length == 3) {
			List<String> completions = new ArrayList<>();
			
			for (NotePriority priority : NotePriority.values()) {
				if (StringUtil.startsWithIgnoreCase(priority.name(), args[2]))
					completions.add(priority.name());
			}
			
			return completions;
		} else
			return Collections.emptyList();
	}

}
