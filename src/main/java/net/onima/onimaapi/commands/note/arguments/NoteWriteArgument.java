package net.onima.onimaapi.commands.note.arguments;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.players.notes.Note;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class NoteWriteArgument extends BasicCommandArgument {

	public NoteWriteArgument() {
		super("write", OnimaPerm.ONIMAAPI_NOTE_WRITE_ARGUMENT, new String[] {"create", "add"});
		
		usage = new JSONMessage("§7/note write <player> <note>", "§d§oAjoute une note à un joueur.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 3, true))
			return false;
		
		UUID uuid = UUIDCache.getUUID(args[1]);
		
		if (uuid == null) {
			sender.sendMessage("§c" + args[1] + " ne s'est jamais connecté sur le serveur.");
			return false;
		}
		
		OfflineAPIPlayer.getPlayer(uuid, offline -> { //TODO check y'a un pb
			Note note = new Note(sender instanceof Player ? ((Player) sender).getUniqueId() : ConfigurationService.CONSOLE_UUID, StringUtils.join(args, ' ', 2, args.length));
			
			System.out.println(offline.getNotes());
			offline.getNotes().add(note);
			System.out.println(offline.getNotes());
			sender.sendMessage("§eVous §7avez ajouté une note à §e" + Methods.getName(offline, true));
		});
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length != 2 || !OnimaPerm.ONIMAAPI_NOTE_REMOVE_ARGUMENT.has(sender))
			return Collections.emptyList();
		
		return OfflineAPIPlayer.getDisconnectedOfflineAPIPlayers().stream().map(offline -> {return Methods.getName(offline);}).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
	}

}
