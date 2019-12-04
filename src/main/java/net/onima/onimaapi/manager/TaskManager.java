package net.onima.onimaapi.manager;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.tasks.FastTileTask;
import net.onima.onimaapi.tasks.SchedulerTask;

public class TaskManager {
	
	private OnimaAPI plugin;

	public TaskManager(OnimaAPI plugin) {
		this.plugin = plugin;
	}
	
	public void registerTasks() {
		new SchedulerTask().runTaskTimerAsynchronously(plugin, 0L, 20L);
		new FastTileTask().runTaskTimerAsynchronously(plugin, 2L, 2L);
	}
	
}

