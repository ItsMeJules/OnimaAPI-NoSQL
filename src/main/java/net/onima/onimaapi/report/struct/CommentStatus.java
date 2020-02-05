package net.onima.onimaapi.report.struct;

public enum CommentStatus {
	
	PRIVATE("§cPrivé"),
	SENT("§6Envoyé"),
	READ("§aLu");
	
	private String title;

	private CommentStatus(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
}
