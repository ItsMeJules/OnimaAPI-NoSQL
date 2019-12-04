package net.onima.onimaapi.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;

public class SpawnerCommand implements CommandExecutor, TabCompleter {

	private JSONMessage usage = new JSONMessage("§7/spawner <entity>", "§d§oPermet de se donner un spawner.");
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!OnimaPerm.SPAWNER_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		
		if(!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}
		
		APIPlayer apiPlayer = APIPlayer.getPlayer((Player) sender);
		
		
		if (args.length < 1) {
			apiPlayer.sendMessage(usage, "§7Utilisation : ");
			return false;
		}
		
		EntityType type = EntityType.fromName(args[0]);
		
		if (type == null) {
			apiPlayer.sendMessage("§cL'entité " + args[0] + " n'existe pas !");
			return true;
		}
		
		String name = type.getName();
		
		if (apiPlayer.toPlayer().getInventory().addItem(new BetterItem(Material.MOB_SPAWNER, 1, 0, ConfigurationService.SPAWNER_NAME.replace("%spawner%", name), name).toItemStack()).isEmpty()) {
			apiPlayer.sendMessage("§dVous §7avez reçu un spawner à §d" + name + "§7.");
			return true;
		} else {
			apiPlayer.sendMessage("§cVotre inventaire est plein vous n'avez pas pu recevoir de spawner !");
			return false;
		}
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (sender instanceof Player && args.length == 1 && OnimaPerm.SPAWNER_COMMAND.has(sender)) {
			List<String> completions = new ArrayList<>();
			
			for (EntityType type : EntityType.values()) {
				if (type.getName() == null)
					continue;
				
				if (StringUtil.startsWithIgnoreCase(type.getName(), args[0]))
					completions.add(type.getName());
			}
			
			return completions;
		}
		
		return Collections.emptyList();
	}
}
