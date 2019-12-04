package net.onima.onimaapi.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftContainer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Preconditions;

import net.minecraft.server.v1_7_R4.Container;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutOpenWindow;
import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.event.menu.PacketMenuCloseEvent;
import net.onima.onimaapi.event.menu.PacketMenuOpenEvent;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.BetterItem;

public abstract class PacketMenu {
	
	public static final int MAX_SIZE, MIN_SIZE;
	public static int TOTAL_MENU;
	
	protected static OnimaAPI plugin;
	protected static Set<PacketMenu> staticMenus;
	
	static {
		MAX_SIZE = 54;
		MIN_SIZE = 9;
		TOTAL_MENU = 0;
		
		plugin = OnimaAPI.getInstance();
		staticMenus = new HashSet<>(100);
	}
	
	protected PacketMenuType type;
	protected int size;
	protected String id, title;
	protected List<UUID> viewers;
	protected OnimaPerm permission;
	protected Map<Integer, Button> buttons;
	protected Inventory inventory;
	protected Container container;
	
	{
		viewers = new ArrayList<>(10);
		buttons = new HashMap<>();
	}
	
	public PacketMenu(String id, String title, int size, boolean createInventory) {
		if (title.length() > 32)
			title = title.substring(0, 32);
		
		Preconditions.checkArgument(size <= MAX_SIZE, "The size cannot be more than 54. ("+size+")");
		Preconditions.checkArgument(size >= MIN_SIZE, "The size cannot be less than 9. ("+size+")");
		
		if (createInventory)
			inventory = Bukkit.createInventory(null, size, title);
		
		if (this instanceof PacketStaticMenu) {
			registerItems();
			((PacketStaticMenu) this).setup();
		}
		
		this.id = id;
		this.title = title;
		this.size = size;
	}
	
	public PacketMenu(String id, String title, PacketMenuType type, boolean createInventory) {
		if (title.length() > 32)
			title = title.substring(0, 32);
		
		if (createInventory)
			inventory = Bukkit.createInventory(null, type.getEquivalent(), title);
		
		if (this instanceof PacketStaticMenu) {
			registerItems();
			((PacketStaticMenu) this).setup();
		}
		
		this.id = id;
		this.title = title;
		this.type = type;
	}
	
	public abstract void registerItems();
	
	protected ItemStack createItemStack(Player player, Button button) {
		BetterItem item = button.getButtonItem(player);

		if (item.getMaterial() != Material.SKULL_ITEM && item.hasName())
				item.setName(item.getName() + "§b§c§d§e");
				
		return item.toItemStack();
	}
	
	public void open(APIPlayer apiPlayer) {
		PacketMenuOpenEvent event = new PacketMenuOpenEvent(this, apiPlayer);
		Bukkit.getPluginManager().callEvent(event);
		
		if (event.isCancelled())
			return;
		
		if (apiPlayer.getViewingMenu() != null)
			apiPlayer.getViewingMenu().close(apiPlayer, false);
		
		Player player = apiPlayer.toPlayer();
		
		if (inventory == null)
			inventory = type == null ? Bukkit.createInventory(null, size, title) : Bukkit.createInventory(null, type.getEquivalent(), title);
		
		if (!(this instanceof PacketStaticMenu))
			updateItems(player);
		
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		int counter = entityPlayer.nextContainerCounter();
		container = CraftEventFactory.callInventoryOpenEvent(entityPlayer, new CraftContainer(inventory, entityPlayer.getBukkitEntity(), counter));
		
		if (container == null)
			return;
		
		sendPackets(entityPlayer, counter);
		
		apiPlayer.setViewingMenu(this);
		viewers.add(apiPlayer.getUUID());
	}

	public void close(APIPlayer apiPlayer, boolean force) {
		PacketMenuCloseEvent event = new PacketMenuCloseEvent(this, apiPlayer);
		Bukkit.getPluginManager().callEvent(event);
		
		if (event.isCancelled()) {
			Bukkit.getScheduler().runTask(plugin, () -> updateTitle(title, apiPlayer));
			return;
		}
		
		if (force)
			((CraftPlayer) apiPlayer.toPlayer()).getHandle().closeInventory();
		
		apiPlayer.setViewingMenu(null);
		viewers.remove(apiPlayer.getUUID());
	}
	
	public void updateItems(Player player) {
		buttons.clear();
		inventory.clear();
		
		registerItems();
		
		for (Entry<Integer, Button> entry : buttons.entrySet()) {
			inventory.setItem(entry.getKey(), createItemStack(player, entry.getValue()));
		}
	}
	
	public void updateLocalized(Player player, int slot) {
		inventory.setItem(slot, createItemStack(player, buttons.get(slot)));
	}
	
	public void updateTitle(String title, APIPlayer apiPlayer, boolean updateItems) {
		this.title = title;
		
		EntityPlayer entityPlayer = ((CraftPlayer) apiPlayer.toPlayer()).getHandle();
		int counter = entityPlayer.nextContainerCounter();
		inventory = type == null ? Bukkit.createInventory(null, size, title) : Bukkit.createInventory(null, type.getEquivalent(), title);
		container = new CraftContainer(inventory, entityPlayer.getBukkitEntity(), counter);
		
		if (updateItems)
			updateItems(apiPlayer.toPlayer());
		
		sendPackets(entityPlayer, counter);
	}
	
	public void updateTitle(String title, APIPlayer apiPlayer) {
		updateTitle(title, apiPlayer, true);
	}
	
	protected void sendPackets(EntityPlayer entityPlayer, int containerCounter) {
		entityPlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerCounter, type == null ? PacketMenuType.CHEST.getType() : type.getType(), title, size, true));
		entityPlayer.activeContainer = container;
		entityPlayer.activeContainer.windowId = containerCounter;
		entityPlayer.activeContainer.addSlotListener(entityPlayer);
	}
	
	public int getSlot(int x, int y) {
		return 9 * y + x;
	}
	
	public PacketMenuType getType() {
		return type;
	}

	public int getSize() {
		return size;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public List<UUID> getViewers() {
		return viewers;
	}

	public OnimaPerm getPermission() {
		return permission;
	}
	
	public Map<Integer, Button> getButtons() {
		return buttons;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	/**
	 * This method returns a set of menu that have been registered.
	 * 
	 * @return A set of menu.
	 */
	public static Set<PacketMenu> getStaticMenus() {
		return staticMenus;
	}
	
	/**
	 * This method returns the menu related to the given ID.
	 * 
	 * @param id - PacketMenu ID
	 * @return The menu related to the given ID<br>
	 * <tt>null</tt> if there is no menu with the given ID
	 */
	public static PacketMenu getMenu(String id) {
		for (PacketMenu menu : staticMenus) {
			if (menu.getId().equalsIgnoreCase(id))
				return menu;
		}
		return null;
	}

}
