package net.onima.onimaapi.event.ranks;

import org.bukkit.command.CommandSender;

import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.Rank;

public class RankReceivedEvent extends RankEvent {
	
	private CommandSender sender;

	public RankReceivedEvent(Rank rank, APIPlayer apiPlayer, boolean recalculatePermissions, CommandSender sender) {
		super(rank, apiPlayer, recalculatePermissions);
		
		this.sender = sender;
	}
	
	public CommandSender getSender() {
		return sender;
	}

}
