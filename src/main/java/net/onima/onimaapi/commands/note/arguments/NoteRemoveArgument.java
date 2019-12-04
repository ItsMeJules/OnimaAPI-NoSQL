package net.onima.onimaapi.commands.note.arguments;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.gui.buttons.DisplayButton;
import net.onima.onimaapi.gui.menu.ConfirmationMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.players.notes.Note;
import net.onima.onimaapi.players.notes.NotePriority;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class NoteRemoveArgument extends BasicCommandArgument {

	public NoteRemoveArgument() {
		super("remove", OnimaPerm.ONIMAAPI_NOTE_REMOVE_ARGUMENT);
		
		usage = new JSONMessage("§7/note remove <player> <index>", "§d§oSupprime la note à l'index donné.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 3, true))
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
		
		OfflineAPIPlayer.getPlayer(uuid, offline -> {
			if (index > offline.getNotes().size() || index < 1) {
				sender.sendMessage("§cL'index " + index + " n'existe pas !");
				return;
			}

			Note note = offline.getNotes().get(index - 1);
			NotePriority priority = note.getPriority();
			
			if (priority.getPriority() > 0 && sender instanceof Player) {
				Player player = (Player) sender;
				BetterItem item = new BetterItem(Material.PAPER, 0, 1, "§fCette note est de priorité : " + priority.getColor() + priority.name());
				
				new ConfirmationMenu("§cSupprimer cette note.", response -> {
					if (response) {
						offline.getNotes().remove(note);
						sender.sendMessage("§eVous §7avez supprimé la note suivante de §e" + Methods.getName(offline, true) + " §7:");
						player.spigot().sendMessage(note.getComponNote(index, offline.getName()));
					}

					return true;
				}, true, new DisplayButton(item)).open(APIPlayer.getPlayer(player));
				
			} else {
				offline.getNotes().remove(note);
				sender.sendMessage("§eVous §7avez supprimé la note suivante de §e" + Methods.getName(offline, true) + " §7:");
				Methods.sendJSON(sender, note.getComponNote(index, offline.getName()));
			}
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
