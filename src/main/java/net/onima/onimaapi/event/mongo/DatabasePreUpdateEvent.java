package net.onima.onimaapi.event.mongo;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.onima.onimaapi.saver.mongo.NoSQLSaver;

public class DatabasePreUpdateEvent extends Event {
	
	private static HandlerList handlers = new HandlerList();
	
	private NoSQLSaver saver;
	
	public DatabasePreUpdateEvent(NoSQLSaver saver) {
		this.saver = saver;
	}
	
	public NoSQLSaver getSaver() {
		return saver;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	
}
