package net.onima.onimaapi.commands.essentials;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;

public class RepairCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.REPAIR_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}
		
		Player player = (Player) sender;
		
		if (args.length < 1) {
			ItemStack item = player.getItemInHand();
			
			if (item == null || item.getType().isBlock() || item.getDurability() == 0 || item.getType().getMaxDurability() < 1) {
				sender.sendMessage("§cL'item que vous tenez n'est pas à réparer.");
				return false;
			}
			
			sender.sendMessage("§7L'item que vous tenez a été §eréparé §7!");
			item.setDurability((short) 0);
			return true;
		} else if (args[0].equalsIgnoreCase("all")) {
			for (ItemStack item : player.getInventory().getContents()) {
				if (item == null || item.getType().isBlock() || item.getDurability() == 0 || item.getType().getMaxDurability() < 1)
					continue;
				
				item.setDurability((short) 0);
			}
			
			for (ItemStack item : player.getInventory().getArmorContents()) {
				if (item == null || item.getType().isBlock() || item.getDurability() == 0 || item.getType().getMaxDurability() < 1)
					continue;
				
				item.setDurability((short) 0);
			}
			
			sender.sendMessage("§7Tous vos items ont été §eréparés §7!");
			return true;
		}
		
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.REPAIR_COMMAND.has(sender) || args.length != 1)
			return Collections.emptyList();
		
		return Arrays.asList("all");
	}
}
