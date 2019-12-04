package net.onima.onimaapi.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class JSONMessage {
	
	private String message, hoverMessage, clickString;
	private boolean click;
	private ClickEvent.Action clickAction;
	
	public JSONMessage(String message, String hoverMessage) {
		this.message = message;
		this.hoverMessage = hoverMessage;
	}
	
	public JSONMessage(String message, boolean click, String clickString) {
		this.message = message;
		this.click = click;
		this.clickString = clickString;
	}
	
	public JSONMessage(String message, String hoverMessage, boolean click, String clickString) {
		this.message = message;
		this.hoverMessage = hoverMessage;
		this.click = click;
		this.clickString = clickString;
	}
	
	public JSONMessage(String message, String hoverMessage, boolean click, String clickString, ClickEvent.Action clickAction) {
		this.message = message;
		this.hoverMessage = hoverMessage;
		this.click = click;
		this.clickString = clickString;
		this.clickAction = clickAction;
	}
	
	public void setClickString(String clickString) {
		this.clickString = clickString;
	}
	
	public void setHoverMessage(String hoverMessage) {
		this.hoverMessage = hoverMessage;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getHoverMessage() {
		return hoverMessage;
	}
	
	public JSONMessage appendHoverMessage(String message) {
		hoverMessage += message;
		return this;
	}

	public void setClickAction(ClickEvent.Action clickAction) {
		click = true;
		this.clickAction = clickAction;
	}

	public void send(CommandSender receiver) {
		send(receiver, "");
	}
	
	public void send(CommandSender receiver, String prefix) {
		if (receiver instanceof Player)
			((Player) receiver).spigot().sendMessage(build(prefix));
		else
			receiver.sendMessage(prefix + message);
	}
	
	public BaseComponent[] build() {
		return build("");
	}
	
	public BaseComponent[] build(String prefix) {
		ComponentBuilder builder = new ComponentBuilder(prefix + message);
		
		if (hoverMessage != null || !hoverMessage.isEmpty())
			builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMessage).create()));
		
		if (click)
			builder.event(new ClickEvent(clickAction == null ? ClickEvent.Action.RUN_COMMAND : clickAction, clickString));
		
		return builder.create();
	}
	
}
