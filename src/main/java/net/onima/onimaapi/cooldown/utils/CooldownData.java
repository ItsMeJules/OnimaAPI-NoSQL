package net.onima.onimaapi.cooldown.utils;

import java.util.UUID;

import org.bukkit.Bukkit;

import net.onima.onimaapi.event.CooldownPauseEvent;
import net.onima.onimaapi.event.CooldownUnPauseEvent;
import net.onima.onimaapi.players.OfflineAPIPlayer;

public class CooldownData {
	
	private Cooldown cooldown;
	private UUID uuid;
	private long expireTime, timePaused;
	
	public CooldownData(Cooldown cooldown, UUID uuid) {
		this(cooldown, uuid, cooldown.getDuration());
	}
	
	public CooldownData(Cooldown cooldown, UUID uuid, long time) {
		this.cooldown = cooldown;
		this.uuid = uuid;
		
		setTimeLeft(time);
	}
	
	public void asNew() {
		timePaused = 0;
		setTimeLeft(cooldown.getDuration());
	}
	
	public long getTimeLeft(boolean ignorePaused) {
		if (!ignorePaused && isPaused())
			return timePaused;
		
		return expireTime - System.currentTimeMillis();
	}
	
	public long getTimeLeft() {
		return getTimeLeft(false);
	}
	
	public void setTimeLeft(long timeLeft) {
		if (isPaused())
			timePaused = timeLeft;
		
		expireTime = timeLeft + System.currentTimeMillis();
	}
	
	public boolean isPaused() {
		return timePaused != 0;
	}
	
	public void setTimePaused(long timePaused) {
		this.timePaused = timePaused;
	}
	
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
	
	public void pause(boolean pause) {
		if (pause) {
			OfflineAPIPlayer.getPlayer(uuid, offline -> {
				CooldownPauseEvent event = new CooldownPauseEvent(cooldown, offline);
				Bukkit.getPluginManager().callEvent(event);
	            
				if (event.isCancelled())
	                return;
				
				timePaused = getTimeLeft(true);
			});
		} else {
			OfflineAPIPlayer.getPlayer(uuid, offline -> {
	            CooldownUnPauseEvent event = new CooldownUnPauseEvent(cooldown, offline);
	            Bukkit.getPluginManager().callEvent(event);
	            
	            if (event.isCancelled())
	                return;
	            
	            expireTime = timePaused + System.currentTimeMillis();
				timePaused = 0;
			});
		}
	}
	
}
