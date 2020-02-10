package net.onima.onimaapi.report;

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
	private String comment;
	
	{
		time = System.currentTimeMillis();
		status = CommentStatus.PRIVATE;
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
		apiPlayer.sendMessage(author + " a écrit un commentaire sur votre §creport §7#" + report.getId() +
				"\n§7Ecrit le : §e" + Methods.toFormatDate(time, ConfigurationService.DATE_FORMAT_HOURS) +
				"\n§7Commentaire : §e" + comment);
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
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}

	public void addToComment(String comment) {
		if (this.comment == null)
			this.comment = comment;
		
		this.comment += comment;
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
		return new StringBuilder("id=").append(id).append(';')
				.append("time=").append(time).append(';')
				.append("report_id=").append(report.id).append(';')
				.append("author=").append(author).append(';')
				.append("status=").append(status.name()).append(';')
				.append("comment=").append(comment).toString();
	}

}
