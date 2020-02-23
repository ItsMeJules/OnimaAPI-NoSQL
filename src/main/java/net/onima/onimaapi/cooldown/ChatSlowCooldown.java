package net.onima.onimaapi.cooldown;

import net.onima.onimaapi.cooldown.utils.Cooldown;
import net.onima.onimaapi.players.OfflineAPIPlayer;

public class ChatSlowCooldown extends Cooldown {

	public ChatSlowCooldown() {
		super("chat_slow", (byte) 18, 0);
	}

	@Override
	public String scoreboardDisplay(long timeLeft) {
		return null;
	}

	@Override
	public boolean action(OfflineAPIPlayer offline) {
		return false;
	}

}
