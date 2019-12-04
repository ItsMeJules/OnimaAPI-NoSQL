package net.onima.onimaapi.event.chat;

import java.util.Collection;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.onima.onimaapi.rank.RankType;

public class ChatEvent extends Event implements Cancellable {
	
	protected static HandlerList handlers = new HandlerList();
	
	protected CommandSender sender;
	protected Collection<CommandSender> readers;
	protected String message;
	protected boolean cancelled;
	protected RankType rankType;
	
	public ChatEvent(CommandSender sender, Collection<CommandSender> readers, String message, RankType rankType) {
		this.sender = sender;
		this.readers = readers;
		this.message = message;
		this.rankType = rankType;
	}
	
	public CommandSender getSender() {
		return sender;
	}

	public Collection<CommandSender> getReaders() {
		return readers;
	}

	public String getMessage() {
		return message;
	}
	
	public RankType getRankType() {
		return rankType;
	}
	
	public void setRankType(RankType rankType) {
		this.rankType = rankType;
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
