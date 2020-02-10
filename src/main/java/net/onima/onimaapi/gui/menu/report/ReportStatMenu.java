package net.onima.onimaapi.gui.menu.report;

import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.DisplayButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.report.struct.ReportStat;
import net.onima.onimaapi.utils.BetterItem;

public class ReportStatMenu extends PacketMenu {

	private OfflineAPIPlayer offline;

	public ReportStatMenu(OfflineAPIPlayer offline) {
		super("report_stats", "§6Reports §7» §c" + offline.getName(), MIN_SIZE * 2, false);
		
		this.offline = offline;
		
		permission = OnimaPerm.REPORT_STATS_CHECK;
	}

	@Override
	public void registerItems() {
		for (Entry<ReportStat, Integer> entry : offline.getReportStastitics().entrySet()) {
			ReportStat stat = entry.getKey();
			int value = entry.getValue();
			
			buttons.put(stat.getSlot(), new DisplayButton(new BetterItem(stat.getMaterial(), value > 64 ? 64 : value, stat.getDamage(), stat.getTitle() + "§b" + value)));
		}
		
		buttons.put(10, new ReportsReceivedButton(false));
		buttons.put(12, new ReportsReceivedButton(true));
		
		buttons.put(14, new ReportsSentButton(false));
		buttons.put(16, new ReportsSentButton(true));
	}
	
	private class ReportsReceivedButton implements Button {
		
		private boolean processed;

		public ReportsReceivedButton(boolean processed) {
			this.processed = processed;
		}
		
		@Override
		public BetterItem getButtonItem(Player player) {
			return new BetterItem(processed ? Material.BOOKSHELF : Material.BOOK_AND_QUILL, 1, 0, "§eReports " + (processed ? "§cnon-" : "§a") + "classés §eà l'encontre de §7" + offline.getName());
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class ReportsSentButton implements Button {
		
		private boolean processed;

		public ReportsSentButton(boolean processed) {
			this.processed = processed;
		}
		
		@Override
		public BetterItem getButtonItem(Player player) {
			return new BetterItem(processed ? Material.BOOKSHELF : Material.BOOK_AND_QUILL, 1, 0, "§eReports " + (processed ? "§cnon-" : "§a") + "classés §eenvoyés par §7" + offline.getName());
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
