package net.onima.onimaapi.tasks;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.bukkit.scheduler.BukkitRunnable;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.utils.Scheduler;

public class SchedulerTask extends BukkitRunnable {

	@Override
	public void run() {
		ZonedDateTime now = ZonedDateTime.now();
		
		for (Scheduler scheduled : OnimaAPI.getScheduled()) {
			
			if (!scheduled.isSchedulerEnabled() || !scheduled.isSchedulerSet()) 
				continue;
			
			if (now.until(scheduled.getTemporal(), ChronoUnit.SECONDS) <= 0) {
				scheduled.action(true);
				scheduled.setTemporal(scheduled.getTemporal().plus(scheduled.getResetTimeCycle(), ChronoUnit.MILLIS));
			} else
				scheduled.action(false);
			
		}
	}

}
