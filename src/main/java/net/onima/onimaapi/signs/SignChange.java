package net.onima.onimaapi.signs;

import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

public class SignChange {
	
	private Sign sign;
	private String[] newLines;
	private BukkitRunnable runnable;

	public SignChange(Sign sign, String[] newLines) {
		this.sign = sign;
		this.newLines = newLines;
	}

	public Sign getSign() {
		return sign;
	}

	public String[] getNewLines() {
		return newLines;
	}

	public BukkitRunnable getRunnable() {
		return runnable;
	}

	public void setRunnable(BukkitRunnable runnable) {
		this.runnable = runnable;
	}
}
