package net.onima.onimaapi.workload.type;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.bukkit.Bukkit;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.utils.Scheduler;
import net.onima.onimaapi.workload.Workload;

public class SchedulerWorkload implements Workload {
	
	private Scheduler scheduler;
	
	public SchedulerWorkload(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	public void compute() {
		ZonedDateTime now = ZonedDateTime.now();
		
		if (now.until(scheduler.getTemporal(), ChronoUnit.SECONDS) <= 0) {
			Bukkit.getScheduler().runTask(OnimaAPI.getInstance(), () -> scheduler.action(true));
			scheduler.setTemporal(scheduler.getTemporal().plus(scheduler.getResetTimeCycle(), ChronoUnit.MILLIS));
		} else
			scheduler.action(false);
			
	}

	@Override
	public boolean reschedule() {
		return scheduler.isSchedulerEnabled() || scheduler.isSchedulerSet();
	}
	
}
