package net.onima.onimaapi.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.md_5.bungee.api.chat.ClickEvent;
import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.WorldChanger;

public class WorldChangerCommand implements CommandExecutor, TabCompleter {

	private JSONMessage travelUsage = new JSONMessage("§7/worldchanger <fromWorld> <toWorld> travel", "§d§oActive ou désactive le changement de monde par défaut.");
	private JSONMessage locationUsage = new JSONMessage("§7/worldchanger <fromWorld> <toWorld> location", "§d§oDéfinit la location de téléportation au changement de monde.");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_WORLDCHANGER_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}
		
		if (args.length < 3) {
			if (args.length == 0 || !args[0].equalsIgnoreCase("travel") || !args[0].equalsIgnoreCase("location"))
				help(sender);
			else if (args[0].equalsIgnoreCase("travel"))
				travelUsage.send(sender, "§7Utilisation : ");
			else if (args[0].equalsIgnoreCase("location"))
				locationUsage.send(sender, "§7Utilisation : ");
			
			return false;
		}
		
		if (Bukkit.getWorld(args[0]) == null) {
			sender.sendMessage("§cLe monde " + args[0] + " n'existe pas !");
			return false;
		} else if (Bukkit.getWorld(args[1]) == null) {
			sender.sendMessage("§cLe monde " + args[1] + " n'existe pas !");
			return false;
		}
		
		WorldChanger changer = WorldChanger.getChanger(args[0], args[1]);
	
		if (changer == null) {
			changer = new WorldChanger(args[0], args[1]);
			WorldChanger.getWorldChangers().add(changer);
		}
		
		
		if (args[2].equalsIgnoreCase("travelagent")) {
			changer.setUseTravelAgent(!changer.shouldUseTravelAgent());
			String msg = changer.shouldUseTravelAgent() ? "§aactivé" : "§cdésactivé";
			JSONMessage message = new JSONMessage("§e" + Methods.getRealName(sender) + " §7a " + msg + " §7le travel agent de §e" + changer.getFromWorldName() + " §7à §e" + changer.getToWorldName() + "§7.", "§oCliquez pour annuler le changement.");
			
			message.setClickAction(ClickEvent.Action.RUN_COMMAND);
			message.setClickString("/worldchanger " + args[0] + ' ' + args[1] + " travel");
			
			OnimaAPI.broadcast(message, OnimaPerm.ONIMAAPI_WORLDCHANGER_COMMAND);
		} else if (args[2].equalsIgnoreCase("location")) {
			Location location = ((Player) sender).getLocation();
			
			changer.setSpawnLocation(location);
			
			OnimaAPI.broadcast("§e" + Methods.getRealName(sender) + " §7a défini le spawn lors du changement de monde en §e" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + " §7de §e" + changer.getFromWorldName() + " §7à §e" + changer.getToWorldName() + "§7.", OnimaPerm.ONIMAAPI_WORLDCHANGER_COMMAND);
		}
		
		return true;
	}

	private void help(CommandSender sender) {
		sender.sendMessage("§e" + ConfigurationService.STAIGHT_LINE);
		locationUsage.send(sender);
		travelUsage.send(sender);
		sender.sendMessage("§e" + ConfigurationService.STAIGHT_LINE);
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_WORLDCHANGER_COMMAND.has(sender))
			return Collections.emptyList();
		
		List<String> completions = new ArrayList<>();

		if (args.length > 0 && args.length < 3)
			completions.addAll(Bukkit.getWorlds().parallelStream().map(World::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args.length == 1 ? args[0] : args[1])).collect(Collectors.toList()));
		else if (args.length == 3) {
			if (StringUtil.startsWithIgnoreCase("location", args[2]))
				completions.add("location");
			
			if (StringUtil.startsWithIgnoreCase("travelagent", args[2]))
				completions.add("travelagent");
		}
		
		return completions;
	}
	
}
