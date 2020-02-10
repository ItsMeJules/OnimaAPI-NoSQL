package net.onima.onimaapi.gui.sign;

import org.bukkit.entity.Player;

public interface SignDoneEvent {

	/**
	 * Gets executed when a player is done editing the sign.
	 *
	 * @param player the player
	 * @param lines new content (string array)
	 */
	void onSignDone(Player player, String[] lines);
	
}
