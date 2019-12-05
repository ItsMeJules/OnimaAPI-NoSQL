package net.onima.onimaapi.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.utils.PlayerOption;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.rank.RankType;

public class PrivateMessage {
	
	private CommandSender sender, receiver;
	private String message;
	private OSound sound;
	
	public PrivateMessage(CommandSender sender, CommandSender receiver) {
		this.sender = sender;
		this.receiver = receiver;
	}

	public PrivateMessage message(String message) {
		this.message = message;
		return this;
	}
	
	public PrivateMessage sound(OSound sound) {
		this.sound = sound;
		return this;
	}
	
	public String receiverFormat(RankType senderRank, RankType receiverRank) {
		return "§d(" + senderRank.getNameColor() + Methods.getName(sender, OnimaPerm.ONIMAAPI_DISGUISE_COMMAND_LIST.has(receiver)) + " §d-> " + receiverRank.getNameColor() + " Moi§d) " + senderRank.getSpeakingColor() + message;
	}
	
	public String senderFormat(RankType senderRank, RankType receiverRank) {
		return "§d(" + senderRank.getNameColor() + "Moi §d-> " + receiverRank.getNameColor() + Methods.getName(receiver, OnimaPerm.ONIMAAPI_DISGUISE_COMMAND_LIST.has(sender)) + "§d) " + senderRank.getSpeakingColor() + message;
	}
	
	public void send(RankType senderRank, RankType receiverRank) {
		ComponentBuilder receiverBuilder = new ComponentBuilder(receiverFormat(senderRank, receiverRank));
		ComponentBuilder senderBuilder = new ComponentBuilder(senderFormat(senderRank, receiverRank));
		
		senderBuilder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§oCliquez ici pour répondre à " + receiverRank.getNameColor() + Methods.getName(receiver, OnimaPerm.ONIMAAPI_DISGUISE_COMMAND_LIST.has(sender))).create()));
		receiverBuilder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§oCliquez ici pour répondre à " + senderRank.getNameColor() + Methods.getName(sender, OnimaPerm.ONIMAAPI_DISGUISE_COMMAND_LIST.has(receiver))).create()));
		
		senderBuilder.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/m " + Methods.getName(sender) + ' '));
		receiverBuilder.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/m " + Methods.getName(receiver) + ' '));
		
		if (sound != null && receiver instanceof Player) {
			APIPlayer apiPlayer = APIPlayer.getPlayer((Player) receiver);
			
			if (apiPlayer.getOptions().getBoolean(PlayerOption.GlobalOptions.SOUNDS))
				sound.play(apiPlayer);
		}
		
		Methods.sendJSON(receiver, receiverBuilder.create());
		Methods.sendJSON(sender, senderBuilder.create());
	}
	
	public static String spyFormat(String message, String senderName, String receiverName, RankType senderRank, RankType receiverRank) {
		return "§d(" + senderRank.getNameColor() + senderName + " §d-> " + receiverRank.getNameColor() + receiverName + "§d) " + senderRank.getSpeakingColor() + message;
	}
	
}
