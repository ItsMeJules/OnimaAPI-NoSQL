package net.onima.onimaapi.commands.essentials;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Sets;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;

public class RenameCommand implements CommandExecutor {
	
	public static final List<String> DISALLOWED = Arrays.asList("Hitler","卍","jews","nigger", "n1gger", "faggot", "queer", "paki", "slut", "nègre", "pute", "enculé", "enculer", "pd", "salope", "connard", "mongolien");
	public static final Set<Material> DISALLOWED_ITEMS = Sets.newHashSet(Material.PAPER, Material.DISPENSER, Material.NAME_TAG, Material.MONSTER_EGG, Material.MONSTER_EGGS, Material.INK_SACK);
	 
	private JSONMessage usage = new JSONMessage("§7/rename <name>", "§d§oChange le nom d'un item.");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_RENAME_COMMAND.has(sender)) {
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
		
        if (DISALLOWED_ITEMS.contains(hand.getType()) && !bypass) {
            sender.sendMessage("§cVous ne pouvez pas renommer cet item !");
            return false;
        }
		
		ItemMeta meta = hand.getItemMeta();
		String oldName = meta.getDisplayName();

		if (oldName != null)
			oldName = oldName.trim();

		String newName;
		
		if (!args[0].equalsIgnoreCase("none") && !args[0].equalsIgnoreCase("null"))
        	newName = Methods.colors(StringUtils.join(args, ' ', 0, args.length));
        else 
            newName = null;

		if (oldName == null && newName == null) {
            sender.sendMessage("§cL'item que vous tenez n'a pas de nom !");
            return false;
        }
		
        if (oldName != null && oldName.equals(newName)) {
            sender.sendMessage("§cL'item que vous tenez a déjà ce nom !");
            return false;
        }
        
        if (newName != null && !bypass) {
            String lower = newName.toLowerCase();
            
            for (String word : DISALLOWED) {
                if (lower.contains(word)) {
                    sender.sendMessage("§cCe nom d'item peut-être offensif, vous avez été warn.");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "warn " + Methods.getRealName(sender) + " Nom de rename innaproprié");
                    return false;
                }
            }
        }
        
        meta.setDisplayName(newName);
        hand.setItemMeta(meta);
        
        if (newName == null) {
            sender.sendMessage("§6Vous avez supprimé le nom " + oldName + '.');
            return true;
        }
        
        sender.sendMessage("§6Vous avez renommé l'item " + ((oldName == null) ? "sans nom" : oldName) + " en " + newName + "§6.");
		return true;
	}

}
