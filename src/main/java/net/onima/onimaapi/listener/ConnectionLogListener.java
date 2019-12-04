package net.onima.onimaapi.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import net.onima.onimaapi.utils.ConnectionLog;

public class ConnectionLogListener implements Listener {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onLogin(AsyncPlayerPreLoginEvent event) {
		new ConnectionLog(event.getUniqueId(), event.getAddress().getHostAddress(), event.getLoginResult(),
		        event.getKickMessage(), System.currentTimeMillis()).sendToDatabase();
	}

}
