package net.onima.onimaapi.event.crates;

import java.util.List;

import net.onima.onimaapi.crates.prizes.Prize;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.players.APIPlayer;

public class CrateWinEvent extends CrateEvent {
	
	private List<Prize> prizes;
	private APIPlayer apiPlayer;

	public CrateWinEvent(Crate crate, List<Prize> prizes, APIPlayer apiPlayer) {
		super(crate);
		this.prizes = prizes;
		this.apiPlayer = apiPlayer;
	}

	public List<Prize> getPrizes() {
		return prizes;
	}
	
	public APIPlayer getAPIPlayer() {
		return apiPlayer;
	}
	
	
}
