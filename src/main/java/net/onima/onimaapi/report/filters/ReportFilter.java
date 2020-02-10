package net.onima.onimaapi.report.filters;

import java.util.UUID;

import net.onima.onimaapi.report.Report;
import net.onima.onimaapi.report.struct.ReportStatus;
import net.onima.onimaapi.report.struct.Verdict;

public interface ReportFilter {

	public static final ReportFilter BUG_REPORT_FILTER = new BugReportFilter();
	public static final ReportFilter PLAYER_REPORT_FILTER = new PlayerReportFilter();
	public static final ReportFilter COMMENTED_REPORT_FILTER = new ReportCommentedFilter();
	public static final ReportFilter DONE_REPORT_FILTER = new ReportDoneFilter(true);
	public static final ReportFilter NOT_DONE_REPORT_FILTER = new ReportDoneFilter(false);
	public static final ReportFilter IMPORTANT_STATUS_REPORT_FILTER = new ReportStatusFilter(ReportStatus.IMPORTANT);
	public static final ReportFilter IN_PROGRESS_STATUS_REPORT_FILTER = new ReportStatusFilter(ReportStatus.IN_PROGRESS);
	public static final ReportFilter WAITING_STATUS_REPORT_FILTER = new ReportStatusFilter(ReportStatus.WAITING);
	public static final ReportFilter TRUE_VERDICT_REPORT_FILTER = new ReportVerdictFilter(Verdict.TRUE);
	public static final ReportFilter UNCERTAIN_VERDICT_REPORT_FILTER = new ReportVerdictFilter(Verdict.UNCERTAIN);
	public static final ReportFilter FALSE_VERDICT_REPORT_FILTER = new ReportVerdictFilter(Verdict.FALSE);
	
	String getDispalyName();
	boolean filter(Report report);
	
	public static ReportFilter getPlayerFilter(UUID uuid, boolean sent) {
		if (sent)
			return new ReportSentFilter(uuid);
		else
			return new ReportReceivedFilter(uuid);
	}
	
}
