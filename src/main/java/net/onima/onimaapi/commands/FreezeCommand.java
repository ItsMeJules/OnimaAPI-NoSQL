package net.onima.onimaapi.commands;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.caching.UUIDCache;
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
		
		UUID uuid = UUIDCache.getUUID(args[0]);
		
		if (uuid == null) {
			sender.sendMessage(ModItem.MOD_PREFIX + " §cLe joueur " + args[0] + " ne s'est jamais connecté !");
			return false;
		}
		
		OfflineAPIPlayer.getPlayer(uuid, offline -> {
			String frozen = !offline.isFrozen() ? "§cfreeze" : "§adéfreeze";
			
			if (offline.isOnline()) {
				APIPlayer player = (APIPlayer) offline;
				
				player.setFrozen(!offline.isFrozen());
				player.sendMessage(Methods.getName(sender) + " §fvous a " + frozen + " §f!");

				if (!offline.isFrozen())
					player.closeMenu();
			} else
				offline.setFrozen(!offline.isFrozen());
			
			
			sender.sendMessage(ModItem.MOD_PREFIX + " §cVous §favez " + frozen + " §f" + Methods.getName(offline, true) + " §f!");
		});
		
		return true;
	}

}
