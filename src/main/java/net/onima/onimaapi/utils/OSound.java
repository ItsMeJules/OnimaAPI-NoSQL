package net.onima.onimaapi.utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.onima.onimaapi.players.APIPlayer;

/**
 * This class is an easier way to handle and play sounds. 
 */
public class OSound {

	private Sound sound;
	private float pitch, volume;
	
	public OSound(Sound sound, float pitch, float volume) {
		this.sound = sound;
		this.pitch = pitch;
		this.volume = volume;
	}
	
	public Sound getSound() {
		return sound;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getVolume() {
		return volume;
	}
	
	public void play(Player player) {
		player.playSound(player.getLocation(), sound, volume, pitch);
	}
	
	public void play(APIPlayer apiPlayer) {
		play(apiPlayer.toPlayer());
	}
	
	public void play(Location location) {
		location.getWorld().playSound(location, sound, volume, pitch);
	}
	
}
