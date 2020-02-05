package net.onima.onimaapi.report.struct;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;

public enum ReportStatus {
	
	WAITING("§aEn attente", Material.PAPER, (short) 5),
	IN_PROGRESS("§6En cours", Material.PAPER, (short) 4),
	IMPORTANT("§cImportant", Material.EMPTY_MAP, (short) 14),
	DONE("§bTraité par %name%", Material.BOOK, (short) 3);

	private String title;
	private Material material;
	private short color;

	ReportStatus(String title, Material material, short color) {
		this.title = title;
		this.material = material;
		this.color = color;
	}

	public String getTitle(String name) {
		return name != null ? StringUtils.replace(title, "%name%", name) : title;
	}

	public Material getMaterial() {
		return material;
	}

	public short getColor() {
		return color;
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
