package net.onima.onimaapi.report.filters;

import net.onima.onimaapi.report.Report;
import net.onima.onimaapi.report.struct.Verdict;

public class ReportVerdictFilter implements ReportFilter {
	
	private Verdict verdict;

	protected ReportVerdictFilter(Verdict verdict) {
		this.verdict = verdict;
	}
	
	@Override
	public String getDispalyName() {
		return "§eFiltre les reports avec le verdict : " + verdict.getTitle().toLowerCase();
	}

	@Override
	public boolean filter(Report report) {
		return report.getVerdict() == verdict;
	}

}
