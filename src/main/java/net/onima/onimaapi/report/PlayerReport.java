package net.onima.onimaapi.report;

import java.util.UUID;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.chat.ClickEvent;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.players.utils.PlayerOption;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.time.Time;

public class PlayerReport extends Report {
	
	public static final long TIME_BETWEEN_REPORT_SAME_PLAYER;
	
	static {
		TIME_BETWEEN_REPORT_SAME_PLAYER = 5 * Time.MINUTE;
	}

	private OfflineAPIPlayer reported;
	
	public PlayerReport(UUID reporter, String reason, OfflineAPIPlayer reported) {
		super(reporter, reason);
		
		this.reported = reported;
	}
	
	@Override
	public boolean execute() {
		APIPlayer apiPlayer = APIPlayer.getPlayer(reporter);
		
		if (reported.getRank().getRankType().hasPermisssion(OnimaPerm.REPORT_EXEMPT)) {
			apiPlayer.sendMessage("§cVous ne pouvez pas report ce joueur !");
			return false;
		}
		
		if (!apiPlayer.getReports().isEmpty()) {
			for (Report report : apiPlayer.getReports()) {
				if (report instanceof PlayerReport && reported.equals(((PlayerReport) report).reported)) {
					PlayerReport r1 = (PlayerReport) report;
					
					if (report.time - r1.time <= PlayerReport.TIME_BETWEEN_REPORT_SAME_PLAYER) {
						apiPlayer.sendMessage("§cVous avez déjà report ce joueur, veuillez patienter !");
						return false;
					}
				}
			}
		}
		
		initID();
		save();
		
		apiPlayer.getReports().add(this);
		reported.getReports().add(this);
		
		JSONMessage msg = new JSONMessage("§a" + Methods.getRealName(Bukkit.getOfflinePlayer(reporter)) + " §7a report §c" + Methods.getName(reported, true) + "§7(" + (reported.isOnline() ? "§aconnecté" : "§cdéconnecté") + "§7)",
				"§7Raison : §6" + reason +
				"\n\n§6Clic gauche §7pour afficher les détails" +
				"\n§7du §creport §6#" + id);
		
		msg.setClickAction(ClickEvent.Action.RUN_COMMAND);
		msg.setClickString("/reports #" + id);

		for (APIPlayer player : APIPlayer.getOnlineAPIPlayers()) {
			if (player.getOptions().getBoolean(PlayerOption.GlobalOptions.REPORT_NOTIFY) && player.toPlayer().hasPermission(OnimaPerm.REPORTS_COMMAND.getPermission()))
				player.sendMessage(msg);
		}
		
		return true;
	}
	
	public OfflineAPIPlayer getReported() {
		return reported;
	}
	
	@Override
	public void serialize() {
		file.set("reports." + id + ".reported", reported.toString());
		
		super.serialize();
	}
	
}
