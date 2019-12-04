package net.onima.onimaapi.crates.utils;

public enum CrateType {
	
	PHYSICAL,
	VIRTUAL,
	ROOM,
	SUPPLY;
	
	public static CrateType fromName(String name) {
		switch (name) {
		case "PHYSICAL":
			return PHYSICAL;
		case "VIRTUAL":
			return VIRTUAL;
		case "ROOM":
			return ROOM;
		case "SUPPLY":
			return SUPPLY;
		default:
			return null;
		}
	}

}
