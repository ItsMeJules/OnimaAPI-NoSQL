package net.onima.onimaapi.report.filters;

import net.onima.onimaapi.report.Report;

public class ReportCommentedFilter implements ReportFilter {

	protected ReportCommentedFilter() {}
	
	@Override
	public String getDispalyName() {
		return "§eFiltre les reports commentés";
	}
	
	@Override
	public boolean filter(Report report) {
		return !report.getComments().isEmpty();
	}

}
