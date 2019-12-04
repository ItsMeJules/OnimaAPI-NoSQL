package net.onima.onimaapi.event.disguise;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerUndisguiseEvent extends Event {
	
	private static HandlerList handlers = new HandlerList();
	private Player player;

	public PlayerUndisguiseEvent(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
