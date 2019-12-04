package net.onima.onimaapi.event;

import net.onima.onimaapi.cooldown.utils.Cooldown;
import net.onima.onimaapi.players.OfflineAPIPlayer;

public class CooldownEndEvent extends CooldownEvent {

	public CooldownEndEvent(Cooldown cooldown, OfflineAPIPlayer apiPlayer) {
		super(cooldown, apiPlayer);
	}

}
