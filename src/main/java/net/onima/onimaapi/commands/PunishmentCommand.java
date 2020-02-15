package net.onima.onimaapi.commands;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.gui.menu.PunishmentsMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.Methods;

public class PunishmentCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}
		
		if (!OnimaPerm.ONIMAAPI_PUNISHMENT_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		APIPlayer apiPlayer = APIPlayer.getPlayer((Player) sender);
		
		if (args.length < 1) {
			apiPlayer.openMenu(new PunishmentsMenu(apiPlayer, OnimaPerm.ONIMAAPI_PUNISHMENT_COMMAND.has(sender)));
			return true;
		}
		
		if (!OnimaPerm.ONIMAAPI_PUNISHMENT_SELF_COMMAND.has(sender)) {
			sender.sendMessage("§cVous ne pouvez pas voir les punitions d'autres joueurs !");
			return false;
		}
		
		UUID uuid = UUIDCache.getUUID(args[0]);
		
		if (uuid == null) {
			sender.sendMessage("§cLe joueur " + args[0] + " ne s'est jamais connecté sur le serveur !");
			return false;
		}
		
		OfflineAPIPlayer.getPlayer(uuid, offline -> {
			sender.sendMessage("§2Vous §favez ouvert le menu des punitions de §2" + Methods.getName(offline, true));
			apiPlayer.openMenu(new PunishmentsMenu(offline, true));
		});
		
		return true;
	}

}
