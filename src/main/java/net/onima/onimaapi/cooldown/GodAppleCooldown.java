package net.onima.onimaapi.cooldown;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.cooldown.utils.Cooldown;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.utils.time.Time;
import net.onima.onimaapi.utils.time.Time.LongTime;

public class GodAppleCooldown extends Cooldown implements Listener {
	
	public GodAppleCooldown() {
		super("god_apple", (byte) 4, 3 * Time.HOUR);
	}

	@Override
	public String scoreboardDisplay(long timeLeft) {
		return "§6Pomme de notch : §c" + LongTime.setHMSFormat(timeLeft);
	}

	@Override
	public boolean action(OfflineAPIPlayer offline) {
		return false;
	}
	
	@Override
	public void onStart(OfflineAPIPlayer offline) {
		if (offline.isOnline()) {
			APIPlayer apiPlayer = (APIPlayer) offline;
			
			apiPlayer.sendMessage("§c\u2588\u2588\u2588\u2588\u2588§c\u2588\u2588\u2588");
			apiPlayer.sendMessage("§c\u2588\u2588\u2588§e\u2588\u2588§c\u2588\u2588\u2588");
			apiPlayer.sendMessage("§c\u2588\u2588\u2588§e\u2588§c\u2588\u2588\u2588\u2588 §6§lPomme de Notch");
			apiPlayer.sendMessage("§c\u2588\u2588§6\u2588\u2588\u2588\u2588§c\u2588\u2588 §7  Consumée");
			apiPlayer.sendMessage("§c\u2588§6\u2588\u2588§f\u2588§6\u2588§6\u2588\u2588§c\u2588");
			apiPlayer.sendMessage("§c\u2588§6\u2588§f\u2588§6\u2588§6\u2588§6\u2588\u2588§c\u2588 §6Cooldown :");
			apiPlayer.sendMessage("§c\u2588§6\u2588\u2588§6\u2588§6\u2588§6\u2588\u2588§c\u2588    §7" + LongTime.setYMDWHMSFormat(duration));
			apiPlayer.sendMessage("§c\u2588§6\u2588\u2588§6\u2588§6\u2588§6\u2588\u2588§c\u2588");
			apiPlayer.sendMessage("§c\u2588\u2588§6\u2588\u2588\u2588\u2588§c\u2588\u2588");
			apiPlayer.sendMessage("§c\u2588\u2588\u2588\u2588\u2588§c\u2588\u2588\u2588");
		}
		
		super.onStart(offline);
	}
	
	@Override
	public void onExpire(OfflineAPIPlayer offline) {
		if (offline.isOnline())
			((APIPlayer) offline).sendMessage("§aVous pouvez de nouveau manger une pomme de notch !");
		
		super.onExpire(offline);
	}
	
	@EventHandler
	public void onItemConsume(PlayerItemConsumeEvent event) {
		ItemStack stack = event.getItem();
		if (stack != null && stack.getType() == Material.GOLDEN_APPLE && stack.getDurability() == 1) {
			Player player = event.getPlayer();
			
			if (getTimeLeft(player.getUniqueId()) > 0L) {
				event.setCancelled(true);
				player.sendMessage("§cDésolé, vous devez attendre " + LongTime.setDHMSFormat(getTimeLeft(player.getUniqueId())) + " pour pouvoir utiliser une autre pomme de Notch.");
			} else
				onStart(APIPlayer.getPlayer(player));
		}
	}

}
