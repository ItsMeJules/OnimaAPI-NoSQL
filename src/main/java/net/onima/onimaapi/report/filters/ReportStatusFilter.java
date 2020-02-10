package net.onima.onimaapi.report.filters;

import net.onima.onimaapi.report.Report;
import net.onima.onimaapi.report.struct.ReportStatus;

public class ReportStatusFilter implements ReportFilter {

	private ReportStatus status;

	protected ReportStatusFilter(ReportStatus status) {
		this.status = status;
	}
	
	@Override
	public String getDispalyName() {
		return "§eFiltre les reports avec le statut : " + status.getTitle(null).toLowerCase();
	}
	
	@Override
	public boolean filter(Report report) {
		return report.getStatus() == status;
	}

}
