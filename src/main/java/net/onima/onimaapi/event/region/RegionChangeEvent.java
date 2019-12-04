package net.onima.onimaapi.event.region;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.zone.type.Region;

public class RegionChangeEvent extends Event implements Cancellable {
	
	private static HandlerList handlers = new HandlerList();
	
	private Region region;
	private APIPlayer apiPlayer;
	private RegionChangeCause cause;
	private boolean cancelled;
	
	public RegionChangeEvent(Region region, APIPlayer apiPlayer, RegionChangeCause cause) {
		this.region = region;
		this.apiPlayer = apiPlayer;
		this.cause = cause;
	}
	
	public Region getRegion() {
		return region;
	}

	public APIPlayer getAPIPlayer() {
		return apiPlayer;
	}

	public RegionChangeCause getCause() {
		return cause;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public static interface RegionChangeCause {
		
	}
	
	public static enum RegionChangementCause implements RegionChangeCause {
		CREATED,
		RESIZE,
		DELETED;
	}

}
