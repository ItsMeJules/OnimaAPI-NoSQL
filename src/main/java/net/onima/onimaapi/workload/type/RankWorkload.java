package net.onima.onimaapi.workload.type;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

import net.onima.onimaapi.event.ranks.RankExpireEvent;
import net.onima.onimaapi.event.ranks.RankExpiredEvent;
import net.onima.onimaapi.event.ranks.RankReceivedEvent;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.Rank;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.OSound;
import net.onima.onimaapi.utils.time.Time.LongTime;
import net.onima.onimaapi.workload.Workload;

public class RankWorkload implements Workload {
	
	private static final OSound RANK_LOST_SOUND, RANK_ALERT_SOUND;
	
	static {
		RANK_LOST_SOUND = new OSound(Sound.CAT_HISS, 0.5F, 1F);
		RANK_ALERT_SOUND = new OSound(Sound.CAT_HIT, 0.5F, 1F);
	}

	private APIPlayer apiPlayer;
	private boolean reschedule;
	
	{
		reschedule = true;
	}
	
	public RankWorkload(APIPlayer apiPlayer) {
		this.apiPlayer = apiPlayer;
	}

	@Override
	public void compute() {
		Rank rank = apiPlayer.getRank();
		Instant now = Instant.now();
		long secondsLeft = now.until(rank.getInstant(), ChronoUnit.SECONDS);
		
		if (secondsLeft <= 0) {
			RankExpireEvent expire = new RankExpireEvent(rank, apiPlayer, false);
			Bukkit.getPluginManager().callEvent(expire);
			
			if (!expire.isCancelled()) {
				apiPlayer.setRank(new Rank(apiPlayer, ConfigurationService.DEFAULT_RANK));
				Bukkit.getPluginManager().callEvent(new RankReceivedEvent(rank, apiPlayer, true, null));
			} else {
				reschedule = false;
				return;
			}
			
			
			RankExpiredEvent event = new RankExpiredEvent(rank, apiPlayer, true);
			Bukkit.getPluginManager().callEvent(event);
			
			if (event.isNotify()) {
				apiPlayer.sendMessage("§cVotre rank " + rank.getRankType().getName() + " §ca expiré !");
				RANK_LOST_SOUND.play(apiPlayer);
			}
			
			reschedule = false;
			return;
		}
		
		if (secondsLeft == 604800
				|| secondsLeft == 86400
				|| secondsLeft == 3600 
				|| secondsLeft == 60 
				|| secondsLeft == 5) {
			apiPlayer.sendMessage("§cVotre rank " + rank.getRankType().getName() + " §cexpirera dans §a" + LongTime.setYMDWHMSFormat(rank.getTimeLeft()) + "§c.");
			RANK_ALERT_SOUND.play(apiPlayer);
		}
	}
	
	@Override
	public boolean reschedule() {
		return reschedule;
	}
	
	@Override
	public boolean shouldExecute() {
		return apiPlayer.getRank().isTemporary();
	}
	
}
