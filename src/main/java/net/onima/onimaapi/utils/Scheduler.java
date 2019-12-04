package net.onima.onimaapi.utils;

import java.time.Month;
import java.time.temporal.Temporal;

public interface Scheduler {

	public Temporal getTemporal();
	public void setTemporal(Temporal temporal);
	public void scheduleEvery(long timeRestart);
	public long getResetTimeCycle();
	public void startTime(Month month, int day, int hour, int minute);
	
	public void action(boolean started);
	
	public boolean isSchedulerEnabled();
	public void setSchedulerEnabled(boolean schedulerEnabled);
	
	public boolean isSchedulerSet();

	public long getWhenItStarts();
	public long getStartTimeLeft();

}
