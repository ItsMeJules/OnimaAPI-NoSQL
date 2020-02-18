package net.onima.onimaapi.commands.reports.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.commands.reports.admin.arguments.ReportsDataArgument;
import net.onima.onimaapi.commands.reports.admin.arguments.ReportsDoneArgument;
import net.onima.onimaapi.commands.reports.admin.arguments.ReportsIdArgument;
import net.onima.onimaapi.commands.reports.admin.arguments.ReportsUserArgument;
import net.onima.onimaapi.gui.menu.report.admin.ReportsMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.commands.ArgumentExecutor;

public class ReportsExecutor extends ArgumentExecutor {

	public ReportsExecutor() {
		super("reports", OnimaPerm.REPORTS_COMMAND);

		addArgument(new ReportsDataArgument());
		addArgument(new ReportsDoneArgument());
		addArgument(new ReportsIdArgument());
		addArgument(new ReportsUserArgument());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return super.onCommand(sender, command, label, args);
		
		if (args.length == 0) {
			new ReportsMenu(false).open(APIPlayer.getPlayer((Player) sender));
			return true;
		} else
			return super.onCommand(sender, command, label, args);
	}
	
}
