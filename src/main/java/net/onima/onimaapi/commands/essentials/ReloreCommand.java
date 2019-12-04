package net.onima.onimaapi.commands.essentials;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;

public class ReloreCommand implements CommandExecutor {

	private JSONMessage usage = new JSONMessage("§7/rename <name>", "§d§oChange le nom d'un item.");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_RELORE_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}
		
		if (args.length < 1) {
			usage.send(sender, "§7Utilisation : ");
			return false;
		}
		
		Player player = (Player) sender;
		ItemStack hand = player.getItemInHand();
		boolean bypass = OnimaPerm.ONIMAAPI_RENAME_COMMAND_BYPASS.has(sender);
		
		if (hand == null || hand.getType() == Material.AIR) {
			sender.sendMessage("§cVous ne tenez aucun item en main !");
			return false;
		}
		
		ItemMeta meta = hand.getItemMeta();
		List<String> lore = meta.getLore();
		List<String> newLore = new ArrayList<>();
		
		if (!args[0].equalsIgnoreCase("none") && !args[0].equalsIgnoreCase("null")) {
			String[] joined = Methods.colors(StringUtils.join(args, ' ', 0, args.length)).split(",");
			
			for (int i = 0; i < joined.length; i++)
				newLore.add(joined[i]);
		}
		
		if ((lore == null || lore.isEmpty()) && newLore.isEmpty()) {
            sender.sendMessage("§cL'item que vous tenez n'a pas de lore !");
            return false;
        }	
		
		
        if (lore != null && !lore.isEmpty() && newLore.equals(lore)) {
            sender.sendMessage("§cL'item que vous tenez a déjà ce lore !");
            return false;
        }
        
        if (!newLore.isEmpty() && !bypass) {
            for (String word : RenameCommand.DISALLOWED) {
            	for (String str : newLore) {
            		String lower = str.toLowerCase();
            		
                    if (lower.contains(word)) {
                        sender.sendMessage("§cCe lore d'item peut-être offensif, vous avez été warn.");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "warn " + Methods.getRealName(sender) + " Relore d'item innaproprié");
                        return false;
                    }
            	}
            }
        }
        
        meta.setLore(newLore);
        hand.setItemMeta(meta);
        
        if (newLore.isEmpty()) {
            sender.sendMessage("§6Vous avez supprimé le lore.");
            return true;
        }
        
        sender.sendMessage("§6Vous avez relore l'item.");
		return true;
	}

}
