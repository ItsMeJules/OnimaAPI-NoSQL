package net.onima.onimaapi.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.onima.onimaapi.cooldown.utils.Cooldown;
import net.onima.onimaapi.players.OfflineAPIPlayer;

public class CooldownEvent extends Event {
	
	private static HandlerList handlers = new HandlerList();
	private Cooldown cooldown;
	private OfflineAPIPlayer apiPlayer;
	
	public CooldownEvent(Cooldown cooldown, OfflineAPIPlayer apiPlayer) {
		this.cooldown = cooldown;
		this.apiPlayer = apiPlayer;
	}
	
	public Cooldown getCooldown() {
		return cooldown;	
	}

	public OfflineAPIPlayer getOfflineAPIPlayer() {
		return apiPlayer;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
