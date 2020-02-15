package net.onima.onimaapi.commands.reports.admin.arguments;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.gui.menu.report.ReportInfoMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.report.Report;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class ReportsIdArgument extends BasicCommandArgument {

	public ReportsIdArgument() {
		super("id", OnimaPerm.REPORTS_HASHTAG_ARGUMENT);
		
		playerOnly = true;
		usage = new JSONMessage("§7/report " + name + " <id>", "§dPermet d'afficher les détails d'un report.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 2, true))
			return false;
	
		Integer id = Methods.toInteger(args[1]);
		
		if (id == null) {
			sender.sendMessage(args[1] + " n'est pas valide !");
			return false;
		}
		
		Report report = null;
		
		for (Report r : Report.getReports()) {
			if (r.getId() == id) {
				report = r;
				break;
			}
		}
		
		if (report == null) {
			sender.sendMessage("§cAucun signalement n'a été trouvé avec l'identifiant #" + id);
			return false;
		}
		
		APIPlayer.getPlayer((Player) sender).openMenu(new ReportInfoMenu(report));
		return true;
	}

}
