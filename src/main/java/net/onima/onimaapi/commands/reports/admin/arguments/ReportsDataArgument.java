package net.onima.onimaapi.commands.reports.admin.arguments;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.gui.menu.report.admin.ReportStatMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class ReportsDataArgument extends BasicCommandArgument {

	public ReportsDataArgument() {
		super("data", OnimaPerm.REPORTS_DATA_ARGUMENT);
		
		playerOnly = true;
		usage = new JSONMessage("§7/reports " + name + " <player>", "§dAffiche tous les données de report d'un joueur.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 2, true))
			return false;
	
		APIPlayer apiPlayer = APIPlayer.getPlayer((Player) sender);
		UUID uuid = UUIDCache.getUUID(args[1]);
		
		if (uuid == null) {
			sender.sendMessage("§cLe joueur " + args[1] + " n'existe pas !");
			return false;
		}
		
		OfflineAPIPlayer.getPlayer(uuid, offline -> apiPlayer.openMenu(new ReportStatMenu(offline)));
		return true;
	}

}
