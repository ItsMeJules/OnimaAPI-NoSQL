package net.onima.onimaapi.event;

import net.onima.onimaapi.cooldown.utils.Cooldown;
import net.onima.onimaapi.players.OfflineAPIPlayer;

public class CooldownStopEvent extends CooldownEndEvent {

	public CooldownStopEvent(Cooldown cooldown, OfflineAPIPlayer apiPlayer) {
		super(cooldown, apiPlayer);
	}

}
