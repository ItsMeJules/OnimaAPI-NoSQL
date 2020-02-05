package net.onima.onimaapi.cooldown;

import net.onima.onimaapi.cooldown.utils.Cooldown;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.utils.time.Time;

public class BugReportCooldown extends Cooldown {

	public BugReportCooldown() {
		super("bug_report", (byte) 17, 5 * Time.MINUTE);
	}

	@Override
	public String scoreboardDisplay(long timeLeft) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean action(OfflineAPIPlayer offline) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
