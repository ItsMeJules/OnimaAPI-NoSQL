package net.onima.onimaapi.commands.ignore.arguments;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class IgnoreListArgument extends BasicCommandArgument {

	public IgnoreListArgument() {
		super("list", OnimaPerm.ONIMAAPI_IGNORE_LIST_ARGUMENT);
		
		usage = new JSONMessage("§7/ignore " + name, "§d§oAffiche la liste des joueurs ignorés.");
		playerOnly = true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 1, true))
			return false;
		
		APIPlayer apiPlayer = APIPlayer.getPlayer((Player) sender);
		List<UUID> ignored = apiPlayer.getIgnored();
		
		sender.sendMessage("§9Joueurs ignorés (" + ignored.size() + ')');
		ComponentBuilder builder = new ComponentBuilder("");
		Iterator<UUID> iterator = ignored.iterator();
		boolean hasPerm = OnimaPerm.ONIMAAPI_DISGUISE_COMMAND_LIST.has(sender);
		
		while (iterator.hasNext()) {
			UUID uuid = iterator.next();
			OfflinePlayer offline = Bukkit.getOfflinePlayer(uuid);
			String name = offline.isOnline() ? Methods.getName(APIPlayer.getPlayer(uuid), hasPerm) : offline.getName();
			String commandName = hasPerm ? Methods.getRealName(offline) : name;
					
			builder.append(name);
			builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eDé-ignorer §6" + commandName).create()));
			builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ignore remove " + commandName));
			
			if (iterator.hasNext())
				builder.append(", ", FormatRetention.NONE);
		}
		
		Methods.sendJSON(sender, builder.create());
		return true;
	}

}
