package net.onima.onimaapi.event;

import net.onima.onimaapi.cooldown.utils.Cooldown;
import net.onima.onimaapi.players.OfflineAPIPlayer;

public class CooldownExtendEvent extends CooldownEvent {
	
	private long oldTimeLeft;

	public CooldownExtendEvent(Cooldown cooldown, OfflineAPIPlayer apiPlayer, long oldTimeLeft) {
		super(cooldown, apiPlayer);
		this.oldTimeLeft = oldTimeLeft;
	}

	public long getOldTimeLeft() {
		return oldTimeLeft;
	}

}
