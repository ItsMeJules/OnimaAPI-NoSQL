package net.onima.onimaapi.caching;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import net.onima.onimaapi.disguise.DisguiseManager;
import net.onima.onimaapi.players.APIPlayer;

public class UUIDCache {
	
	private static Map<String, UUID> nameToUuid;
	
	static {
		nameToUuid = new HashMap<>();
	}

	public static UUID getUUID(String name) {
		if (DisguiseManager.getDisguisedPlayers().containsKey(name))
			name = DisguiseManager.getDisguisedPlayers().get(name);
		
		if (nameToUuid.containsKey(name.toLowerCase()))
			return nameToUuid.get(name.toLowerCase());

		return null;
	}

	public static void load() {
		for (OfflinePlayer offline : Bukkit.getOfflinePlayers()) {
			String name = offline.getName();
			
			if (offline.isOnline())
				name = APIPlayer.getPlayer(offline.getUniqueId()).getName();
			
			if (name == null)
				continue;
			
			nameToUuid.put(name.toLowerCase(), offline.getUniqueId());
		}
	}
	
	public static void update(String name, UUID uuid) {
		nameToUuid.put(name, uuid);
	}

}
