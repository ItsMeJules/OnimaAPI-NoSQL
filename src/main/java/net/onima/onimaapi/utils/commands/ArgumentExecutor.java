package net.onima.onimaapi.utils.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;

public abstract class ArgumentExecutor implements CommandExecutor, TabCompleter {
	
	private List<BasicCommandArgument> arguments;
	private String label;
	private OnimaPerm permission;
	
	{
		arguments = new ArrayList<>();
	}
	
	public ArgumentExecutor(String label, OnimaPerm permission) {
		this.label = label;
		this.permission = permission;
	}
	
	public boolean existArgument(BasicCommandArgument argument) {
		return arguments.contains(argument);
	}
	
	public void addArgument(BasicCommandArgument argument) {
		arguments.add(argument);
	}
	
	public void removeArgument(BasicCommandArgument argument) {
		arguments.remove(argument);
	}
	
	public BasicCommandArgument getArgument(String label) {
		for (BasicCommandArgument bca : this.arguments) {
			if (bca.getName().equalsIgnoreCase(label) || Arrays.asList(bca.getAliases()).contains(label))
				return bca;
		}
		return null;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1) {
			if (permission != null && !permission.has(sender)) {
				sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
				return false;
			}
			
			sender.sendMessage("§e" + ConfigurationService.STAIGHT_LINE);
			sender.sendMessage("§d§o" + WordUtils.capitalizeFully(label + "§e" + " - " + "§7" + "(Page 1/1)"));
			
			for (BasicCommandArgument argument : arguments) {
				OnimaPerm permission = argument.getPermission();
				
				if (permission == null || permission.has(sender))
					argument.usage.send(sender);
			}
			
			sender.sendMessage("§e" + ConfigurationService.STAIGHT_LINE);
			return true;
		}
		
		BasicCommandArgument argument2 = getArgument(args[0]);
		OnimaPerm permission2 = (argument2 == null) ? null : argument2.getPermission();
		
		if (argument2 == null || (permission2 != null && !permission2.has(sender))) {
			sender.sendMessage("§cLa sous-commande " + args[0] + " n'existe pas.");
			return false;
		}
		
		argument2.onCommand(sender, command, label, args);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> completions = new ArrayList<>();
		
		if (args.length < 2) {
			for (BasicCommandArgument bca : arguments) {
				OnimaPerm permission = bca.getPermission();
				
				if (permission != null && !permission.has(sender)) continue;
					
					String name = bca.getName();
					
					if (StringUtil.startsWithIgnoreCase(name, args[0]))
						completions.add(name);
					
					if (args[0].isEmpty()) continue;
					
					for (String alias : bca.getAliases()) {
						if (StringUtil.startsWithIgnoreCase(alias, args[0]))
							completions.add(alias);
					}
			}
		} else {
			BasicCommandArgument argument = getArgument(args[0]);
			
			if (argument == null) return completions;
			
			OnimaPerm permission = argument.getPermission();
			
			if (permission == null || permission.has(sender))
				completions = argument.onTabComplete(sender, cmd, label, args);
		}
		return completions;
	}

	public List<BasicCommandArgument> getArguments() {
		return arguments;
	}

	public String getLabel() {
		return label;
	}
	
}
