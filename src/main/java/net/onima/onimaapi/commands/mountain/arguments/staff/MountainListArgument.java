package net.onima.onimaapi.commands.mountain.arguments.staff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.onima.onimaapi.mountain.utils.Mountain;
import net.onima.onimaapi.mountain.utils.MountainType;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class MountainListArgument extends BasicCommandArgument {

	private static final int MAX_MOUNTAIN_PER_PAGE = 10;
	
	public MountainListArgument() {
		super("list", OnimaPerm.ONIMAAPI_MOUNTAIN_LIST_ARGUMENT);
		usage = new JSONMessage("§7/mountain " + name + " (type) (page)", "§d§oPermet d'afficher la liste des montagnes.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 1, true))
			return false;
		
		MountainType type = args.length > 1 ? MountainType.fromString(args[1]) : null;
		
		if (args.length < 3) {
			showPage(sender, 1, type);
			return true;
		}
		
		Integer page = Methods.toInteger(args[2]);
		
		if (page != null)
			showPage(sender, page, type);
		else
			showPage(sender, 1, type);
		
		return true;
	}
	
	private void showPage(CommandSender sender, int pageNumber, MountainType type) {
		Multimap<Integer, BaseComponent[]> pages = ArrayListMultimap.create();
		int helps = 0;
		int index = 1;
		
		List<Mountain> mountains;
		
		if (type != null)
			mountains = Mountain.getMountains().stream().filter(mountain -> mountain.getType() == type).collect(Collectors.toCollection(() -> new ArrayList<>(6)));
		else
			mountains = Mountain.getMountains();
		
		for (Mountain mountain : mountains) {
			pages.get(index).add(new ComponentBuilder(" §7- §e" + mountain.getType() + " §7ID: §e" + mountain.getName() + "")
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a/mountain show " + mountain.getName()).create()))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mountain show " + mountain.getName())).create());
			helps++;
			
			if (helps == MAX_MOUNTAIN_PER_PAGE) {
				index++;
				helps = 0;
			}
		}
		
		if (!pages.containsKey(pageNumber)) {
			sender.sendMessage("§cLa page " + pageNumber + " n'existe pas !");
			return;
		}
		
		sender.sendMessage("§7Total de §e" + mountains.size() + ", §7page §e" + pageNumber + '/' + pages.keySet().size());
		for (BaseComponent[] components : pages.get(pageNumber)) 
			Methods.sendJSON(sender, components);
			
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2 && OnimaPerm.ONIMAAPI_MOUNTAIN_LIST_ARGUMENT.has(sender)) {
			List<String> completions = new ArrayList<>();
			
			for (MountainType type : MountainType.values()) {
				if (StringUtil.startsWithIgnoreCase(type.name(), args[1]));
					completions.add(type.name());
			}
			
			return completions;
		}
		
		return Collections.emptyList();
	}

}
