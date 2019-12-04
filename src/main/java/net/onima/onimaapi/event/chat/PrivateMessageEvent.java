package net.onima.onimaapi.event.chat;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import net.onima.onimaapi.rank.RankType;

public class PrivateMessageEvent extends ChatEvent {

	private RankType readerRank;
	
	public PrivateMessageEvent(CommandSender sender, CommandSender reader, String message) {
		super(sender, Arrays.asList(reader), message, RankType.getRank(sender));
		
		readerRank = RankType.getRank(reader);
	}
	
	public RankType getReaderRank() {
		return readerRank;
	}
	
	public void setReaderRank(RankType readerRank) {
		this.readerRank = readerRank;
	}
	
	public CommandSender getReceiver() {
		for (CommandSender sender : readers)
			return sender;
		
		return null;
	}
	
}
