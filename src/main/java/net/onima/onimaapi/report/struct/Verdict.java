package net.onima.onimaapi.report.struct;

import org.bukkit.Material;

public enum Verdict {

	TRUE("§aVrai", Material.STAINED_CLAY, (short) 5),
	UNCERTAIN("§eIncertain", Material.STAINED_CLAY, (short) 4),
	FALSE("§c§lFaux", Material.STAINED_CLAY, (short) 14);
	
	private String title;
	private Material material;
	private short damage;

	private Verdict(String title, Material material, short damage) {
		this.title = title;
		this.material = material;
		this.damage = damage;
	}
	
	public String getTitle() {
		return title;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public short getDamage() {
		return damage;
	}
	
	public static Verdict getVerdict(String verdict) {
		switch (verdict) {
		case "TRUE":
			return TRUE;
		case "UNCERTAIN":
			return UNCERTAIN;
		case "FALSE":
			return FALSE;
		default:
			return null;
		}
	}

}
