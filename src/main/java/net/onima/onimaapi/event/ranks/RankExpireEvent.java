package net.onima.onimaapi.event.ranks;

import org.bukkit.event.Cancellable;

import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.Rank;

public class RankExpireEvent extends RankEvent implements Cancellable {
	
	private boolean cancel;

	public RankExpireEvent(Rank rank, APIPlayer apiPlayer, boolean recalculatePermissions) {
		super(rank, apiPlayer, recalculatePermissions);
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

}
