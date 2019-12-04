package net.onima.onimaapi.utils;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import net.minecraft.server.v1_7_R4.EntityFireworks;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_7_R4.World;
 
public class InstantFirework extends EntityFireworks {
    
	Player[] players;
    boolean gone;
 
    public InstantFirework(World world, Player... p) {
        super(world);
        players = p;
        this.a(0.25F, 0.25F);
    }
    
    @Override
    public void h() {
        if (gone)
            return;
 
        if (!world.isStatic) {
            gone = true;
 
            if (players != null) {
                if (players.length > 0) {
                    for (Player player : players)
                        (((CraftPlayer) player).getHandle()).playerConnection.sendPacket(new PacketPlayOutEntityStatus(this, (byte) 17));
 
                    die();
                    return;
                }
            }
 
            world.broadcastEntityEffect(this, (byte) 17);
            die();
        }
    }
 
    public static void spawn(Location location, FireworkEffect effect, Player... players) {
        try {
        	InstantFirework firework = new InstantFirework(((CraftWorld) location.getWorld()).getHandle(), players);
            FireworkMeta meta = ((Firework) firework.getBukkitEntity()).getFireworkMeta();
            
            meta.addEffect(effect);
            ((Firework) firework.getBukkitEntity()).setFireworkMeta(meta);
            firework.setPosition(location.getX(), location.getY(), location.getZ());
 
            if ((((CraftWorld) location.getWorld()).getHandle()).addEntity(firework))
                firework.setInvisible(true);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}