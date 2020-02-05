package net.onima.onimaapi.event.disguise;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.onima.onimaapi.disguise.DisguiseManager;
import net.onima.onimaapi.disguise.DisguiseSkin;
import net.onima.onimaapi.players.APIPlayer;

public class PlayerPreDisguiseEvent extends Event implements Cancellable {
	
	private static HandlerList handlers = new HandlerList();
	
	private APIPlayer apiPlayer;
	private DisguiseSkin skin;
	private DisguiseManager disguiseManager;
	private boolean cancelled;
	
	public PlayerPreDisguiseEvent(APIPlayer apiPlayer, DisguiseSkin skin, DisguiseManager disguiseManager) {
		this.apiPlayer = apiPlayer;
		this.skin = skin;
		this.disguiseManager = disguiseManager;
	}

	public APIPlayer getAPIPlayer() {
		return apiPlayer;
	}
	
	public DisguiseSkin getSkin() {
		return skin;
	}
	
	public DisguiseManager getDisguiseManager() {
		return disguiseManager;
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

}
