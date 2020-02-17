package net.onima.onimaapi.gui.menu.report;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import net.md_5.bungee.api.chat.ClickEvent;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.BackButton;
import net.onima.onimaapi.gui.buttons.MenuOpenerButton;
import net.onima.onimaapi.gui.buttons.ReportButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.buttons.utils.HeadButton;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.punishment.Warn;
import net.onima.onimaapi.punishment.utils.Punishment;
import net.onima.onimaapi.report.BugReport;
import net.onima.onimaapi.report.PlayerReport;
import net.onima.onimaapi.report.Report;
import net.onima.onimaapi.report.struct.ReportStat;
import net.onima.onimaapi.report.struct.ReportStatus;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.CasualFormatDate;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;

public class ReportInfoMenu extends PacketMenu {

	private Report report;
	private boolean isBugReport;

	public ReportInfoMenu(Report report) {
		super("report_info", (report instanceof BugReport ? "§aBug Report §7#" : "§7Report §c#") + report.getId(), MIN_SIZE * 3, false);
		
		this.report = report;
	
		isBugReport = report instanceof BugReport;
	}

	@Override
	public void registerItems() {
		buttons.put(0, new ReportButton(report, false));
		OfflineAPIPlayer.getPlayer(report.getReporter(), offline -> {
			buttons.put(3, new ReporterButton(offline, true));
			buttons.put(4, new AbusiveReportButton(offline));
		});
		
		if (!isBugReport)
			OfflineAPIPlayer.getPlayer(((PlayerReport) report).getReported(), offline -> buttons.put(5, new ReporterButton(offline, false)));
		else {
			BugReport br = (BugReport) report;
			
			buttons.put(20, new BugReportInfoButton(br, 0));
			buttons.put(5, new BugReportInfoButton(br, 1));
		
			if (br.getLinkToProof() != null)
				buttons.put(24, new BugReportInfoButton(br, 2));
		}
			
		buttons.put(8, new BackButton(new ReportsMenu(report.isDone())));
		
		int index = 12;
		for (ReportStatus status : ReportStatus.values()) {
			if (status == ReportStatus.DONE && !report.isDone())
				buttons.put(22, new MenuOpenerButton(new BetterItem(status.getMaterial(isBugReport), 1, status.getColor(isBugReport), "§eDéfinir le verdict", "", "§6Clic §7pour définir le verdict."), new VerdictMenu(report, this)));
			else
				buttons.put(status == ReportStatus.DONE ? 22 : index, new StatusButton(status));
			
			index++;
		}
		
		buttons.put(18, new DeleteReportButton());
		buttons.put(26, new CommentsMenuButton());
	}
	
	private class BugReportInfoButton implements Button {

		private BugReport report;
		private int i;
		private String name;
		private List<String> lore;
		private Material material;
		
		{
			lore = Lists.newArrayList("");
		}

		public BugReportInfoButton(BugReport report, int i) {
			this.report = report;
			this.i = i;
			
			switch (i) {
			case 0:
				name = "§eCe que le joueur faisait";
				lore.addAll(Lists.newArrayList(report.getPlayerActionsDescription().split("(?<=\\G.{34})")));
				material = Material.WATER_LILY;
				break;
			case 1:
				name = "§eDate du bug";
				lore.add("§6" + Methods.toFormatDate(report.getTimeWhenBugOccured(), ConfigurationService.DATE_FORMAT_HOURS));
				lore.add("§6" + new CasualFormatDate("d u z hi").toNormalDate(report.getTimeWhenBugOccured()));
				material = Material.WATCH;
				break;
			case 2:
				name = "§eLien vers une vidéo/image";
				lore.addAll(Lists.newArrayList(report.getLinkToProof().split("(?<=\\G.{34})")));
				lore.add("");
				lore.add("§6Clic §7pour afficher le lien dans le chat.");
				material = Material.ITEM_FRAME;
				break;
			default:
				break;
			}
		}

		@Override
		public BetterItem getButtonItem(Player player) {
			return new BetterItem(material, 1, 0, name, lore);
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);

			if (i == 2) {
				new JSONMessage(report.getLinkToProof(), "§eCliquez pour ouvrir le lien.", true, report.getLinkToProof(), ClickEvent.Action.OPEN_URL).send(clicker);
				menu.close(APIPlayer.getPlayer(clicker), true);
			}
		}
		
	}
	
	private class AbusiveReportButton implements Button {
		
		private OfflineAPIPlayer offline;

		public AbusiveReportButton(OfflineAPIPlayer offline) {
			this.offline = offline;
		}

		@Override
		public BetterItem getButtonItem(Player player) {
			String realName = Methods.getRealName(offline.getOfflinePlayer());
			
			return new BetterItem(ReportStat.ABUSIVE.getMaterial(), 1, ReportStat.ABUSIVE.getDamage(), ReportStat.ABUSIVE.getTitle(), "",
					"§6Clic gauche §7pour warn §lsilencieusement", "§a" + realName + " §7pour report abusif.",
					"§6Clic droit §7pour warn §lpubliquement", "§a" + realName + " §7pour report abusif.");
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
			offline.addStatistic(ReportStat.ABUSIVE, 1);
			
			Punishment warn = new Warn(clicker.getUniqueId(), offline.getUUID());
			
			warn.setReason("Report abusif (ID du report : §7#" + report.getId() + "§a)");
			warn.setSilent(event.isLeftClick());
			warn.execute();
		}
		
	}
	
	private class StatusButton implements Button {
		
		private ReportStatus status;

		private StatusButton(ReportStatus status) {
			this.status = status;
		}

		@Override
		public BetterItem getButtonItem(Player player) {
			BetterItem item = new BetterItem(Material.STAINED_CLAY, 1, status.getColor(false), status == ReportStatus.DONE ? status.getTitle(report.getDoneBy()) : status.getTitle(null), "");
			
			if (status == report.getStatus()) {
				item.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				item.addLore("§7§oStatus actutel.");
			} else
				item.addLore("§6Clic §7pour définir le status du").addLore("§7report sur : §6" + status.getTitle(null).toLowerCase());
			
			return item;
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
			report.setStatus(status);
			updateItems(clicker);
		}
		
	}
	
	private class DeleteReportButton implements Button {

		@Override
		public BetterItem getButtonItem(Player player) {
			return new BetterItem(Material.TNT, 1, 0, "§cSupprimer le report", "", "§6Clic §7pour supprimer le report.");
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
			report.remove();
			
			OfflineAPIPlayer.getPlayer(report.getReporter(), offline -> offline.getReports().remove(report));
			
			if (report instanceof PlayerReport)
				OfflineAPIPlayer.getPlayer(((PlayerReport) report).getReported(), offline -> offline.getReports().remove(report));
			
			menu.close(APIPlayer.getPlayer(clicker), true);
		}
		
	}
	
	private class ReporterButton extends HeadButton {
		
		private boolean reporter;

		{
			lore();
		}

		public ReporterButton(OfflineAPIPlayer owner, boolean reporter) {
			super(owner);
			
			this.reporter = reporter;
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
			new ReportStatMenu(owner).open(APIPlayer.getPlayer(clicker));
		}

		@Override
		protected void lore() {
			lore.add((reporter ? "§7Reporter : §a" : "§7Reporté : §c") + Methods.getRealName(owner.getOfflinePlayer()) + "§7(" + (owner.isOnline() ? "§aconnecté" : "§cdéconnecté") + "§7)");
			lore.add("");
			lore.add(ReportStat.REPORTS.getTitle() + "§b" + owner.getReportStastitics().get(ReportStat.REPORTS));
			lore.add(ReportStat.REPORTED_TIMES.getTitle() + "§b" + owner.getReportStastitics().get(ReportStat.REPORTED_TIMES));
			lore.add("");
			lore.add("§6Clic §7pour afficher ses stats.");
		}
		
	}
	
	private class CommentsMenuButton implements Button {

		@Override
		public BetterItem getButtonItem(Player player) {
			int size = report.getComments().size();
			return new BetterItem(Material.CHEST, size > 64 ? 64 : size, 0, "§eCommentaires sur ce report.", "", "§6Clic §7pour afficher les", "§7commentaires.");
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
			APIPlayer apiPlayer = APIPlayer.getPlayer(clicker);
			
			apiPlayer.openMenu(new CommentsMenu(report, apiPlayer, ReportInfoMenu.this));
		}
		
	}

}
