//package net.onima.onimaapi.hologram;
//
//import java.lang.reflect.InvocationTargetException;
//
//import org.bukkit.Location;
//import org.bukkit.entity.EntityType;
//import org.bukkit.entity.Player;
//
//import com.comphenix.protocol.PacketType;
//import com.comphenix.protocol.ProtocolLibrary;
//import com.comphenix.protocol.events.PacketContainer;
//import com.comphenix.protocol.wrappers.WrappedDataWatcher;
//
//public class NameTagSpawner {
//    private static final int WITHER_SKULL = 66;
//    
//    // Shared entity ID allocator
//    private static int SHARED_ENTITY_ID = Short.MAX_VALUE;
//
//    // The starting entity ID
//    private int startEntityId;
//    private int nameTagCount;
//    
//    /**
//     * Construct a new name tag spawner.
//     * <p>
//     * Specify a number of name tags to spawn.
//     * @param nameTags - the maximum number of name tags we will spawn at any given time.
//     */
//    public NameTagSpawner(int nameTagCount) {
//        this.startEntityId = SHARED_ENTITY_ID;
//        this.nameTagCount = nameTagCount;
//        
//        // We need to reserve two entity IDs per name tag 
//        SHARED_ENTITY_ID += nameTagCount * 2; 
//    }
//    
//    /**
//     * Retrieve the maximum number of name tags we can spawn.
//     * @return The maximum number.
//     */
//    public int getNameTagCount() {
//        return nameTagCount;
//    }
//    
//    /**
//     * Set the location and message of a name tag.
//     * @param index - index of the name tag. Cannot exceeed {@link #getNameTagCount()}.
//     * @param observer - the observing player.
//     * @param location - the location in the same world as the player.
//     * @param dY - Y value to add to the final location.
//     * @param message - the message to display.
//     */
//    public void setNameTag(int index, Player observer, Location location, double dY, String message) {
//    	PacketContainer attach = new PacketContainer(PacketType.Play.Server.ATTACH_ENTITY);
//    	PacketContainer horse = createHorsePacket(index, location, dY, message);
//        PacketContainer skull = createSkullPacket(index, location, dY);
//
//        attach.getModifier().writeDefaults();
//        
//        // The horse is riding on the skull
//        attach.getIntegers().write(1, horse.getIntegers().read(0));
//        attach.getIntegers().write(2, skull.getIntegers().read(0));
//       
//        try {
//			ProtocolLibrary.getProtocolManager().sendServerPacket(observer, horse);
//			ProtocolLibrary.getProtocolManager().sendServerPacket(observer, skull);
//			ProtocolLibrary.getProtocolManager().sendServerPacket(observer, attach);
//		} catch (InvocationTargetException e) {
//			throw new RuntimeException("Cannot send packet.", e);
//		}
//    }
//
//    // Construct the invisible horse packet
//    @SuppressWarnings("deprecation")
//	private PacketContainer createHorsePacket(int index, Location location, double dY, String message) {
//    	PacketContainer horse = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
//    	
//        horse.getIntegers().write(0, startEntityId + index * 2);
//        horse.getIntegers().write(1, (int) EntityType.HORSE.getTypeId());
//        horse.getIntegers().write(2, (int) Math.floor(location.getX() * 32.0D));
//        horse.getIntegers().write(3, (int) Math.floor(location.getY() + dY + 55 * 32.0D));
//        horse.getIntegers().write(4, (int) Math.floor(location.getZ() * 32.0D));
//
//        WrappedDataWatcher wdw = new WrappedDataWatcher();
//        
//        wdw.setObject(10, message);
//        wdw.setObject(11, (byte) 1);
//        wdw.setObject(12, -1700000);
//        horse.getDataWatcherModifier().write(0, wdw);
//        
//        return horse;
//    }
//    
//    // Construct the wither skull packet
//    private PacketContainer createSkullPacket(int index, Location location, double dY) {
//    	PacketContainer skull = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
//    	
//    	skull.getModifier().writeDefaults();
//        skull.getIntegers().write(0, startEntityId + index * 2 + 1);
//        skull.getIntegers().write(9, WITHER_SKULL);
//        skull.getIntegers().write(1, (int) Math.floor(location.getX() * 32.0D));
//        skull.getIntegers().write(2, (int) Math.floor(location.getY() + dY + 55 * 32.0D));
//        skull.getIntegers().write(3, (int) Math.floor(location.getZ() * 32.0D));
//        
//        return skull;
//    }
//    
//}