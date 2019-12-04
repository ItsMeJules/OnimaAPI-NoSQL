package net.onima.onimaapi.gui.menu.utils;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.minecraft.server.v1_7_R4.ContainerAnvil;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.event.menu.PacketMenuOpenEvent;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.PacketMenuType;
import net.onima.onimaapi.gui.PacketStaticMenu;
import net.onima.onimaapi.players.APIPlayer;

public abstract class AnvilMenu extends PacketMenu {
	
	protected UUID uuid;
	protected HashMap<AnvilSlot, ItemStack> items;
	protected Listener listener;
	protected AnvilClickEventHandler handler;
	
	{
		items = new HashMap<>();
	}

	public AnvilMenu(UUID uuid, String id, String title) {
		super(id, title, PacketMenuType.ANVIL, false);
		
		this.uuid = uuid;
	}

	@Override
	public void open(APIPlayer apiPlayer) {
		PacketMenuOpenEvent event = new PacketMenuOpenEvent(this, apiPlayer);
		Bukkit.getPluginManager().callEvent(event);
		
		if (event.isCancelled())
			return;
		
		if (apiPlayer.getViewingMenu() != null)
			apiPlayer.getViewingMenu().close(apiPlayer, false);
		
		Player player = apiPlayer.toPlayer();
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		container = (AnvilContainer) CraftEventFactory.callInventoryOpenEvent(entityPlayer, new AnvilContainer(entityPlayer));
		
		if (container == null)
			return;
		
        inventory = container.getBukkitView().getTopInventory();
        
        if (!(this instanceof PacketStaticMenu))
        	updateItems(player);

        int counter = entityPlayer.nextContainerCounter();
        sendPackets(entityPlayer, counter);
		
		handler = handler();
		startListening();
		
		apiPlayer.setViewingMenu(this);
		viewers.add(apiPlayer.getUUID());
	}
	
	@Override
	public void updateItems(Player player) {
		inventory.clear();
        for (Entry<AnvilSlot, ItemStack> entry : items.entrySet())
            inventory.setItem(entry.getKey().slot, entry.getValue());
	}
	
	public abstract AnvilClickEventHandler handler();
	
	public void startListening() {
		listener = new Listener() {
			
            @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
            public void onInventoryClick(InventoryClickEvent event) {
                if (event.getWhoClicked() instanceof Player) {
                    Player clicker = (Player) event.getWhoClicked();

                    if (event.getClickedInventory().equals(inventory)) {
                        event.setCancelled(true);
                        
                        ItemStack item = event.getCurrentItem();
                        String name = "";
                        
                        if (item != null) {
                            if (item.hasItemMeta()) {
                                ItemMeta meta = item.getItemMeta();

                                if (meta.hasDisplayName())
                                    name = meta.getDisplayName();
                            }
                        }

                        AnvilClickEvent clickEvent = new AnvilClickEvent(clicker, AnvilSlot.bySlot(event.getRawSlot()), name);

                        handler.onAnvilClick(clickEvent);

                        if (clickEvent.willClose())
                        	close(APIPlayer.getPlayer(clicker), true);

                        if (clickEvent.willDestroy())
                            destroy();
                    }
                }
            }

            @EventHandler
            public void onInventoryClose(InventoryCloseEvent event) {
                if (event.getPlayer() instanceof Player) {
                    Inventory inv = event.getInventory();

                    if (inv.equals(inventory)) {
                    	inventory.clear();
                        close(APIPlayer.getPlayer((Player) event.getPlayer()), false);
                        destroy();
                    }
                }
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event) {
                if (event.getPlayer().getUniqueId().equals(uuid)) {
                	close(APIPlayer.getPlayer((Player) event.getPlayer()), false);
                	destroy();
                }
            }
        };
        
        Bukkit.getPluginManager().registerEvents(listener, OnimaAPI.getInstance());
	}
	
    public void destroy() {
    	HandlerList.unregisterAll(listener);
    	
        items = null;
        listener = null;
    }
    
    public void setSlot(AnvilSlot slot, ItemStack item) {
        items.put(slot, item);
    }
    
    public class AnvilClickEvent {
    	
    	private Player clicker;
        private AnvilSlot slot;
        private String input;
        private boolean close = false;
        private boolean destroy = false;

        public AnvilClickEvent(Player clicker, AnvilSlot slot, String input) {
        	this.clicker = clicker;
            this.slot = slot;
            this.input = input;
        }
        
        public Player getClicker() {
        	return clicker;
        }

        public AnvilSlot getSlot() {
            return slot;
        }

        public String getInput() {
            return input;
        }

        public boolean willClose() {
            return close;
        }

        public void setWillClose(boolean close) {
            this.close = close;
        }

        public boolean willDestroy() {
            return destroy;
        }

        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }
    }

    public interface AnvilClickEventHandler {
        void onAnvilClick(AnvilClickEvent event);
    }

    private class AnvilContainer extends ContainerAnvil {
    	
    	public AnvilContainer(EntityHuman entity) {
        	super(entity.inventory, entity.world, 0, 0, 0, entity);
        }

        @Override
        public boolean a(EntityHuman entityhuman) {
            return true;
        }
        
    }
    
    public enum AnvilSlot {
        INPUT_LEFT(0),
        INPUT_RIGHT(1),
        OUTPUT(2);

        private int slot;

        AnvilSlot(int slot) {
            this.slot = slot;
        }

        public int getSlot() {
            return slot;
        }

        public static AnvilSlot bySlot(int slot) {
        	for (AnvilSlot anvilSlot : values()) {
                if (anvilSlot.slot == slot)
                    return anvilSlot;
            }
        	
            return null;
        }
    }

}
