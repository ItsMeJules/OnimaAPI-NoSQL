package net.onima.onimaapi.commands.cooldown.arguments;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.cooldown.utils.Cooldown;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class CooldownRemoveArgument extends BasicCommandArgument {

	public CooldownRemoveArgument() {
		super("remove", OnimaPerm.ONIMAAPI_COOLDOWN_REMOVE_ARGUMENT);
		usage = new JSONMessage("§7/cooldown " + name + " <player> <cooldown>", "§d§oPermet d'enlever un cooldown.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 3, true))
			return false;
		
		UUID uuid = UUIDCache.getUUID(args[1]);
		
		if (uuid == null) {
			sender.sendMessage("§cLe joueur " + args[1] + " n'existe pas !");
			return false;
		}
		
		OfflineAPIPlayer.getPlayer(uuid, offline -> {
			Cooldown cooldown = Cooldown.getCooldown(args[2]);
			
			if (cooldown == null) {
				sender.sendMessage("§cLe cooldown " + args[2] + " n'existe pas !");
				return;
			}
			
			if (cooldown.getTimeLeft(offline.getUUID()) <= 0L) {
				sender.sendMessage("§c" + Methods.getName(offline, true) + " n'a pas le cooldown " + cooldown.getName() + " !");
				return;
			}
			
			offline.removeCooldown(cooldown);
			sender.sendMessage("§dVous §7avez enlevé le cooldown §d" + cooldown.getName() + " §7à §d" + Methods.getName(offline, true) + "§7.");
			if (offline.isOnline())
				((APIPlayer) offline).sendMessage("§d" + Methods.getRealName(sender) + " §7vous a enlevé le cooldown §d" + cooldown.getName() + "§7.");
		});
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_COOLDOWN_REMOVE_ARGUMENT.has(sender))
			return Collections.emptyList();
		
		if (args.length == 2)
			return super.onTabComplete(sender, cmd, label, args);
		else if (args.length == 3) {
			OfflineAPIPlayer offlinePlayer = OfflineAPIPlayer.getOfflineAPIPlayers().get(UUIDCache.getUUID(args[1]));
			
			if (offlinePlayer != null)
				return offlinePlayer.getCooldowns().stream().map(Cooldown::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[2])).collect(Collectors.toList());
		}
		
		return Collections.emptyList();
	}

}
