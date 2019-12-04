package net.onima.onimaapi.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.onima.onimaapi.event.ranks.RankEvent;
import net.onima.onimaapi.event.ranks.RankExpireEvent;
import net.onima.onimaapi.players.APIPlayer;

public class RankReceivedListener implements Listener {
	
	@EventHandler
	public void onRankReceived(RankEvent event) {
		if (event.shouldRecalculatePermissions() && !(event instanceof RankExpireEvent)) {
			APIPlayer apiPlayer = event.getAPIPlayer();
			
			apiPlayer.setupPermissions();
			apiPlayer.loadFromPerms();
		}
	}

}
