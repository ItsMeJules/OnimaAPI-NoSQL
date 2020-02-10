package net.onima.onimaapi.gui.menu.report;

import java.util.HashMap;
import java.util.Map;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.ReportButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.report.Report;
import net.onima.onimaapi.report.filters.ReportFilter;

public class PlayerReceivedReportsMenu extends PlayerReportsMenu {

	public PlayerReceivedReportsMenu(PacketMenu backMenu, OfflineAPIPlayer offline) {
		super("player_received_reports", "§6Reports reçus", backMenu, offline);
		
		addFilter(ReportFilter.getPlayerFilter(offline.getUUID(), false));
	}

	@Override
	public Map<Integer, Button> getAllPagesItems() {
		Map<Integer, Button> map = new HashMap<>();
		
		for (Report report : offline.getReports()) {
			if (isValid(report))
				buttons.put(buttons.size(), new ReportButton(report, true));
		}
		
		return map;
	}
	
}
