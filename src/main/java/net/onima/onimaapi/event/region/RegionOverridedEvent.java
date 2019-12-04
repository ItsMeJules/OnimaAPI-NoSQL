package net.onima.onimaapi.event.region;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.onima.onimaapi.zone.type.Region;

public class RegionOverridedEvent extends Event {

	private static HandlerList handlers = new HandlerList();
	
	private Region region;
	private Region overrider;
	private CommandSender sender;
	
	public RegionOverridedEvent(Region region, Region overrider, CommandSender sender) {
		this.region = region;
		this.overrider = overrider;
		this.sender = sender;
	}
	
	public Region getRegion() {
		return region;
	}

	public Region getOverrider() {
		return overrider;
	}

	public CommandSender getSender() {
		return sender;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
