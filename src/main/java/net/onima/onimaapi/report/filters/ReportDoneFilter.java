package net.onima.onimaapi.report.filters;

import net.onima.onimaapi.report.Report;

public class ReportDoneFilter implements ReportFilter {
	
	private boolean done;

	protected ReportDoneFilter(boolean done) {
		this.done = done;
	}
	
	@Override
	public String getDispalyName() {
		return "§eFiltre les reports traités";
	}
	
	@Override
	public boolean filter(Report report) {
		return report.isDone() == done;
	}

}
