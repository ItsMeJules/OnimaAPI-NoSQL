package net.onima.onimaapi.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.mod.ModItem;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;

public class FreezeCommand implements CommandExecutor {
	
	private JSONMessage usage;
	
	{
		usage = new JSONMessage("§7/freeze <player>", "§dFreeze un joueur");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.FREEZE_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (args.length < 1) {
			usage.send(sender, "§7Utilisation : ");
			return false;
		}
		
		OfflineAPIPlayer.getPlayer(args[0], offline -> {
			if (offline == null) {
				sender.sendMessage(ModItem.MOD_PREFIX + " §cLe joueur " + args[0] + " ne s'est jamais connecté !");
				return;
			}
			
			String frozen = !offline.isFrozen() ? "§cfreeze" : "§adéfreeze";
			
			if (offline.isOnline()) {
				APIPlayer player = (APIPlayer) offline;
				
				player.setFrozen(!offline.isFrozen());
				player.sendMessage(APIPlayer.getPlayer((Player) sender).getColoredName(true) + " §fvous a " + frozen + " §f!");

				if (!offline.isFrozen())
					player.closeMenu();
			} else
				offline.setFrozen(!offline.isFrozen());
			
			
			sender.sendMessage(ModItem.MOD_PREFIX + " §cVous §favez " + frozen + " §f" + Methods.getName(offline, true) + " §f!");
		});
		
		return true;
	}

}
