package net.onima.onimaapi.crates.prizes;

public enum PrizeRarity {
	
	BASIC("§8Basique"),
	NORMAL("§7Normal"),
	RARE("§2Rare"),
	EPIC("§5Epique"),
	LEGENDARY("§f§k- §6Légendaire §f§k-"),
	MYSTIC("§c§k|§f§k|§c§k| §eMystique §c§k|§f§k|§c§k|");
	
	private String niceName;
	
	private PrizeRarity(String niceName) {
		this.niceName = niceName;
	}
	
	public String getNiceName() {
		return niceName;
	}
	
	public static PrizeRarity fromName(String name) {
		switch (name) {
		case "§8Basique":
		case "BASIC":
			return BASIC;
		case "§7Normal":
		case "NORMAL":
			return NORMAL;
		case "§2Rare":
		case "RARE":
			return RARE;
		case "§5Epique":
		case "EPIC":
			return EPIC;
		case "§f§k- §6Légendaire §f§k-":
		case "LEGENDARY":
			return LEGENDARY;
		case "§c§k|§f§k|§c§k| §eMystique §c§k|§f§k|§c§k|":
		case "MYSTIC":
			return MYSTIC;
		default:
			return null;
		}
	}

}
