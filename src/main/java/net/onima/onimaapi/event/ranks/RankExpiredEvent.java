package net.onima.onimaapi.event.ranks;

import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.Rank;

public class RankExpiredEvent extends RankEvent {

	private boolean notify;
	
	{
		notify = true;
	}
	
	public RankExpiredEvent(Rank rank, APIPlayer apiPlayer, boolean recalculatePermissions) {
		super(rank, apiPlayer, recalculatePermissions);
	}

	public boolean isNotify() {
		return notify;
	}

	public void setNotify(boolean notify) {
		this.notify = notify;
	}
	
}
