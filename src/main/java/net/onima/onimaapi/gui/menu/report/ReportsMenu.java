package net.onima.onimaapi.gui.menu.report;

import java.util.HashMap;
import java.util.Map;

import net.onima.onimaapi.gui.PacketStaticMenu;
import net.onima.onimaapi.gui.buttons.ReportButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.menu.utils.PageMenu;
import net.onima.onimaapi.report.Report;

public class ReportsMenu extends PageMenu implements PacketStaticMenu {

	public ReportsMenu() {
		super("reports", "§6Reports §7» §e" + Report.getReports().size(), MAX_SIZE, false);
	}

	@Override
	public Map<Integer, Button> getAllPagesItems() {
		Map<Integer, Button> map = new HashMap<>();
		
		Report.getReports().stream().filter(report -> !report.isDone()).forEach(report -> map.put(map.size(), new ReportButton(report, true)));
		
		return map;
	}

	@Override
	public int getMaxItemsPerPage() {
		return 53;
	}

	@Override
	public void setup() {
		updateItems(null);
	}

}
