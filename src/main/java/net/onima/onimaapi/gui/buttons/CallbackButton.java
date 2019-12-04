package net.onima.onimaapi.gui.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.callbacks.APICallback;

public class CallbackButton<T> extends DisplayButton {

	private T arg;
	private APICallback<T> callback;
	private InventoryClickEvent event;
	
	public CallbackButton(BetterItem item, T arg) {
		super(item);
		
		this.arg = arg;
	}
	
	public CallbackButton(ItemStack item, T arg) {
		super(item);
		
		this.arg = arg;
	}
	
	public void setCallBack(APICallback<T> callback) {
		this.callback = callback;
	}
	
	public InventoryClickEvent getEvent() {
		return event;
	}
	
	@Override
	public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
		this.event = event;
		callback.call(arg);
	}

}
