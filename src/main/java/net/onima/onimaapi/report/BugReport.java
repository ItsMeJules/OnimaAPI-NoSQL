package net.onima.onimaapi.report;

import java.util.UUID;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.chat.ClickEvent;
import net.onima.onimaapi.cooldown.BugReportCooldown;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.utils.PlayerOption;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;

public class BugReport extends Report {

	public BugReport(UUID reporter, String reason) {
		super(reporter, reason);
	}
	
	@Override
	public boolean execute() {
		APIPlayer apiPlayer = APIPlayer.getPlayer(reporter);

		if (apiPlayer.getTimeLeft(BugReportCooldown.class) >= 0L) {
			apiPlayer.sendMessage("§cVous ne pouvez pas soumettre de bug report pour le moment.");
			return false;
		}
		
		initID();
		save();
		
		apiPlayer.getReports().add(this);
		
		JSONMessage msg = new JSONMessage("§a" + Methods.getRealName(Bukkit.getOfflinePlayer(reporter)) + " §2a signalé un bug !",
				"§2Description : §a" + reason +
				"\n\n§aClic gauche §2pour afficher les détails" +
				"\n§2du §asignalement §6#" + id);
		
		msg.setClickAction(ClickEvent.Action.RUN_COMMAND);
		msg.setClickString("/bugreports #" + id);

		for (APIPlayer player : APIPlayer.getOnlineAPIPlayers()) {
			if (player.getOptions().getBoolean(PlayerOption.GlobalOptions.REPORT_NOTIFY) && player.toPlayer().hasPermission(OnimaPerm.REPORTS_COMMAND.getPermission()))
				player.sendMessage(msg);
		}
		
		return true;
	}
	
	@Override
	public BetterItem getItem() {
		// TODO Auto-generated method stub
		return null;
	}

}
