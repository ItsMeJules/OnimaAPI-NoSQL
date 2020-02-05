package net.onima.onimaapi.manager;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.tasks.FastTileTask;

public class TaskManager {
	
	private OnimaAPI plugin;

	public TaskManager(OnimaAPI plugin) {
		this.plugin = plugin;
	}
	
	public void registerTasks() {
		new FastTileTask().runTaskTimerAsynchronously(plugin, 2L, 2L);
	}
	
}
