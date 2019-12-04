package net.onima.onimaapi.mod;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import net.onima.onimaapi.OnimaAPI;

public class SilentChest {
	
	public static void hook(OnimaAPI plugin) { //TODO Si le coffre est déjà ouvert quand un silent chest player l'ouvre le son de fermeture ne se joue pas pour les viewers qui l'avaient déjà ouvert
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, ListenerPriority.HIGH, PacketType.Play.Server.NAMED_SOUND_EFFECT) {
			
			@Override
			public void onPacketSending(PacketEvent event) {
				Player listener = event.getPlayer();
				String sound = event.getPacket().getStrings().read(0);
				
				if (!(sound.equalsIgnoreCase("random.chestopen") || sound.equalsIgnoreCase("random.chestclosed")))
					return;
		
				if (listener.hasMetadata("silent-open")) {
					event.setCancelled(true);
					
					if (sound.equalsIgnoreCase("random.chestclosed"))
						listener.removeMetadata("silent-open", plugin);
				}
			}

		});
	}
	
}
