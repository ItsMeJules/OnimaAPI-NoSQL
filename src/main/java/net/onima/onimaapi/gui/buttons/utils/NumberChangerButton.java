package net.onima.onimaapi.gui.buttons.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.utils.callbacks.APICallback;

public abstract class NumberChangerButton implements Button {

	protected Number number;
	protected APICallback<Number> callback;
	
	public NumberChangerButton(Number number, APICallback<Number> callback) {
		this.number = number;
		this.callback = callback;
	}
	
	@Override
	public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
		event.setCancelled(true);
		callback.call(number);
	}
	
}
