package net.onima.onimaapi.report.struct;

import org.bukkit.Material;

public enum ReportReason {
	
	FLY(Material.FEATHER, (short) 0, "fly"),
	SPEED(Material.DIAMOND_HOE, (short) 0, "speed"),
	FORCE_FIELD(Material.IRON_SWORD, (short) 0, "force field"),
	REACH(Material.LEASH, (short) 0, "reach"),
	FAST_BOW(Material.BOW, (short) 0, "fast bow"),
	KNOBACK(Material.FISHING_ROD, (short) 0, "knockback"),
	GRIEF(Material.TNT, (short) 0, "grief");
	
	private Material material;
	private short damage;
	private String niceName;
	
	private ReportReason(Material material, short damage, String niceName) {
		this.material = material;
		this.damage = damage;
		this.niceName = niceName;
	}
	
	public Material getMaterial() {
		return material;
	}

	public short getDamage() {
		return damage;
	}
	
	public String getNiceName() {
		return niceName;
	}
	
}
