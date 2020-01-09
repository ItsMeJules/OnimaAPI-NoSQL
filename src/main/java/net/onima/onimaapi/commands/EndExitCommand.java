package net.onima.onimaapi.commands;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.items.Wand;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.WorldChanger;
import net.onima.onimaapi.zone.Cuboid;

public class EndExitCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ENDEXIT_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}
		
		APIPlayer apiPlayer = APIPlayer.getPlayer((Player) sender);
		Wand wand = apiPlayer.getWand();

		if (!wand.hasAllLocationsSet()) {
			sender.sendMessage("§cVous devez sélectionner une zone !");
			sender.sendMessage("  §d§oLocation §7manquante : §d§on°" + (wand.getLocation1() == null ? "1" : "2"));
			return false;
		}
		
		Cuboid cuboid = new Cuboid(wand.getLocation1(), wand.getLocation2(), true);
		
		if (cuboid.getYLength() > 1) {
			sender.sendMessage("§cLa sortie de l'end ne peut pas dépasser les 1 de hauteur.");
			return false;
		}
		
		for (Block block : cuboid) {
			switch (block.getType()) {
			case WATER:
			case STATIONARY_WATER:
				break;

			default:
				sender.sendMessage("§cLes blocks doivent être de l'eau.");
				return false;
			}
		}
		
		sender.sendMessage("§6Sortie de l'end défini !");
		WorldChanger.setEndExit(cuboid);
		return true;
	}

}
