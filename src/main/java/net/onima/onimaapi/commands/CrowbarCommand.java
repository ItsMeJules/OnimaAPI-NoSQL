package net.onima.onimaapi.commands;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.items.Crowbar;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;

public class CrowbarCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.CROWBAR_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		Player receiver;
		
		if (args.length == 0) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("§cSeulement les joueurs peuvent se donner des crowbars !");
				return false;
			}
			
			receiver = (Player) sender;
			sender.sendMessage("§dVous §7avez reçu une " + ConfigurationService.CROWBAR_NAME + "§7.");
		} else {
			receiver = Bukkit.getPlayer(args[0]);
			
			if (receiver == null) {
				sender.sendMessage("§cLe joueur " + args[0] + " n'est pas en ligne !");
				return false;
			}
			
			receiver.sendMessage("§dVous §7avez reçu une " + ConfigurationService.CROWBAR_NAME + "§7.");
			sender.sendMessage("§dVous §7avez donné une " + ConfigurationService.CROWBAR_NAME + "§7 à §d" + Methods.getName(receiver, true) + "§7.");
		}
		
		Crowbar crowbar = new Crowbar();
		
		if (sender instanceof Player)
			crowbar.setGiver(Methods.getRealName(sender));
		
		for (Entry<Integer, ItemStack> entry : receiver.getInventory().addItem(crowbar.getItem()).entrySet())
			receiver.getWorld().dropItemNaturally(receiver.getLocation(), entry.getValue());
			
		return true;
	}

}
