package net.onima.onimaapi.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;

public class CondenseCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}
		
		if (!OnimaPerm.CONDENSE_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		Player player = ((Player) sender);
        PlayerInventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getContents();
        int done = 0;
        
        for (int i = 0; i < contents.length; i++) {
            ItemStack current = contents[i];
            
            if (current != null) {
                for (int i2 = i + 1; i2 < contents.length; i2++) {
                    ItemStack current2 = contents[i2];
                    
                    if (current.isSimilar(current2)) {
                        int allowed = current.getMaxStackSize() - current.getAmount();
                    
                        if (allowed > 0) {
                            int left = current2.getAmount() - allowed;
                        
                            if (left > 0) {
                                current2.setAmount(left);
                                current.setAmount(current.getMaxStackSize());
                            } else {
                                done++;
                                current.setAmount(current.getAmount() + current2.getAmount());
                                contents[i2] = null;
                            }
                        }
                    }
                }
            }
        }
        
        inventory.setContents(contents);
        player.updateInventory();
        sender.sendMessage(done == 0 ? "§cVous n'avez pas d'items à condenser !" : "§dVous §7avez condensé §d" + done + " §7item" + (done > 1 ? "s" : "") + '.');
        return true;
	}

}
