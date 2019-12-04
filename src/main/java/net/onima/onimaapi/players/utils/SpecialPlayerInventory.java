package net.onima.onimaapi.players.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftInventory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.google.common.base.Predicate;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.PlayerInteractManager;
import net.minecraft.server.v1_7_R4.PlayerInventory;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.caching.Cache;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.time.Time;

public class SpecialPlayerInventory extends PlayerInventory {
	
	private static final Map<UUID, SpecialPlayerInventory> inventories = new HashMap<>();
	private static final Cache<UUID, Player> playerCache = new Cache<>(30 * Time.SECOND,
            new Predicate<Player>() {
		
				@Override
				public boolean apply(final Player value) {
					UUID uuid = value.getUniqueId();
					
					return inventories.containsKey(uuid) && inventories.get(uuid).isInUse();
				}
				
			}, new Predicate<Player>() {
				
		@Override
		public boolean apply(final Player value) {
			UUID uuid = value.getUniqueId();
			
			if (inventories.containsKey(uuid)) {
				Inventory inv = inventories.remove(uuid).getBukkitInventory();
				List<HumanEntity> viewers = inv.getViewers();
			
				for (HumanEntity entity : viewers.toArray(new HumanEntity[viewers.size()]))
					entity.closeInventory();
			}

			if (!value.isOnline())
				value.saveData();
			
			return true;
		}
	});
	
    private final ItemStack[] extra = new ItemStack[5];
    private final CraftInventory inventory = new CraftInventory(this);
    private boolean online;

    public SpecialPlayerInventory(Player player, boolean online) {
        super(((CraftPlayer) player).getHandle());
        this.online = online;
        this.items = super.player.inventory.items;
        this.armor = super.player.inventory.armor;
    }

    public Inventory getBukkitInventory() {
        return inventory;
    }
    
    public void setPlayerOnline(Player player) {
        if (!online) {
            this.player = ((CraftPlayer) player).getHandle();
            this.player.inventory.items = this.items;
            this.player.inventory.armor = this.armor;
            online = true;
        }
    }

    public void setPlayerOffline() {
    	online = false;
    }

    public boolean isInUse() {
        return !this.getViewers().isEmpty();
    }

    @Override
    public ItemStack[] getContents() {
        ItemStack[] contents = new ItemStack[getSize()];
        System.arraycopy(items, 0, contents, 0, items.length);
        System.arraycopy(armor, 0, contents, items.length, armor.length);
        return contents;
    }

    @Override
    public int getSize() {
        return super.getSize() + 5;
    }

    @Override
    public ItemStack getItem(int i) {
        ItemStack[] is = this.items;

        if (i >= is.length) {
            i -= is.length;
            is = this.armor;
        } else
            i = getReversedItemSlotNum(i);

        if (i >= is.length) {
            i -= is.length;
            is = this.extra;
        } else if (is == this.armor)
            i = getReversedArmorSlotNum(i);

        return is[i];
    }

    @Override
    public ItemStack splitStack(int i, int j) {
        ItemStack[] is = this.items;

        if (i >= is.length) {
            i -= is.length;
            is = this.armor;
        } else
            i = getReversedItemSlotNum(i);

        if (i >= is.length) {
            i -= is.length;
            is = this.extra;
        } else if (is == this.armor)
            i = getReversedArmorSlotNum(i);

        if (is[i] != null) {
            ItemStack itemstack;

            if (is[i].count <= j) {
                itemstack = is[i];
                is[i] = null;
                return itemstack;
            } else {
                itemstack = is[i].a(j);
                if (is[i].count == 0) {
                    is[i] = null;
                }

                return itemstack;
            }
        }

        return null;
    }

    @Override
    public ItemStack splitWithoutUpdate(int i) {
        ItemStack[] is = this.items;

        if (i >= is.length) {
            i -= is.length;
            is = this.armor;
        } else {
            i = getReversedItemSlotNum(i);
        }

        if (i >= is.length) {
            i -= is.length;
            is = this.extra;
        } else if (is == this.armor) {
            i = getReversedArmorSlotNum(i);
        }

        if (is[i] != null) {
            ItemStack itemstack = is[i];

            is[i] = null;
            return itemstack;
        }

        return null;
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        ItemStack[] is = this.items;

        if (i >= is.length) {
            i -= is.length;
            is = this.armor;
        } else {
            i = getReversedItemSlotNum(i);
        }

        if (i >= is.length) {
            i -= is.length;
            is = this.extra;
        } else if (is == this.armor) {
            i = getReversedArmorSlotNum(i);
        }

        // Effects
        if (is == this.extra) {
            player.drop(itemstack, true);
            itemstack = null;
        }

        is[i] = itemstack;

        player.defaultContainer.b();
    }

    private int getReversedItemSlotNum(int i) {
        if (i >= 27) {
            return i - 27;
        }
        return i + 9;
    }

    private int getReversedArmorSlotNum(int i) {
        if (i == 0) {
            return 3;
        }
        if (i == 1) {
            return 2;
        }
        if (i == 2) {
            return 1;
        }
        if (i == 3) {
            return 0;
        }
        return i;
    }

    public String getName() {
        if (player.getName().length() > 16)
            return player.getName().substring(0, 16);

        return Methods.getRealName((OfflinePlayer) player);
    }

    public boolean hasCustomName() {
        return true;
    }
    
    private static Player loadPlayer(OfflinePlayer offline) {
    	if (!offline.hasPlayedBefore())
    		return null;
    	
    	UUID uuid = offline.getUniqueId();
    	
    	if (playerCache.containsKey(uuid))
    		return playerCache.get(uuid);

    	Player target;
    	
    	if (offline.isOnline()) {
    		target = offline.getPlayer();
    		playerCache.put(uuid, target);
    		return target;
    	}
    	
    	GameProfile profile = new GameProfile(offline.getUniqueId(), offline.getName());
    	MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
    	EntityPlayer entity = new EntityPlayer(server, server.getWorldServer(0), profile, new PlayerInteractManager(server.getWorldServer(0)));
    
    	target = entity.getBukkitEntity();
         
    	if (target != null)
    		target.loadData();
         
    	return target;
    }
    
    public static SpecialPlayerInventory createInventory(OfflinePlayer offlinePlayer, boolean online) {
    	UUID uuid = offlinePlayer.getUniqueId();
    	
    	if (inventories.containsKey(uuid))
    		return inventories.get(uuid);
    	
    	Player player = loadPlayer(offlinePlayer);
    	SpecialPlayerInventory inventory = new SpecialPlayerInventory(player, online);
    	
    	inventories.put(uuid, inventory);
    	playerCache.put(uuid, player);
    	
    	return inventory;
    }
    
    public static void onConnect(Player player) {
    	UUID uuid = player.getUniqueId();
    	
    	if (!playerCache.containsKey(uuid))
    		return;
    		
    	playerCache.put(uuid, player);
    	
    	if (inventories.containsKey(uuid)) {
    		inventories.get(uuid).setPlayerOnline(player);
    		Bukkit.getScheduler().runTask(OnimaAPI.getInstance(), () -> {
    			if (player.isOnline())
    				player.updateInventory();
    		});
    	}
    }
    
    public static void onDisconnect(Player player) {
    	UUID uuid = player.getUniqueId();
    	
    	if (!playerCache.containsKey(uuid))
    		return;
    	
    	if (inventories.containsKey(uuid))
    		inventories.get(uuid).setPlayerOffline();
    }
    
}
