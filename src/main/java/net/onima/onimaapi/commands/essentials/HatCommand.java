package net.onima.onimaapi.commands.essentials;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;

public class HatCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.HAT_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cCette commande est seulement utilisable par les joueurs !");
			return false;
		}
		
		Player player = (Player) sender;
		ItemStack item = player.getItemInHand();
		
		if (item == null || item.getType() == Material.AIR) {
			sender.sendMessage("§cVous devez avoir un item dans la main !");
			return false;
		}
		
		player.getInventory().setHelmet(item);
		player.setItemInHand(null);
		player.sendMessage("§7Vous avez un nouveau chapeau !");
		return true;
	}

}
