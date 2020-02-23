package net.onima.onimaapi.report;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;

import net.md_5.bungee.api.chat.ClickEvent;
import net.onima.onimaapi.gui.menu.report.ReportReasonMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.players.utils.PlayerOption;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.report.struct.ReportReason;
import net.onima.onimaapi.report.struct.ReportStat;
import net.onima.onimaapi.report.struct.ReportStatus;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.OSound;
import net.onima.onimaapi.utils.time.Time;

public class PlayerReport extends Report { //TODO Ajouter les effets que le joueur reporté avait ?
	
	public static final long TIME_BETWEEN_REPORT_SAME_PLAYER;
	
	static {
		TIME_BETWEEN_REPORT_SAME_PLAYER = 5 * Time.MINUTE;
	}

	private UUID reported;
	
	public PlayerReport(UUID reporter, String reason, UUID reported) {
		super(reporter, reason);
		
		this.reported = reported;
	}
	
	/**
	 * It will always return true, it can also take up to 2 ticks.
	 */
	@Override
	public boolean execute() {
		APIPlayer apiPlayer = APIPlayer.getPlayer(reporter);
		
		if (reported.equals(reporter)) {
			apiPlayer.sendMessage("§cVous ne pouvez pas vous report vous même.");
			return true;
		}
		
		OfflineAPIPlayer.getPlayer(reported, offline -> {
			if (offline.getRank().getRankType().hasPermisssion(OnimaPerm.REPORT_EXEMPT)) {
				new OSound(Sound.VILLAGER_NO, 1F, 2F).play(apiPlayer);
				apiPlayer.sendMessage("§cVous ne pouvez pas report ce joueur !");
				return;
			}
			
			if (!apiPlayer.getReports().isEmpty()) {
				for (Report report : apiPlayer.getReports()) {
					if (report instanceof PlayerReport
							&& reported.equals(((PlayerReport) report).reported)
							&& time - report.time <= PlayerReport.TIME_BETWEEN_REPORT_SAME_PLAYER) {
						new OSound(Sound.VILLAGER_NO, 1F, 2F).play(apiPlayer);
						apiPlayer.sendMessage("§cVous avez déjà report ce joueur, veuillez patienter !");
						return;
					}
				}
			}
			
			initID();
			save();
			
			apiPlayer.addStatistic(ReportStat.REPORTS, 1);
			apiPlayer.getReports().add(this);
			
			offline.getReports().add(this);
			offline.addStatistic(ReportStat.REPORTED_TIMES, 1);
			
			JSONMessage msg = new JSONMessage("§a" + Methods.getRealName(Bukkit.getOfflinePlayer(reporter)) + " §7a report §c" + Methods.getName(offline, true) + "§7(" + (offline.isOnline() ? "§aconnecté" : "§cdéconnecté") + "§7)",
					"§7Raison : §6" + reason +
					"\n\n§6Clic gauche §7pour afficher les détails" +
					"\n§7du §creport §6#" + id);
			
			msg.setClickAction(ClickEvent.Action.RUN_COMMAND);
			msg.setClickString("/reports id " + id);

			for (APIPlayer player : APIPlayer.getOnlineAPIPlayers()) {
				if (player.getOptions().getBoolean(PlayerOption.GlobalOptions.REPORT_NOTIFY) && player.toPlayer().hasPermission(OnimaPerm.REPORTS_COMMAND.getPermission()))
					player.sendMessage(msg);
			}
			
			if (apiPlayer.getViewingMenu() instanceof ReportReasonMenu) {
				new OSound(Sound.VILLAGER_YES, 1F, 2F).play(apiPlayer);
				apiPlayer.sendMessage("§eVous avez report §6" + Methods.getName(offline) + " §epour : §7" + ReportReason.valueOf(reason).getNiceName());
			} else
				apiPlayer.sendMessage("§eVous avez report §6" + Methods.getName(offline) + " §epour : §7" + reason);
		});
		
		return true;
	}
	
	@Override
	public BetterItem getItem() {
		BetterItem item = new BetterItem(status.getMaterial(false), 1, status.getColor(false));
		
		if (status.equals(ReportStatus.WAITING))
			item.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		
		OfflinePlayer rerOff = Bukkit.getOfflinePlayer(reporter);
		OfflinePlayer redOff = Bukkit.getOfflinePlayer(reported);
		
		item.setName("§cReport §7#" + id)
			.addLore("")
			.addLore("§7Status : " + status.getTitle(doneBy))
			.addLore("§7Date : §e" + Methods.toFormatDate(time, ConfigurationService.DATE_FORMAT_HOURS));
		
		if (verdict != null)
			item.addLore("§7Verdict : " + verdict.getTitle());
		
		item.addLore("")
			.addLore("§7Report par : §a" + Methods.getRealName(rerOff) + "§7(" + (rerOff.isOnline() ? "§aconnecté" : "§cdéconnecté") + "§7)")
			.addLore("§7Joueur report : §c" + Methods.getRealName(redOff) + "§7(" + (redOff.isOnline() ? "§aconnecté" : "§cdéconnecté") + "§7)")
			.addLore("§7Raison : §6" + reason);
		
		return item;
	}
	
	public UUID getReported() {
		return reported;
	}
	
	@Override
	public void serialize() {
		file.set("player_reports." + id + ".reported", reported.toString());
		
		super.serialize();
	}
	
}
