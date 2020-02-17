package net.onima.onimaapi.report.struct;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;

public enum ReportStatus {
	
	WAITING("§aEn attente", Material.PAPER, Material.GOLDEN_APPLE,(short) 5, (short) 1),
	IN_PROGRESS("§6En cours", Material.PAPER, Material.GOLDEN_APPLE, (short) 4, (short) 0),
	IMPORTANT("§cImportant", Material.EMPTY_MAP, Material.APPLE, (short) 14, (short) 0),
	DONE("§bTraité par %name%", Material.BOOK, Material.FIREBALL, (short) 3, (short) 0);

	private String title;
	private Material material, bugMaterial;
	private short color, bugColor;

	ReportStatus(String title, Material material, Material bugMaterial, short color, short bugColor) {
		this.title = title;
		this.material = material;
		this.bugMaterial = bugMaterial;
		this.color = color;
		this.bugColor = bugColor;
	}

	public String getTitle(String name) {
		return name != null ? StringUtils.replace(title, "%name%", name) : title;
	}

	public Material getMaterial(boolean bugReport) {
		return bugReport ? bugMaterial : material;
	}

	public short getColor(boolean bugReport) {
		return bugReport ? bugColor : color;
	}

	public static ReportStatus getFrom(String status) {
		switch (status) {
		case "WAITING":
			return WAITING;
		case "IN_PROGRESS":
			return IN_PROGRESS;
		case "IMPORTANT":
			return IMPORTANT;
		case "DONE":
			return DONE;
		default:
			return WAITING;
		}
	}

}
