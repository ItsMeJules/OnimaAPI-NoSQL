package net.onima.onimaapi.event.ranks;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.Rank;

public class RankEvent extends Event {
	
	private static HandlerList handlers = new HandlerList();
	private Rank rank;
	private APIPlayer apiPlayer;
	private boolean recalculatePermissions;
	
	public RankEvent(Rank rank, APIPlayer apiPlayer, boolean recalculatePermissions) {
		this.rank = rank;
		this.apiPlayer = apiPlayer;
		this.recalculatePermissions = recalculatePermissions;
	}
	
	public Rank getRank() {
		return rank;	
	}

	public APIPlayer getAPIPlayer() {
		return apiPlayer;
	}
	
	public boolean shouldRecalculatePermissions() {
		return recalculatePermissions;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
