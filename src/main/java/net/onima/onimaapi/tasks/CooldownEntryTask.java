package net.onima.onimaapi.tasks;

import java.util.Iterator;

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
	public void run(Iterator<OfflineAPIPlayer> iterator) {
		while (iterator.hasNext()) {
			OfflineAPIPlayer offline = iterator.next();
			Iterator<Cooldown> cooldownIterator = offline.getCooldowns().iterator();
			
			while (cooldownIterator.hasNext()) {
				Cooldown cooldown = cooldownIterator.next();
				
				if (cooldown.getTimeLeft(offline.getUUID()) <= 50L) { //Milliseconds for a tick, it's done now as I have to resync it to call onExpire.
					cooldownIterator.remove();
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
