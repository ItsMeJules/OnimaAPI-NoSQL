package net.onima.onimaapi.event.mongo;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLoadEvent extends Event {
	
	private static HandlerList handlers = new HandlerList();
	
	private UUID uuid;
	
	public PlayerLoadEvent(UUID uuid) {
		this.uuid = uuid;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
