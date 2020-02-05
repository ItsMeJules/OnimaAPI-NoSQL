package net.onima.onimaapi.workload.manager;

import java.util.ArrayDeque;

import org.bukkit.scheduler.BukkitTask;

import net.minecraft.util.com.google.common.collect.Queues;
import net.onima.onimaapi.workload.Workload;

public class WorkloadThread implements Runnable {
	
	public static final double DEFAULT_MAX_MS_PER_TICK;
	
	protected static int lastId;
	
	static {
		DEFAULT_MAX_MS_PER_TICK = 5 * 1E6; //Millis -> Nano = X * 1E6
		lastId = 0;
	}
	
	protected ArrayDeque<Workload> deque;
	protected Workload firstRescheduledElement;
	protected int id;
	protected double maxMsPerTick;
	protected BukkitTask bukkitTask;
	
	protected WorkloadThread(double maxMsPerTick) {
		deque = Queues.newArrayDeque();
		id = lastId++;
		maxMsPerTick = maxMsPerTick * 1E6;
	}
	
	public void addWorkload(Workload workload) {
		deque.add(workload);
	}
	
	@Override
	public void run() {
		double thresold = System.nanoTime() + maxMsPerTick;
		
		while (!deque.isEmpty() && System.nanoTime() <= thresold) {
			if (!computeWorkload(deque.poll()))
				break;
		}
	}
	
	public int getId() {
		return id;
	}
	
	protected boolean computeWorkload(Workload workload) {
		if (workload != null) {
			if (workload.shouldExecute())
				workload.compute();
			
			if (workload.reschedule()) {
				addWorkload(workload);
				
				if (firstRescheduledElement == null)
					firstRescheduledElement = workload;
				else
					return firstRescheduledElement != null;
			}
		}
		
		return true;
	}

}
