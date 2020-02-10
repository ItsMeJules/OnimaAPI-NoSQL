package net.onima.onimaapi.gui.sign;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;

public class InputSign {
	
	protected static final Map<UUID, SignDoneEvent> listeners = new ConcurrentHashMap<>();
	protected static final Map<UUID, Location> signLocations = new ConcurrentHashMap<>();

	protected Player player;
	
	public InputSign(Player player) {
		this.player = player;
	}
	
	public void open(SignDoneEvent event) {
		Location loc = player.getLocation();
		
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.OPEN_SIGN_ENTITY);
        packet.getIntegers().write(0, 0).write(2, 0);
        loc.setX(0);
        loc.setY(0);
        loc.setZ(0);
        
        try {
        	ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    		listeners.put(player.getUniqueId(), event);
    		signLocations.put(player.getUniqueId(), loc);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
	}

	public void destroy() {
		listeners.remove(player.getUniqueId());
		signLocations.remove(player.getUniqueId());
	}

	public static Map<UUID, SignDoneEvent> getListeners() {
		return listeners;
	}

	public static Map<UUID, Location> getSignLocations() {
		return signLocations;
	}

}
