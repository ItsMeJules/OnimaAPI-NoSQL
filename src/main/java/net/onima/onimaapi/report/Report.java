package net.onima.onimaapi.report;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.manager.ConfigManager;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.report.struct.ReportStatus;
import net.onima.onimaapi.report.struct.Verdict;
import net.onima.onimaapi.saver.FileSaver;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.Config;

public abstract class Report implements FileSaver {
	
	public static final byte MIN_REPORT_REASON_LENGTH;
	
	public static int ID;
	
	protected static Set<Report> reports;
	protected static Config config;
	protected static FileConfiguration file;
	protected static boolean refreshed;
	
	static {
		MIN_REPORT_REASON_LENGTH = 3;
		
		reports = new TreeSet<>(new Comparator<Report>() {
			@Override
			public int compare(Report r1, Report r2) {
				if (r1.time > r2.time)
					return -1;
				else if (r1.time < r2.time)
					return 1;
				else
					return 0;
			}
		});
		config = ConfigManager.getReportSerialConfig();
		file = config.getConfig();
	}
	
	protected int id;
	protected UUID reporter;
	protected String reason, doneBy;
	protected long time;
	protected ReportStatus status;
	protected Verdict verdict;
	protected List<ReportComment> comments;
	
	{
		time = System.currentTimeMillis();
		status = ReportStatus.WAITING;
		comments = new ArrayList<>();
	}
	
	public Report(UUID reporter, String reason) {
		this.reporter = reporter;
		this.reason = reason;
	}
	
	public abstract boolean execute();
	public abstract BetterItem getItem();
	
	public void process(Verdict verdict, String doneBy) {
		this.verdict = verdict;
		this.doneBy = doneBy;
		
		status = ReportStatus.DONE;
	}
	
	public void initID() {
		id = ID++;
	}
	
	public int getId() {
		return id;
	}

	public UUID getReporter() {
		return reporter;
	}
	
	public String getReason() {
		return reason;
	}
	
	public boolean isDone() {
		return doneBy != null;
	}
	
	public String getDoneBy() {
		return doneBy;
	}
	
	public void setDoneBy(String doneBy) {
		this.doneBy = doneBy;
	}
	
	public long getTime() {
		return time;
	}
	
	public ReportStatus getStatus() {
		return status;
	}
	
	public void setStatus(ReportStatus status) {
		this.status = status;
	}
	
	public Verdict getVerdict() {
		return verdict;
	}
	
	public void setVerdict(Verdict verdict) {
		this.verdict = verdict;
	}
	
	public List<ReportComment> getComments() {
		return comments;
	}
	
	@Override
	public boolean isSaved() {
		return reports.contains(this);
	}
	
	@Override
	public void refreshFile() {
		ConfigurationSection section = file.getConfigurationSection("reports");
		
		if (section != null) {
			List<Integer> reportsId = reports.stream().map(Report::getId).collect(Collectors.toList());
			
			for (String id : section.getKeys(false)) {
				if (!reportsId.contains(Integer.valueOf(id)))
					config.remove("reports." + id, false);
			}
		}
		
		refreshed = true;		
	}
	
	@Override
	public void remove() {
		reports.remove(this);
		OnimaAPI.getShutdownSavers().remove(this);
	}
	
	@Override
	public void save() {
		reports.add(this);
		OnimaAPI.getShutdownSavers().add(this);
	}
	
	@Override
	public void serialize() {
		if (!refreshed)
			refreshFile();
		
		String path = (this instanceof PlayerReport ? "player" : "bug")+ "_reports." + id + ".";
		
		file.set(path + "reporter", reporter.toString());
		file.set(path + "reason", reason);
		file.set(path + "done_by", doneBy);
		file.set(path + "time", time);
		file.set(path + "status", status.name());
		file.set(path + "verdict", verdict == null ? null : verdict.name());
		file.set(path + "comments", comments.stream().map(ReportComment::asSerializableString).collect(Collectors.toCollection(() -> new ArrayList<>(comments.size()))));
	}
	
	public static void deserialize() {
		ConfigurationSection section = file.getConfigurationSection("player_reports");
		
		if (section != null) {
			for (String id : section.getKeys(false)) {
				String path = id + ".";
				String verdict = section.getString(path + "verdict");
				PlayerReport report = new PlayerReport(UUID.fromString(section.getString(path + "reporter")), section.getString(path + "reason"), UUID.fromString(section.getString(path + "reported")));
				
				OfflineAPIPlayer.getPlayer(report.getReporter(), offline -> offline.getReports().add(report));
				OfflineAPIPlayer.getPlayer(report.getReported(), offline -> offline.getReports().add(report));
				report.save();
				
				report.id = Integer.valueOf(id);
				report.time = section.getLong(path + "time");
				report.status = ReportStatus.valueOf(section.getString(path + "status"));
				report.verdict = verdict == null ? null : Verdict.valueOf(verdict);
				report.doneBy = section.getString(path + "done_by");
				
				for (String line : section.getStringList(path + "comments"))
					report.comments.add(ReportComment.fromString(line, report));
				
			}
		}
		
		ConfigurationSection bugSection = file.getConfigurationSection("bug_reports");
		
		if (bugSection != null) {
			for (String id : section.getKeys(false)) {
				String path = id + ".";
				String verdict = section.getString(path + "verdict");
				BugReport report = new BugReport(UUID.fromString(section.getString(path + "reporter")), section.getString(path + "reason"));
				
				OfflineAPIPlayer.getPlayer(report.getReporter(), offline -> offline.getReports().add(report));
				report.save();
				
				report.id = Integer.valueOf(id);
				report.time = section.getLong(path + "time");
				report.status = ReportStatus.valueOf(section.getString(path + "status"));
				report.verdict = verdict == null ? null : Verdict.valueOf(verdict);
				report.doneBy = section.getString(path + "done_by");
				report.timeWhenBugOccured = section.getLong(path + "time_bug_occured");
				report.linkToProof = section.getString(path + "proof_link");
				report.playerActionsDescription = section.getString(path + "player_actions");
				
				for (String line : section.getStringList(path + "comments"))
					report.comments.add(ReportComment.fromString(line, report));
			}
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + id;
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
		result = prime * result + ((reporter == null) ? 0 : reporter.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + (int) (time ^ (time >>> 32));
		result = prime * result + ((verdict == null) ? 0 : verdict.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		Report other = (Report) obj;
		
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		
		if (id != other.id)
			return false;
		
		if (reason == null) {
			if (other.reason != null)
				return false;
			
		} else if (!reason.equals(other.reason))
			return false;
		
		if (reporter == null) {
			if (other.reporter != null)
				return false;
			
		} else if (!reporter.equals(other.reporter))
			return false;
		
		if (status != other.status)
			return false;
		
		if (time != other.time)
			return false;
		
		if (verdict != other.verdict)
			return false;
		
		return true;
	}
	
	public static Set<Report> getReports() {
		return reports;
	}
	
}
