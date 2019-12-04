package net.onima.onimaapi.fakeblock;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.reflect.StructureModifier;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.players.APIPlayer;

public class FakeBlocksFix {
	
	public static void hook(OnimaAPI plugin) {
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_DIG, PacketType.Play.Client.BLOCK_PLACE) {
			@Override
			public void onPacketReceiving(PacketEvent event) {
				Player player = event.getPlayer();
				StructureModifier<Integer> modifier = event.getPacket().getIntegers();
				APIPlayer apiPlayer = APIPlayer.getPlayer(player);
				FakeBlock fb = apiPlayer.getFakeBlock(new Location(player.getWorld(), modifier.read(0), modifier.read(1), modifier.read(2)));
				
				try {
	     			if (fb != null) {
	     				event.setCancelled(true);
	     				fb.send(player);
	     			}
				} catch (FieldAccessException e) {
					e.printStackTrace();
				}
			}
		
		});
	}
	
}