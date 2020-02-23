package net.onima.onimaapi.commands.cooldown.arguments;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.cooldown.utils.Cooldown;
import net.onima.onimaapi.cooldown.utils.CooldownData;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;
import net.onima.onimaapi.utils.time.Time.LongTime;

public class CooldownCheckArgument extends BasicCommandArgument {

	private static final int MAX_COOLDOWN_PER_PAGE = 6;
	
	public CooldownCheckArgument() {
		super("check", OnimaPerm.ONIMAAPI_COOLDOWN_CHECK_ARGUMENT);
		usage = new JSONMessage("§7/cooldown " + name + " <player> (page)", "§d§oPermet d'afficher les cooldowns d'un joueur.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 2, true))
			return false;
		
		UUID uuid = UUIDCache.getUUID(args[1]);
		
		if (uuid == null) {
			sender.sendMessage("§cLe joueur " + args[1] + " n'existe pas !");
			return false;
		}
		
		OfflineAPIPlayer.getPlayer(uuid, offline -> {
			if (offline == null) {
				sender.sendMessage("§cLe joueur " + args[1] + " ne s'est jamais connecté sur le serveur !");
				return;
			}
			
			if (offline.getCooldowns().isEmpty()) {
				sender.sendMessage("§c" + Methods.getName(offline, true) + " n'a aucun cooldown d'actif !");
				return;
			}
			
			if (args.length < 3) {
				showPage(sender, offline, 1);
				return;
			}
			
			Integer page = Methods.toInteger(args[2]);
			
			if (page != null)
				showPage(sender, offline, page);
			else
				showPage(sender, offline, 1);
		});
		
		return true;
	}
	
	private void showPage(CommandSender sender, OfflineAPIPlayer offline, int pageNumber) {
		Multimap<Integer, BaseComponent[]> pages = ArrayListMultimap.create();
		int helps = 0;
		int index = 1;
		
		for (Cooldown cooldown : offline.getCooldowns()) {
			CooldownData data = cooldown.getData(offline.getUUID());
			
			pages.get(index).add(new ComponentBuilder(" §e" + cooldown.getName() + " §7- §e" + LongTime.setYMDWHMSFormat(data.getTimeLeft()) + (data.isPaused() ? " §7[§fPAUSE§7]" : ""))                                      
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a/cooldown remove " + offline.getName() + ' ' + cooldown.getName()).create()))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cooldown remove " + offline.getName() + ' ' + cooldown.getName())).create());
			helps++;
			
			if (helps == MAX_COOLDOWN_PER_PAGE) {
				index++;
				helps = 0;
			}
		}
		
		if (!pages.containsKey(pageNumber)) {
			sender.sendMessage("§cLa page " + pageNumber + " n'existe pas !");
			return;
		}
		
		sender.sendMessage("§7Total de §e" + offline.getCooldowns().size() + ", §7page §e" + pageNumber + '/' + pages.keySet().size());
		for (BaseComponent[] components : pages.get(pageNumber)) 
			Methods.sendJSON(sender, components);
			
	}

}
