package net.onima.onimaapi.commands.essentials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;

public class EnchantCommand implements CommandExecutor, TabCompleter {

	private JSONMessage usage = new JSONMessage("§7/enchant <enchantment> <level>", "§d§oEnchante un item.");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_ENCHANT_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}
		
		if (args.length < 2) {
			usage.send(sender, "§7Utilisation : ");
			return false;
		}
		
		Player player = (Player) sender;
		Enchantment enchantment = Enchantment.getByName(args[0]);
		
        if (enchantment == null) {
            sender.sendMessage("§cL'enchantement '" + args[0] + "' n'existe pas !");
            return false;
        }
        
        ItemStack stack = player.getItemInHand();
       
        if (stack == null || stack.getType() == Material.AIR) {
            sender.sendMessage("§cVous ne portez aucun item !");
            return false;
        }
        
        Integer level = Methods.toInteger(args[1]);
       
        if (level == null) {
            sender.sendMessage("§c" + args[1] + " n'est pas un entier !");
            return false;
        }
		
        stack.addUnsafeEnchantment(enchantment, level);
        sender.sendMessage("Item enchanté avec §7" + enchantment.getName() + ' ' + level + "§f.");
        return true;
	}
	
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
    	if (!OnimaPerm.ONIMAAPI_ENCHANT_COMMAND.has(sender) && args.length != 1) 
    		return Collections.emptyList();
    	
    	List<String> completions = new ArrayList<>();
    	
    	for (Enchantment enchant : Enchantment.values()) {
    		if (StringUtil.startsWithIgnoreCase(enchant.getName(), args[0]))
    			completions.add(enchant.getName());
    	}
    	
    	return completions;
    }

}
