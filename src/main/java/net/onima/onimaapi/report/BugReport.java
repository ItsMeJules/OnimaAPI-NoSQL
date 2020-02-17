package net.onima.onimaapi.report;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;

import net.md_5.bungee.api.chat.ClickEvent;
import net.onima.onimaapi.cooldown.BugReportCooldown;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.utils.PlayerOption;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.report.struct.ReportStat;
import net.onima.onimaapi.report.struct.ReportStatus;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;

public class BugReport extends Report {
	
	protected long timeWhenBugOccured;
	protected String linkToProof;
	protected String playerActionsDescription;

	public BugReport(UUID reporter, String reason) {
		super(reporter, reason);
	}
	
	@Override
	public boolean execute() {
		APIPlayer apiPlayer = APIPlayer.getPlayer(reporter);

		if (apiPlayer.getTimeLeft(BugReportCooldown.class) > 0L) {
			apiPlayer.sendMessage("§cVous ne pouvez pas soumettre de bug report pour le moment.");
			return false;
		}
		
		initID();
		save();
		
		apiPlayer.addStatistic(ReportStat.REPORTS, 1);
		apiPlayer.getReports().add(this);
		
		JSONMessage msg = new JSONMessage("§a" + Methods.getRealName(Bukkit.getOfflinePlayer(reporter)) + " §2a signalé un bug !",
				"§2Description : §a" + reason +
				"\n\n§aClic gauche §2pour afficher les détails" +
				"\n§2du §asignalement §6#" + id);
		
		msg.setClickAction(ClickEvent.Action.RUN_COMMAND);
		msg.setClickString("/bugreports id " + id);

		for (APIPlayer player : APIPlayer.getOnlineAPIPlayers()) {
			if (player.getOptions().getBoolean(PlayerOption.GlobalOptions.REPORT_NOTIFY) && player.toPlayer().hasPermission(OnimaPerm.REPORTS_COMMAND.getPermission()))
				player.sendMessage(msg);
		}
		
		apiPlayer.sendMessage("§eVous avez report un bug. Revenez voir dans quelque temps.");
		apiPlayer.startCooldown(BugReportCooldown.class);
		return true;
	}
	
	@Override
	public BetterItem getItem() {
		BetterItem item = new BetterItem(status.getMaterial(), 1, status.getColor());
		
		if (status.equals(ReportStatus.WAITING))
			item.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		
		OfflinePlayer rerOff = Bukkit.getOfflinePlayer(reporter);
		
		return item.setName("§aBug Report §7#" + id)
				.addLore("")
				.addLore("§7Status : " + status.getTitle(doneBy))
				.addLore("§7Date : §e" + Methods.toFormatDate(time, ConfigurationService.DATE_FORMAT_HOURS))
				.addLore("")
				.addLore("§7Report par : §a" + Methods.getRealName(rerOff) + "§7(" + (rerOff.isOnline() ? "§aconnecté" : "§cdéconnecté") + "§7)")
				.addLore("§7Raison : §6" + reason);
	}
	
	public long getTimeWhenBugOccured() {
		return timeWhenBugOccured;
	}
	
	public String getLinkToProof() {
		return linkToProof;
	}
	
	public String getPlayerActionsDescription() {
		return playerActionsDescription;
	}
	
	@Override
	public void serialize() {
		file.set("bug_reports." + id + ".time_bug_occured", timeWhenBugOccured);
		file.set("bug_reports." + id + ".proof_link", linkToProof);
		file.set("bug_reports." + id + ".player_actions", playerActionsDescription);
		
		super.serialize();
	}

}
