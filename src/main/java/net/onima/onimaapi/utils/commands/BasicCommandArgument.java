package net.onima.onimaapi.utils.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;

public abstract class BasicCommandArgument {
	
	protected String name;
	protected JSONMessage usage;
	protected boolean playerOnly;
	protected OnimaAPI plugin;
	protected OnimaPerm permission;
	protected String[] aliases;
	
	{
		plugin = OnimaAPI.getInstance();
	}
	
	public BasicCommandArgument(String name) {
		this(name, (OnimaPerm) null);
	}

	public BasicCommandArgument(String name, OnimaPerm permission) {
		this(name, permission, new String[0]);
	}

	public BasicCommandArgument(String name, String[] aliases) {
		this(name, null, aliases);
	}

	public BasicCommandArgument(String name, OnimaPerm permission, String[] aliases) {
		this.playerOnly = false;
		this.name = name;
		this.permission = permission;
		this.aliases = aliases;
	}
	
	public String getName() {
		return name;
	}
	
	public JSONMessage getUsage() {
		return usage;
	}
	
	public void setUsage(JSONMessage usage) {
		this.usage = usage;
	}

	public boolean isPlayerOnly() {
		return playerOnly;
	}

	public OnimaPerm getPermission() {
		return permission;
	}

	public String[] getAliases() {
		return aliases;
	}
	
//	public abstract boolean goodUsage(CommandSender sender, String label, String[] args, int length);
	public abstract boolean onCommand(CommandSender sender, Command cmd, String label, String[] args);
	
	public boolean checks(CommandSender sender, String[] args, int neededArgs, boolean sendMessage) {
		if (playerOnly && !(sender instanceof Player)) {
			if (sendMessage)
				sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return true;
		}
		
		if (sender instanceof Player && permission != null && !permission.has(sender)) {
			if (sendMessage)
				sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return true;
		}
		
		if (args.length < neededArgs) {
			if (sendMessage)
				usage.send(sender, "§7Utilisation : ");
			return true;
		}
		
		return false;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			List<String> completions = new ArrayList<>();
			
			if (StringUtil.startsWithIgnoreCase(name, args[1]))
				completions.add(name);
				
			if (args[0].isEmpty()) return completions;
			
			for (String alias : aliases) {
				if (StringUtil.startsWithIgnoreCase(alias, args[0]))
					completions.add(alias);
			}
			
			return completions;
		} else
			return APIPlayer.getOnlineAPIPlayers().parallelStream()
					.map(apiPlayer -> {
						if (!OnimaPerm.ONIMAAPI_DISGUISE_COMMAND.has(sender))
							return apiPlayer.getDisplayName();
						else
							return apiPlayer.getName();
				}).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
	}

}
