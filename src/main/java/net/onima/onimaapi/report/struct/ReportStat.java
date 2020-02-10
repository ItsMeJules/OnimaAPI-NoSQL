package net.onima.onimaapi.report.struct;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

public enum ReportStat {

	TRUE_APPRECIATIONS(0, "§7Verdict " + Verdict.TRUE.title.toLowerCase() + " : ", Material.STAINED_CLAY, (short) 5),
	UNCERTAIN_APPRECIATIONS(1, "§7Verdict " + Verdict.UNCERTAIN.title.toLowerCase() + " : ", Material.STAINED_CLAY, (short) 4),
	FALSE_APPRECIATIONS(2, "§7Verdict " + Verdict.FALSE.title.toLowerCase() + " : ", Material.STAINED_CLAY, (short) 14),
	ABUSIVE(4, "§eReports abusifs : ", Material.GOLD_AXE, (short) 0),
	REPORTS(6, "§7Reports envoyés : ", Material.BOW, (short) 0),
	REPORTED_TIMES(7, "§7Reports reçus : ", Material.PAPER, (short) 0),
	PROCESSED_REPORTS(8, "§7Reports traités : ", Material.BOOK, (short) 0);
	
	protected int slot;
	protected String title;
	protected Material material;
	protected short damage;
	
	private ReportStat(int slot, String title, Material material, short damage) {
		this.slot = slot;
		this.title = title;
		this.material = material;
		this.damage = damage;
	}
	
	public int getSlot() {
		return slot;
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
	
	public static Map<ReportStat, Integer> newMap() {
		Map<ReportStat, Integer> map = new HashMap<>();
		
		for (ReportStat stat : values())
			map.put(stat, 0);
			
		return map;
	}
	
}
