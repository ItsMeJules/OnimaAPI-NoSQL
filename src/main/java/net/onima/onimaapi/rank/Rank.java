package net.onima.onimaapi.rank;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.bson.Document;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.mongo.saver.MongoSerializer;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.workload.type.RankWorkload;

public class Rank implements MongoSerializer {

	private OfflineAPIPlayer offlineAPIPlayer;
	private RankType rankType;
	private Instant expireTime;
	
	public Rank(OfflineAPIPlayer offlineAPIPlayer, RankType rankType, Long expireTime) {
		this.offlineAPIPlayer = offlineAPIPlayer;
		this.rankType = rankType;
		this.expireTime = expireTime != null ? Instant.now().plusMillis(expireTime) : null;
		
		if (expireTime != null && offlineAPIPlayer.isOnline())
			OnimaAPI.getDistributor().get(OnimaAPI.getInstance().getWorkloadManager().getRanksId()).addWorkload(new RankWorkload((APIPlayer) offlineAPIPlayer));
	}
	
	public Rank(OfflineAPIPlayer offlineAPIPlayer, RankType rankType) {
		this(offlineAPIPlayer, rankType, rankType.getTime() != Integer.MAX_VALUE ? rankType.getTime() : null);
	}
	
	public boolean isTemporary() {
		return expireTime != null;
	}
	
	public Instant getInstant() {
		return expireTime;
	}
	
	public void setInstant(Instant instant) {
		expireTime = instant;
	}
	
	public void setRankDuration(Long time) {
		expireTime = time != null ? Instant.now().plusMillis(time) : null;
	}
	
	public long getTimeLeft() {
		return Instant.now().until(expireTime, ChronoUnit.MILLIS);
	}
	
	public RankType getRankType() {
		return rankType;
	}

	public OfflineAPIPlayer getOfflinePlayer() {
		return offlineAPIPlayer;
	}
	
	@Override
	public Document getDocument(Object... objects) {
		return new Document("rank_type", rankType.name()).append("expire", isTemporary() ? expireTime.toEpochMilli() : null);
	}

}
