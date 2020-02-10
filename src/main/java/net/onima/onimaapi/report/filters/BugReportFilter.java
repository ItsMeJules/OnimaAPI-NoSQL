package net.onima.onimaapi.report.filters;

import net.onima.onimaapi.report.BugReport;
import net.onima.onimaapi.report.Report;

public class BugReportFilter implements ReportFilter {
	
	protected BugReportFilter() {}
	
	@Override
	public String getDispalyName() {
		return "§eFiltre les reports de bugs";
	}

	@Override
	public boolean filter(Report report) {
		return report instanceof BugReport;
	}

}
