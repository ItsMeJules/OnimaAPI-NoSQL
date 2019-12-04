package net.onima.onimaapi.players.utils;

public class PlayTime {
	
	private long lastPlayTimeUpdate, playTime;
	
	public PlayTime(long connectionTime, long playTime) {
		lastPlayTimeUpdate = connectionTime;
		this.playTime = playTime;
	}
	
	public long getPlayTime() {
		long now = System.currentTimeMillis();
		playTime += now - lastPlayTimeUpdate;
		lastPlayTimeUpdate = now;
		
		return playTime;
	}
	
	public void setLastPlayTimeUpdate(long connectionTime) {
		lastPlayTimeUpdate = connectionTime;
	}
	
	public void setPlayTime(long playTime) {
		this.playTime = playTime;
	}

}
