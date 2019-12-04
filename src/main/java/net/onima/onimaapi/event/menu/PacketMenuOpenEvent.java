package net.onima.onimaapi.event.menu;

import org.bukkit.event.Cancellable;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.players.APIPlayer;

public class PacketMenuOpenEvent extends PacketMenuEvent implements Cancellable {

	private APIPlayer apiPlayer;
	private boolean cancelled;

	public PacketMenuOpenEvent(PacketMenu menu, APIPlayer apiPlayer) {
		super(menu);
		
		this.apiPlayer = apiPlayer;
	}
	
	public APIPlayer getApiPlayer() {
		return apiPlayer;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
}
