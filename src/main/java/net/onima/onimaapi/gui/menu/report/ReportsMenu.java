package net.onima.onimaapi.gui.menu.report;

import java.util.HashMap;
import java.util.Map;

import net.onima.onimaapi.gui.buttons.ReportButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.menu.utils.PageMenu;
import net.onima.onimaapi.report.Report;

public class ReportsMenu extends PageMenu {

	private boolean done;

	public ReportsMenu(boolean done) {
		super("reports_" + done, "§6Reports " + (done ? "traités" : "non-traités"), MAX_SIZE, false);
		
		this.done = done;
	}

	@Override
	public Map<Integer, Button> getAllPagesItems() {
		Map<Integer, Button> map = new HashMap<>();
		
		for (Report report : Report.getReports()) {
			if (report.isDone() == done)
				map.put(map.size(), new ReportButton(report, true));
		}
		
		return map;
	}

	@Override
	public int getMaxItemsPerPage() {
		return 53;
	}

}
