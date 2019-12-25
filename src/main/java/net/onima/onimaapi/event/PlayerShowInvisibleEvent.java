package net.onima.onimaapi.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerShowInvisibleEvent extends Event {
	
	private static HandlerList handlers = new HandlerList();
	
	private boolean show;
	private Player clicker;
	
	public PlayerShowInvisibleEvent(boolean show, Player clicker) {
		this.show = !show;
		this.clicker = clicker;
	}

	public boolean shouldShow() {
		return show;
	}

	public Player getClicker() {
		return clicker;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
