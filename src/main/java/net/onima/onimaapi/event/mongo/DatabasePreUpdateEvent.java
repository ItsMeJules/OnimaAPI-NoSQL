package net.onima.onimaapi.event.mongo;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.onima.onimaapi.mongo.saver.NoSQLSaver;

public class DatabasePreUpdateEvent extends Event {
	
	private static HandlerList handlers = new HandlerList();
	
	private NoSQLSaver saver;
	private Action action;
	private boolean runAsync;
	
	public DatabasePreUpdateEvent(NoSQLSaver saver, Action action, boolean runAsync) {
		this.saver = saver;
		this.action = action;
		this.runAsync = runAsync;
	}
	
	public NoSQLSaver getSaver() {
		return saver;
	}
	
	public Action getAction() {
		return action;
	}
	
	public boolean shouldRunAsync() {
		return runAsync;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	public static enum Action {
		DELETE,
		WRITE;
	}
	
}
