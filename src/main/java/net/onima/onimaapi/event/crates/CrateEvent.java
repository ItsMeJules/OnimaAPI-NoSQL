package net.onima.onimaapi.event.crates;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.onima.onimaapi.crates.utils.Crate;

public class CrateEvent extends Event {
	
	private static HandlerList handlers = new HandlerList();
	
	private Crate crate;
	
	public CrateEvent(Crate crate) {
		this.crate = crate;
	}
	
	public Crate getCrate() {
		return crate;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
