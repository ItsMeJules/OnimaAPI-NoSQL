package net.onima.onimaapi.workload.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import net.onima.onimaapi.OnimaAPI;

public class WorkloadDistributor {

	private Map<Integer, WorkloadThread> map;
	
	public WorkloadDistributor() {
		map = new HashMap<>();
	}

	public WorkloadThread newThread(double maxExecuteTime) {
		WorkloadThread thread = new WorkloadThread(maxExecuteTime);
		
		map.put(thread.getId(), thread);
		return thread;
	}

	public WorkloadThread newThread() {
		WorkloadThread thread = new WorkloadThread(WorkloadThread.DEFAULT_MAX_MS_PER_TICK);
		
		map.put(thread.getId(), thread);
		return thread;
	}
	
	public void start(int id, Callable<BukkitTask> callback) {
		WorkloadThread thread = get(id);
		
		if (thread == null)
			return;
		
		try {
			thread.bukkitTask = callback.call();
				
			if (thread.bukkitTask == null)
				throw new NullPointerException("bukkitTask = null, id du WorkloadThread : " + thread.id);
		} catch (Exception e) {
				e.printStackTrace();
		}
	}
	
	public void startPerTick(int id, boolean async, long ticks) {
		WorkloadThread thread = get(id);
		
		if (thread == null)
			return;
		
		thread.bukkitTask = async ? Bukkit.getScheduler().runTaskTimerAsynchronously(OnimaAPI.getInstance(), thread, 0L, ticks) : Bukkit.getScheduler().runTaskTimer(OnimaAPI.getInstance(), thread, 0L, ticks);
	}
	
	public void startPerSecond(int id, boolean async, long seconds) {
		WorkloadThread thread = get(id);
		
		if (thread == null)
			return;
	
		thread.bukkitTask = async ? Bukkit.getScheduler().runTaskTimerAsynchronously(OnimaAPI.getInstance(), thread, 0L, 20L * seconds) : Bukkit.getScheduler().runTaskTimer(OnimaAPI.getInstance(), thread, 0L, 20L * seconds);
	}
	
	public WorkloadThread get(int id) {
		return map.get(id);
	}
	
	public void stop(int id) {
		WorkloadThread thread = map.remove(id);
		
		if (thread != null && thread.bukkitTask != null)
			thread.bukkitTask.cancel();
	}
	
}
