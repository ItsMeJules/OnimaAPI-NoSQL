package net.onima.onimaapi.event.disguise;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.onima.onimaapi.disguise.DisguiseSkin;
import net.onima.onimaapi.rank.RankType;

public class PlayerDisguiseEvent extends Event {
	
	private static HandlerList handlers = new HandlerList();
	
	private Player player;
	private UUID disguisedUUID;
	private String originalName;
	private String newName;
	private RankType rankType;
	private DisguiseSkin skin;
	
	public PlayerDisguiseEvent(Player player, UUID disguisedUUID, String originalName, String newName, RankType rankType, DisguiseSkin skin) {
		this.player = player;
		this.disguisedUUID = disguisedUUID;
		this.originalName = originalName;
		this.newName = newName;
		this.rankType = rankType;
		this.skin = skin;
	}
	
	public Player getPlayer() {
		return player;
	}

	public UUID getDisguisedUUID() {
		return disguisedUUID;
	}

	public String getOriginalName() {
		return originalName;
	}

	public String getNewName() {
		return newName;
	}

	public RankType getRankType() {
		return rankType;
	}

	public DisguiseSkin getSkin() {
		return skin;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}


}
