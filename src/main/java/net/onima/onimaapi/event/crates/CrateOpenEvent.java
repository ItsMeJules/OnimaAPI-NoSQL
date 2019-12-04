package net.onima.onimaapi.event.crates;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import net.onima.onimaapi.crates.booster.KeyBooster;
import net.onima.onimaapi.crates.prizes.Prize;
import net.onima.onimaapi.crates.utils.Crate;

public class CrateOpenEvent extends CrateEvent implements Cancellable {
	
	private List<Prize> rewards;
	private Player player;
	private KeyBooster booster;
	private boolean cancelled;
	
	public CrateOpenEvent(Crate crate, List<Prize> rewards, Player player, KeyBooster booster) {
		super(crate);
		this.rewards = rewards;
		this.player = player;
		this.booster = booster;
	}
	
	public List<Prize> getRewards() {
		return rewards;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public KeyBooster getBooster() {
		return booster;
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
