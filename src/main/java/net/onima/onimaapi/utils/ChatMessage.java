package net.onima.onimaapi.utils;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.onima.onimaapi.rank.RankType;

public class ChatMessage {
	
	protected String playerName;
	protected StringBuilder message;
	protected BaseComponent[] components;
	protected RankType rank;
	protected String rankClickCommand, nameColor, chatColor;
	protected boolean colors;
	
	{
		message = new StringBuilder(100);
	}
	
	public ChatMessage(String playerName, String message) {
		this.playerName = playerName;
		this.message.append(message);
	}
	
	public ChatMessage(String playerName) {
		this(playerName, "");
	}
	
	public ChatMessage prefixMessage(String message) {
		this.message.insert(0, message);
		return this;
	}
	
	public ChatMessage message(String message) {
		this.message.append(message);
		return this;
	}
	
	public ChatMessage nameColor(ChatColor nameColor) {
		this.nameColor = nameColor.toString();
		return this;
	}
	
	public ChatMessage nameColor(String nameColor) {
		this.nameColor = nameColor;
		return this;
	}
	
	public ChatMessage chatColor(ChatColor chatColor) {
		this.chatColor = chatColor.toString();
		return this;
	}
	
	public ChatMessage chatColor(String chatColor) {
		this.chatColor = chatColor;
		return this;
	}
	
	public ChatMessage rank(RankType rank) {
		this.rank = rank;
		return this;
	}
	
	public ChatMessage rankClickCommand(String rankClickCommand) {
		this.rankClickCommand = rankClickCommand;
		return this;
	}
	
	public ChatMessage canUseColor(boolean colors) {
		this.colors = colors;
		return this;
	}
	
	public ChatMessage build() {
		ComponentBuilder builder = new ComponentBuilder(rank.getPrefix());
		
		builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(rank.getDescription()).create()));
		builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, rankClickCommand));
		
		builder.append(rank == RankType.DEFAULT ? "" : " ", FormatRetention.NONE);
		builder.append(nameColor + playerName + "§r");
		builder.append(": ");
		builder.append(chatColor + (colors ? Methods.colors(message.toString()) : message.toString()));
		
		components = builder.create();
		return this;
	}
	
	public void send(Collection<CommandSender> senders) {
		for (CommandSender sender : senders)
			Methods.sendJSON(sender, components);
	}
	
	public void send(Player... players) {
		send(Arrays.asList(players));
	}
	
}
