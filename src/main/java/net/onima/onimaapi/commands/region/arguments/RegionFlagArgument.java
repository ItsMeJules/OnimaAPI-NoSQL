package net.onima.onimaapi.commands.region.arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import com.google.common.collect.Sets;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;
import net.onima.onimaapi.zone.struct.Flag;
import net.onima.onimaapi.zone.type.Region;

public class RegionFlagArgument extends BasicCommandArgument {
	
	private static final Set<String> ARGS;
	
	static {
		ARGS = Sets.newHashSet("list", "add", "remove");
	}

	public RegionFlagArgument() {
		super("flag", OnimaPerm.ONIMAAPI_REGION_FLAG_ARGUMENT);
		usage = new JSONMessage("§7/region " + name + " <region> <add | list | remove> <flag>", "§d§oPermet de gérer les flags.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 3, true))
			return false;
		
		Region region = null;
		
		if ((region = Region.getByName(args[1])) == null) {
			sender.sendMessage("§cLa région " + args[1] + " n'existe pas !");
			return false;
		}
		
		if (args[2].equalsIgnoreCase("list")) {
			sender.sendMessage("§7Flags de la région §d" + region.getDisplayName(sender) + " §7:");
			ComponentBuilder builder = new ComponentBuilder("");
			
			for (Flag flag : region.getFlags()) {
				builder.append("§e" + flag.getName());
				builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cSupprimer ce flag.").create()));
				builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/region flag " + region.getName() + " remove " + flag.getName()));
				builder.append(", ", FormatRetention.NONE);
			}
			
			Methods.sendJSON(sender, builder.create());
			return true;
		}
		
		if (args.length >= 4) {
			Flag flag = Flag.fromName(args[3]);
			
			if (flag == null) {
				sender.sendMessage("§cLe flag " + args[3] + " n'existe pas !");
				return false;
			}
			
			if (args[2].equalsIgnoreCase("add")) {
				
				if (region.hasFlag(flag)) {
					sender.sendMessage("§cLa région " + region.getDisplayName(sender) + " a déjà le flag '" + flag.getName() + "'.");
					return false;
				}
				
				region.addFlag(flag);
				sender.sendMessage("§dVous §7avez ajouté le flag §d" + flag.getName() + " §7a la région §d" + region.getDisplayName(sender) + "§7.");
				return true;
			} else if (args[2].equalsIgnoreCase("remove")) {
				
				if (!region.hasFlag(flag)) {
					sender.sendMessage("§cLa région " + region.getDisplayName(sender) + " n'a pas le flag '" + flag.getName() + "'.");
					return false;
				}
				
				region.removeFlag(flag);
				sender.sendMessage("§dVous §7avez supprimé le flag §d" + flag.getName() + " §7de la région §d" + region.getDisplayName(sender) + "§7.");
				return true;
			}
		}
		
		usage.send(sender, "§7Utilisation : ");
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 2 && !OnimaPerm.ONIMAAPI_REGION_FLAG_ARGUMENT.has(sender))
			return Collections.emptyList();
		
		List<String> completions = new ArrayList<>();
		
		if (args.length == 2) {
			for (Region region : Region.getRegions()) {
				if (StringUtil.startsWithIgnoreCase(region.getName(), args[1]))
					completions.add(region.getName());
			}
		} else if (args.length == 3) {
			for (String arg : ARGS) {
				if (StringUtil.startsWithIgnoreCase(arg, args[2]))
					completions.add(arg);
			}
		} else if (args.length == 4) {
			Region region = Region.getByName(args[1]);
			
			if (region != null) {
				for (Flag flag : Flag.values()) { //TODO Optimize
					if(region.hasFlag(flag) && args[2].equalsIgnoreCase("add")) continue;
					if(!region.hasFlag(flag) && args[2].equalsIgnoreCase("remove")) continue;
					
					if (StringUtil.startsWithIgnoreCase(flag.getName(), args[3]))
						completions.add(flag.getName());
				}
			}
		}
			
		return completions;
	}

}
