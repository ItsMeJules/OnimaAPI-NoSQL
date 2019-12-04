package net.onima.onimaapi.event;

import net.onima.onimaapi.cooldown.utils.Cooldown;
import net.onima.onimaapi.players.OfflineAPIPlayer;

public class CooldownStartEvent extends CooldownEvent {
	
	public CooldownStartEvent(Cooldown cooldown, OfflineAPIPlayer apiPlayer) {
		super(cooldown, apiPlayer);
	}
	
}
