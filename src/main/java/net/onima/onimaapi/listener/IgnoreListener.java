package net.onima.onimaapi.listener;

import java.util.Iterator;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.onima.onimaapi.event.chat.ChatEvent;
import net.onima.onimaapi.players.APIPlayer;

public class IgnoreListener implements Listener {
	
	@EventHandler
	public void onChat(ChatEvent event) {
		if (!(event.getSender() instanceof Player))
			return;
		
		UUID uuid = ((Player) event.getSender()).getUniqueId(); 
		Iterator<CommandSender> iterator = event.getReaders().iterator();
		
		while (iterator.hasNext()) {
			CommandSender sender = iterator.next();
		
			if (!(sender instanceof Player))
				continue;
			
			APIPlayer apiPlayer = APIPlayer.getPlayer((Player) sender);
			
			if (apiPlayer.getIgnored().contains(uuid))
				iterator.remove();
		}
		
	}

}
