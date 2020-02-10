package net.onima.onimaapi.report.filters;

import java.util.UUID;

import net.onima.onimaapi.report.PlayerReport;
import net.onima.onimaapi.report.Report;

public class ReportReceivedFilter implements ReportFilter {
	
	private UUID sender;

	protected ReportReceivedFilter(UUID sender) {
		this.sender = sender;
	}
	
	@Override
	public String getDispalyName() {
		return "§eFiltre les reports reçus";
	}

	@Override
	public boolean filter(Report report) {
		if (!(report instanceof PlayerReport))
			return false;
		
		return sender.equals(((PlayerReport) report).getReported());
	}

}