package net.onima.onimaapi.event;

import org.bukkit.event.Cancellable;

import net.onima.onimaapi.cooldown.utils.Cooldown;
import net.onima.onimaapi.players.OfflineAPIPlayer;

public class CooldownUnPauseEvent extends CooldownEvent implements Cancellable {
	
	private boolean cancelled;

	public CooldownUnPauseEvent(Cooldown cooldown, OfflineAPIPlayer apiPlayer) {
		super(cooldown, apiPlayer);
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
