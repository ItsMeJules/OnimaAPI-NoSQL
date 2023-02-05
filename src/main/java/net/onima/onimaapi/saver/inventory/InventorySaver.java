package net.onima.onimaapi.saver.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.utils.Methods;

public class InventorySaver {
	
	protected List<InventoryItem> items;
	protected int inventorySize;
	protected long savedTime;
	
	public InventorySaver(Inventory inventory) {
		inventorySize = inventory.getSize();
		savedTime = System.currentTimeMillis();
		items = new ArrayList<>(inventorySize);
		
		for (int i = 0; i < inventorySize; i++) {
			ItemStack item = inventory.getItem(i);
			
			if (item == null)
				continue;
			
			items.add(new InventoryItem(i, item));
		}
	}
	
	public void restore(Inventory inventory) {
		for (InventoryItem item : items)
			inventory.setItem(item.slot, item.item);
	}

	public List<InventoryItem> getItems() {
		return items;
	}
	
	public String getItemsAsString() {
		if (items.isEmpty())
			return "";
		
		StringBuilder builder = new StringBuilder();
		 
		builder.append(inventorySize).append(';');
		 
		for (InventoryItem item : items)
			builder.append(item.slot).append("#").append(Methods.serializeItem(item.item, true)).append(";");
			 
		return builder.toString();
	}
	
	public long getSavedTime() {
		return savedTime;
	}
	
	public static Inventory getItemsFromString(String items) {
		if (items == null || items.isEmpty())
			return null;
		
		String[] values = items.split(";");
		Inventory inventory = Bukkit.createInventory(null, Integer.valueOf(values[0]));
		
		for (int i = 1; i < values.length; i++) {
			String[] info = values[i].split("#");
			
			inventory.setItem(Integer.valueOf(info[0]), Methods.deserializeItem(info[1], true));
		}
		
		return inventory;
	}
	
	public class InventoryItem {
		
		private int slot;
		private ItemStack item;
		
		public InventoryItem(int slot, ItemStack item) {
			this.slot = slot;
			this.item = item;
		}

		public int getSlot() {
			return slot;
		}

		public ItemStack getItem() {
			return item;
		}
		
	}

}
