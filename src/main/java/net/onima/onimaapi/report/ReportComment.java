package net.onima.onimaapi.report;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.report.struct.CommentStatus;
import net.onima.onimaapi.saver.Saver;
import net.onima.onimaapi.saver.SerializableString;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;

public class ReportComment implements Saver, SerializableString {
	
	private int id;
	private long time;
	private Report report;
	private String author;
	private CommentStatus status;
	private List<String> comment;
	
	{
		time = System.currentTimeMillis();
		status = CommentStatus.PRIVATE;
		comment = new ArrayList<>();
	}
	
	public ReportComment(Report report, String author) {
		this.report = report;
		this.author = author;
		
		id = report.comments.size();
	}
	
	public boolean sendToReporter() {
		APIPlayer apiPlayer = APIPlayer.getPlayer(report.getReporter());
		
		if (apiPlayer == null)
			return false;
		else {
			sendToReporter(apiPlayer);
			return true;
		}
	}
	
	public void sendToReporter(APIPlayer apiPlayer) {
		apiPlayer.sendMessage(author + " §fa écrit un commentaire sur votre §creport §7#" + report.getId() +
				"\n§7Ecrit le : §e" + Methods.toFormatDate(time, ConfigurationService.DATE_FORMAT_HOURS) +
				"\n§7Commentaire : §e" + commentAsString().replace("£", " "));
		status = CommentStatus.READ;
	}
	
	public int getId() {
		return id;
	}
	
	public long getTime() {
		return time;
	}
	
	public Report getReport() {
		return report;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public CommentStatus getStatus() {
		return status;
	}
	
	public void setStatus(CommentStatus status) {
		this.status = status;
	}
	
	public List<String> getComment() {
		return comment;
	}
	
	public void setComment(List<String> comment) {
		this.comment = comment;
	}

	public void addToComment(String comment) {
		this.comment.add(comment);
	}
	
	public String commentAsString() {
		StringBuilder builder = new StringBuilder();
		
		for (String part : this.comment)
			builder.append(part).append("£");
		
		return builder.toString();
	}
	
	@Override
	public void save() {
		report.comments.add(this);
	}

	@Override
	public void remove() {
		report.comments.remove(this);
	}

	@Override
	public boolean isSaved() {
		return report.comments.contains(this);
	}
	
	@Override
	public String asSerializableString() {
		return new StringBuilder().append(id).append(';')
				.append(time).append(';')
				.append(author).append(';')
				.append(status.name()).append(';')
				.append(commentAsString()).toString();
	}

	public static ReportComment fromString(String str, Report report) {
		String[] parts = str.split(";");
		
		ReportComment comment = new ReportComment(report, parts[2]);
		
		comment.id = Integer.valueOf(parts[0]);
		comment.time = Long.valueOf(parts[1]);
		comment.status = CommentStatus.valueOf(parts[3]);
		comment.comment = Lists.newArrayList(parts[4].split("£"));
		
		return comment;
	}

}
