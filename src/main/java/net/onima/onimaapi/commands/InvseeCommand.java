package net.onima.onimaapi.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.utils.SpecialPlayerInventory;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;

public class InvseeCommand implements CommandExecutor {

	private JSONMessage usage = new JSONMessage("§7/invsee <player>", "§d§oInspecte l'inventaire d'un joueur.");
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_COMMAND_INVSEE.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}
		
		if (args.length == 0) {
			usage.send(sender, "§7Utilisation : ");
			return false;
		}
		
		Player player = (Player) sender;
		OfflinePlayer offline = Bukkit.getOfflinePlayer(args[0]);
		
		if (offline == null || !offline.hasPlayedBefore()) {
			sender.sendMessage("§c" + args[0] + " ne s'est jamais connecté !");
			return false;
		}
		
		player.openInventory(SpecialPlayerInventory.createInventory(offline, offline.isOnline()).getBukkitInventory());
		APIPlayer.getPlayer(player).setExaminating(offline.getUniqueId());
		return true;
	}

}
