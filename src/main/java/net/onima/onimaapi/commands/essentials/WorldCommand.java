package net.onima.onimaapi.commands.essentials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.WorldChanger;

public class WorldCommand implements CommandExecutor, TabCompleter {
	
	private JSONMessage tpUsage = new JSONMessage("§7/world tp <world> (spawn)", "§d§oTéléporte de monde en monde.");
	private JSONMessage listUsage = new JSONMessage("§7/world list", "§d§oAffiche tous les mondes.");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_WORLD_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}

		if (args.length < 1) {
			help(sender);
			return false;
		}
		
		if (args[0].equalsIgnoreCase("list")) {
			sender.sendMessage("§7Liste des mondes :");
			for (World world : Bukkit.getWorlds())
				new JSONMessage("  §7- §e" + world.getName(), "§o/world tp " + world.getName() + " spawn", true, "/world tp " + world.getName() + " spawn").send(sender);

			return true;
		} else if (args[0].equalsIgnoreCase("tp")) {
			if (args.length < 2) {
				tpUsage.send(sender, "§7Utilisation : ");
				return false;
			}
			
			World world = Bukkit.getWorld(args[1]);
			
			if (world == null) {
				sender.sendMessage("§cLe monde " + args[0] + " n'existe pas !");
				return false;
			}
			
			Player player = (Player) sender;
			Location location = player.getLocation();
			
			if (location.getWorld().getUID().equals(world.getUID())) {
				sender.sendMessage("§cVous êtes déjà dans le monde " + world.getName());
				return false;
			}
			
			Location tpLocation = null;
			
			if (args.length >= 3 && args[2].equalsIgnoreCase("spawn")) {
				WorldChanger changer = WorldChanger.getChanger(location.getWorld().getName(), world.getName());
				tpLocation = changer != null ? changer.getSpawnLocation() : world.getSpawnLocation();
			} else
				tpLocation = new Location(world, location.getX(), world.getHighestBlockYAt(location), location.getZ(), location.getYaw(), location.getPitch());
			
			player.teleport(tpLocation);
			sender.sendMessage("§dVous §7avez été téléporté dans le monde §d" + world.getName());
			return true;
		}
		
		help(sender);
		return false;
	}
	
	private void help(CommandSender sender) {
		sender.sendMessage("§e" + ConfigurationService.STAIGHT_LINE);
		tpUsage.send(sender);
		listUsage.send(sender);
		sender.sendMessage("§e" + ConfigurationService.STAIGHT_LINE);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_WORLD_COMMAND.has(sender))
			return Collections.emptyList();
		
		List<String> completions = new ArrayList<>();
		
		if (args.length == 1) {
			if (StringUtil.startsWithIgnoreCase("tp", args[0]))
				completions.add("tp");
			
			if (StringUtil.startsWithIgnoreCase("list", args[0]))
				completions.add("list");
		} else if (args.length == 2 && args[0].equalsIgnoreCase("tp")) {
			for (World world : Bukkit.getWorlds()) {
				if (StringUtil.startsWithIgnoreCase(world.getName(), args[1]))
					completions.add(world.getName());
			}
		} else if (args.length == 3 && args[0].equalsIgnoreCase("tp")) {
			if (StringUtil.startsWithIgnoreCase("spawn", args[2]))
				completions.add("spawn");
		}
		
		return completions;
	}
	
}
