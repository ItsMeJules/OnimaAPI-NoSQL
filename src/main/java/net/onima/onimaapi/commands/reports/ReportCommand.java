package net.onima.onimaapi.commands.reports;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.gui.menu.report.ReportReasonMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.report.PlayerReport;
import net.onima.onimaapi.report.Report;

public class ReportCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.REPORT_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}
		
		APIPlayer reporter = APIPlayer.getPlayer((Player) sender);
		UUID reported = UUIDCache.getUUID(args[0]);
		
		if (reported == null) {
			sender.sendMessage("§c" + args[0] + " ne s'est jamais connecté sur le serveur !");
			return false;
		}
		
		if (args.length == 1) {
			reporter.openMenu(new ReportReasonMenu(reporter.getUUID(), reported));
			return true;
		} else {
			Report report = new PlayerReport(reporter.getUUID(), StringUtils.join(args, ' ', 1, args.length), reported);
			
			if (report.getReason().length() < Report.MIN_REPORT_REASON_LENGTH) {
				sender.sendMessage("§cLa raison du report est trop courte !");
				return false;
			}
			
			report.execute();
		}
		
		return true;
	}

}
