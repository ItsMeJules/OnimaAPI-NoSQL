package net.onima.onimaapi.players.notes;

public enum NotePriority {
	
	INFORMATIVE(0, "§7"),
	IMPORTANT(1, "§6"),
	VERY_IMPORTANT(2, "§c"),
	EXTREMELY_IMPORTANT(3, "§4");
	
	private int priority;
	private String color;

	private NotePriority(int priority, String color) {
		this.priority = priority;
		this.color = color;
	}

	public int getPriority() {
		return priority;
	}

	public String getColor() {
		return color;
	}

	public static NotePriority fromName(String string) {
		switch (string) {
		case "INFORMATIVE":
			return INFORMATIVE;
		case "IMPORTANT":
			return IMPORTANT;
		case "VERY_IMPORTANT":
			return VERY_IMPORTANT;
		case "EXTREMELY_IMPORTANT":
			return EXTREMELY_IMPORTANT;
		default:
			return null;
		}
	}

}
