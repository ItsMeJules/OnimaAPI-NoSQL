package net.onima.onimaapi.report.filters;

import net.onima.onimaapi.report.PlayerReport;
import net.onima.onimaapi.report.Report;

public class PlayerReportFilter implements ReportFilter {

	protected PlayerReportFilter() {}
	
	@Override
	public String getDispalyName() {
		return "§eFiltre les reports de joueurs";
	}
	
	@Override
	public boolean filter(Report report) {
		return report instanceof PlayerReport;
	}

}
