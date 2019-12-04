package net.onima.onimaapi.tasks;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.cooldown.utils.Cooldown;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.utils.TaskPerEntry;

public class CooldownEntryTask extends TaskPerEntry<OfflineAPIPlayer> {
	
	private static CooldownEntryTask cooldownTask;

	public CooldownEntryTask() {
		super(200);
	}

	@Override
	public void run(List<OfflineAPIPlayer> list) {
		for (OfflineAPIPlayer offline : list) {
			Iterator<Cooldown> iterator = offline.getCooldowns().iterator();
			
			while (iterator.hasNext()) {
				Cooldown cooldown = iterator.next();
				
				if (cooldown.getTimeLeft(offline.getUUID()) <= 0L) {
					iterator.remove();
					Bukkit.getScheduler().runTask(OnimaAPI.getInstance(), () -> cooldown.onExpire(offline));
				}
			}
		}
	}
	
	public static void init(OnimaAPI plugin) {
		cooldownTask = new CooldownEntryTask();
		
		cooldownTask.task(task -> task.runTaskTimerAsynchronously(plugin, 0L, 1L));
		cooldownTask.insert(OfflineAPIPlayer.getDisconnectedOfflineAPIPlayers());
	}
	
	public static CooldownEntryTask get() {
		return cooldownTask;
	}

}
