package net.onima.onimaapi.event;

import java.util.Map;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.players.APIPlayer;

public class PlayerTransferItemEvent extends Event {
	
	private static HandlerList handlers = new HandlerList();
	
	private ItemStack item;
	private Map<Integer, Integer> fromSlots, toSlots;
	private Inventory fromInventory, toInventory;
	private APIPlayer apiPlayer;
	
	public PlayerTransferItemEvent(ItemStack item, APIPlayer apiPlayer, Inventory fromInventory, Inventory toInventory) {
		this.item = item;
		this.apiPlayer = apiPlayer;
		this.fromInventory = fromInventory;
		this.toInventory = toInventory;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public Map<Integer, Integer> getFromSlots() {
		return fromSlots;
	}

	public void setFromSlots(Map<Integer, Integer> fromSlots) {
		this.fromSlots = fromSlots;
	}

	public Map<Integer, Integer> getToSlots() {
		return toSlots;
	}

	public void setToSlots(Map<Integer, Integer> toSlots) {
		this.toSlots = toSlots;
	}

	public Inventory getFromInventory() {
		return fromInventory;
	}
	
	public void setFromInventory(Inventory fromInventory) {
		this.fromInventory = fromInventory;
	}

	public Inventory getToInventory() {
		return toInventory;
	}

	public void setToInventory(Inventory toInventory) {
		this.toInventory = toInventory;
	}
	
	public APIPlayer getAPIPlayer() {
		return apiPlayer;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
