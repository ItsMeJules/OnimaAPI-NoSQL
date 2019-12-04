package net.onima.onimaapi.event.menu;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.onima.onimaapi.gui.PacketMenu;

public class PacketMenuEvent extends Event {
	
	private static HandlerList handlers = new HandlerList();
	private PacketMenu menu;
	
	public PacketMenuEvent(PacketMenu menu) {
		this.menu = menu;
	}
	
	public PacketMenu getPacketMenu() {
		return menu;	
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
