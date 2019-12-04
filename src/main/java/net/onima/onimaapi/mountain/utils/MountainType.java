package net.onima.onimaapi.mountain.utils;

public enum MountainType {

	GLOWSTONE,
	ORES,
	TREASURE;

	public static MountainType fromString(String string) {
		switch (string) {
		case "GLOWSTONE":
			return GLOWSTONE;
		case "ORES":
			return ORES;
		case "TREASURE":
			return TREASURE;
		default:
			return null;
		}
	}
	
}
