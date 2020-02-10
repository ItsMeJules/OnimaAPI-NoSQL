package net.onima.onimaapi.gui.sign.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.gui.sign.InputSign;
import net.onima.onimaapi.gui.sign.SignDoneEvent;

public class SignHandler {
	
	public static void hook(OnimaAPI plugin) {
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.UPDATE_SIGN) {
			@Override
			public void onPacketReceiving(PacketEvent event) {
				Player player = event.getPlayer();
				PacketContainer packet = event.getPacket();
				StructureModifier<Integer> ints = packet.getIntegers();
				Location loc = InputSign.getSignLocations().remove(player.getUniqueId());
				
				if (loc == null
						|| ints.read(0) != loc.getBlockX()
						|| ints.read(1) != loc.getBlockY()
						|| ints.read(2) != loc.getBlockZ()) {
					return;
				}
				
				SignDoneEvent doneEvent = InputSign.getListeners().remove(player.getUniqueId());
				
				if (doneEvent != null) {
					event.setCancelled(true);
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> doneEvent.onSignDone(player, packet.getStringArrays().read(0)));
				}
			}
		
		});
	}

}
