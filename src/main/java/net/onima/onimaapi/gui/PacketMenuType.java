package net.onima.onimaapi.gui;

import org.bukkit.event.inventory.InventoryType;

public enum PacketMenuType {
	
	CHEST("chest", 0, InventoryType.CHEST),
	CRAFTING_TABLE("crafting_table", 1, InventoryType.CRAFTING),
	FURNACE("furnace", 2, InventoryType.FURNACE),
	DISPENSER("dispenser", 3, InventoryType.DISPENSER),
	ENCHANTING_TABLE("enchanting_table", 4, InventoryType.ENCHANTING),
	BREWING_STAND("brewing_stand", 5, InventoryType.BREWING),
	VILLAGER("villager", 6, InventoryType.MERCHANT),
	BEACON("beacon", 7, InventoryType.BEACON),
	ANVIL("anvil", 8, InventoryType.ANVIL),
	HOPPER("hopper", 9, InventoryType.HOPPER),
	DROPPER("dropper", 10, InventoryType.DROPPER),
	ENTITY_HORSE("EntityHorse", 11, null);

	private String name;
	private int type;
	private InventoryType bukkitEquivalent;
	
	{
		name = "minecraft:";
	}
	
	private PacketMenuType(String name, int type, InventoryType bukkitEquivalent) {
		this.name += name;
		this.type = type;
		this.bukkitEquivalent = bukkitEquivalent;
	}
	
	public String getName() {
		return name;
	}
	
	public int getType() {
		return type;
	}
	
	public InventoryType getEquivalent() {
		return bukkitEquivalent;
	}
	
}
