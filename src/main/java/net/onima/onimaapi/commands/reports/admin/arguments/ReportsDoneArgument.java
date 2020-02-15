package net.onima.onimaapi.commands.reports.admin.arguments;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.gui.menu.report.ReportsMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class ReportsDoneArgument extends BasicCommandArgument {

	public ReportsDoneArgument() {
		super("done", OnimaPerm.REPORTS_DONE_ARGUMENT);
		
		playerOnly = true;
		usage = new JSONMessage("§7/reports " + name, "§d§oPermet d'afficher les reports traités.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 1, true))
			return false;
	
		APIPlayer.getPlayer((Player) sender).openMenu(new ReportsMenu(true));
		return true;
	}

}
