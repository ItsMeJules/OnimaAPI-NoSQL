package net.onima.onimaapi.report.filters;

import java.util.UUID;

import net.onima.onimaapi.report.Report;

public class ReportSentFilter implements ReportFilter {
	
	private UUID sender;

	protected ReportSentFilter(UUID sender) {
		this.sender = sender;
	}
	
	@Override
	public String getDispalyName() {
		return "§eFiltre les reports envoyés";
	}

	@Override
	public boolean filter(Report report) {
		return sender.equals(report.getReporter());
	}

}