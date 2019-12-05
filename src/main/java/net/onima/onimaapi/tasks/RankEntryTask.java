package net.onima.onimaapi.tasks;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.event.ranks.RankExpireEvent;
import net.onima.onimaapi.event.ranks.RankExpiredEvent;
import net.onima.onimaapi.event.ranks.RankReceivedEvent;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.Rank;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.OSound;
import net.onima.onimaapi.utils.TaskPerEntry;
import net.onima.onimaapi.utils.time.Time.LongTime;

public class RankEntryTask extends TaskPerEntry<APIPlayer> {
	
	private static final OSound RANK_LOST_SOUND, RANK_ALERT_SOUND;

	private static RankEntryTask rankTask;
	
	static {
		RANK_LOST_SOUND = new OSound(Sound.CAT_HISS, 0.5F, 1F);
		RANK_ALERT_SOUND = new OSound(Sound.CAT_HIT, 0.5F, 1F);
	}

	public RankEntryTask() {
		super(200);
	}

	@Override
	public void run(Iterator<APIPlayer> iterator) {
		while (iterator.hasNext()) {
			APIPlayer player = iterator.next();
			Rank rank = player.getRank();
			
			if (rank.isTemporary()) {
				Instant now = Instant.now();
				long secondsLeft = now.until(rank.getInstant(), ChronoUnit.SECONDS);
				
				if (secondsLeft <= 0) {
					if (!onExpire(iterator, rank, player))
						continue;
					
					RankExpiredEvent event = new RankExpiredEvent(rank, player, true);
					Bukkit.getPluginManager().callEvent(event);
					
					if (event.isNotify()) {
						player.sendMessage("§cVotre rank " + rank.getRankType().getName() + " §ca expiré !");
						RANK_LOST_SOUND.play(player);
					}
					
					iteratorRemove(iterator);
					continue;
				}
				
				if (secondsLeft == 604800
						|| secondsLeft == 86400
						|| secondsLeft == 3600 
						|| secondsLeft == 60 
						|| secondsLeft == 5) {
					player.sendMessage("§cVotre rank " + rank.getRankType().getName() + " §cexpirera dans §a" + LongTime.setYMDWHMSFormat(rank.getTimeLeft()) + "§c.");
					RANK_ALERT_SOUND.play(player);
				}
			}
		}
	}
	
	
	private boolean onExpire(Iterator<APIPlayer> iterator, Rank rank, APIPlayer player) {
		RankExpireEvent expire = new RankExpireEvent(rank, player, false);
		Bukkit.getPluginManager().callEvent(expire);
		
		if (expire.isCancelled())
			return false;
		
		player.setRank(new Rank(player, ConfigurationService.DEFAULT_RANK));
		Bukkit.getPluginManager().callEvent(new RankReceivedEvent(rank, player, true, null));
		
		iteratorRemove(iterator);
		return true;
	}
	
	public static void init(OnimaAPI plugin) {
		rankTask = new RankEntryTask();
		
		rankTask.task(task -> task.runTaskTimerAsynchronously(plugin, 0L, 20L));
		rankTask.insert(APIPlayer.getOnlineAPIPlayers());
	}
	
	public static RankEntryTask get() {
		return rankTask;
	}

}
