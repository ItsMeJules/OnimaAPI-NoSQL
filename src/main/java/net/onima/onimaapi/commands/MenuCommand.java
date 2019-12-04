package net.onima.onimaapi.commands;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;

public class MenuCommand implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}
		
		if (!OnimaPerm.MENU_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		Player player = (Player) sender;
		
		if (args.length < 1) {
			player.sendMessage("§7Utilisation : /menu <name>");
			return false;
		}
		
		PacketMenu menu = PacketMenu.getMenu(args[0]);
		APIPlayer apiPlayer = APIPlayer.getPlayer(player);
		
		if (menu == null && (menu = apiPlayer.getMenu(args[0])) == null) {
			player.sendMessage("§cLe menu " + args[0] + " n'existe pas !");
			return false;
		}
		
		if (menu.getPermission() != null && !menu.getPermission().has(player)) {
			player.sendMessage("§cVous n'avez pas la permission d'ouvrir le menu " + menu.getId() + " !");
			return false;
		}
		
		apiPlayer.openMenu(menu);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length != 1 && !(sender instanceof Player))
			return Collections.emptyList();
		
		return Stream.concat(PacketMenu.getStaticMenus().stream(), APIPlayer.getPlayer((Player) sender).getMenus().stream())
				.filter(menu -> { 
					if (menu.getPermission() != null)
						return menu.getPermission().has(sender);
					else
						return true;
				})
				.map(PacketMenu::getId)
				.filter(name -> StringUtil.startsWithIgnoreCase(name, args[0])).collect(Collectors.toList());
	}
	
}
