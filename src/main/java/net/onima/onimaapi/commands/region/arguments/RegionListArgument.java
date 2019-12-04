package net.onima.onimaapi.commands.region.arguments;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;
import net.onima.onimaapi.zone.type.Region;

public class RegionListArgument extends BasicCommandArgument {
	
	private static final int MAX_REGION_PER_PAGE = 10;

	public RegionListArgument() {
		super("list", OnimaPerm.ONIMAAPI_REGION_LIST_ARGUMENT);
		usage = new JSONMessage("§7/region " + name + " (page)", "§d§oAffiche la liste des régions.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 1, true))
			return false;
		
		if (args.length < 2) {
			showPage(sender, 1);
			return true;
		}
		
		Integer page = Methods.toInteger(args[2]);
		
		if (page != null)
			showPage(sender, page);
		else
			showPage(sender, 1);
		
		return true;
	}
	
	private void showPage(CommandSender sender, int pageNumber) {
		Multimap<Integer, BaseComponent[]> pages = ArrayListMultimap.create();
		int helps = 0;
		int index = 1;
		
		for (Region region : Region.getRegions()) {
			pages.get(index).add(new ComponentBuilder(" §7- §e" + region.getName() + " §7Nom d'affichage : §e" + region.getDisplayName(sender) + "")
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a/region show " + region.getName()).create()))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/region show " + region.getName())).create());
			helps++;
			
			if (helps == MAX_REGION_PER_PAGE) {
				index++;
				helps = 0;
			}
		}
		
		if (!pages.containsKey(pageNumber)) {
			sender.sendMessage("§cLa page " + pageNumber + " n'existe pas !");
			return;
		}
		
		sender.sendMessage("§7Total de §e" + Region.getRegions().size() + ", §7page §e" + pageNumber + '/' + pages.keySet().size());
		for (BaseComponent[] components : pages.get(pageNumber)) 
			Methods.sendJSON(sender, components);
			
	}
	
}
