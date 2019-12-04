package net.onima.onimaapi.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.scheduler.BukkitRunnable;

import net.onima.onimaapi.utils.callbacks.VoidCallback;

public abstract class TaskPerEntry<T> {
	
	protected List<EntriesTask> tasks;
	
	private int maxPerTask;
	private VoidCallback<EntriesTask> callback;
	
	{
		tasks = new ArrayList<>();
	}
	
	public TaskPerEntry(int maxPerTask) {
		this.maxPerTask = maxPerTask;
	}
	
	public void task(VoidCallback<EntriesTask> callback) {
		this.callback = callback;
	}
	
	public void insert(T t) {
		EntriesTask task = getCurrentTask(true);
		
		if (!task.isRunning()) {
			callback.call(task);
			task.setRunning(true);
		}
		
		task.entries.add(t);
	}
	
	public void insert(Collection<T> collection) {
		for (T element : collection)
			insert(element);
	}
	
	public void remove(T t) {
		EntriesTask task = getCurrentTask(false);
		
		if (task != null) {
			task.entries.remove(t);
		
			if (task.entries.size() < 1) {
				task.cancel();
				tasks.remove(task);
			}
		}
	}
	
	public void iteratorRemove(Iterator<T> iterator) {
		EntriesTask task = getCurrentTask(false);
		
		if (task != null) {
			iterator.remove();
			
			if (task.entries.size() < 1) {
				task.cancel();
				tasks.remove(task);
			}
		}
	}
	
	public EntriesTask getCurrentTask(boolean createTask) {
		EntriesTask task = null;
		
		if (tasks.isEmpty() && createTask) {
			tasks.add(task = new EntriesTask());
		} else {
			ListIterator<EntriesTask> iterator = tasks.listIterator();
			
			while (iterator.hasNext()) {
				EntriesTask next = iterator.next();
				
				if (next.entries.size() >= maxPerTask) {
					if (!iterator.hasNext() && createTask) {
						iterator.add(task = new EntriesTask());
						break;
					}
				} else {
					task = next;
					break;
				}
					
			}
		}
		
		return task;
	}
	
	public abstract void run(List<T> list);
	
	public class EntriesTask extends BukkitRunnable {
		
		private List<T> entries = new ArrayList<>();
		private boolean running;

		@Override
		public void run() {
			TaskPerEntry.this.run(entries);
		}
		
		public boolean isRunning() {
			return running;
		}
		
		public void setRunning(boolean running) {
			this.running = running;
		}
		
	}
	
}
