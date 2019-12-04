package net.onima.onimaapi.listener;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.onima.onimaapi.disguise.DisguiseManager;
import net.onima.onimaapi.event.chat.ChatEvent;
import net.onima.onimaapi.event.chat.PrivateMessageEvent;
import net.onima.onimaapi.players.APIPlayer;

public class DisguiseListener implements Listener {
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		StringBuilder newCommand = new StringBuilder();
		
		for (String word : event.getMessage().split(" ")) {
			if (DisguiseManager.getDisguisedPlayers().containsKey(word))
				word = DisguiseManager.getDisguisedPlayers().get(word);
			
			newCommand.append(word).append(' ');
		}
		
		event.setMessage(newCommand.toString());
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPrivateMessage(PrivateMessageEvent event) {
		CommandSender reader = event.getReceiver();
		
		if (reader instanceof Player) {
			APIPlayer apiPlayer = APIPlayer.getPlayer((Player) reader);
			
			if (apiPlayer.getDisguiseManager().isDisguised())
				event.setReaderRank(apiPlayer.getDisguiseManager().getRankType());
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onChat(ChatEvent event) {
		CommandSender sender = event.getSender();
		
		if (sender instanceof Player) {
			APIPlayer apiPlayer = APIPlayer.getPlayer((Player) sender);
			
			if (apiPlayer.getDisguiseManager().isDisguised())
				event.setRankType(apiPlayer.getDisguiseManager().getRankType());
		}
	}

}
