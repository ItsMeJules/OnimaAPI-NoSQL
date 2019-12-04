package net.onima.onimaapi.gui.menu.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.BackButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.buttons.utils.UpdatableButton;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.callbacks.APICallback;

public abstract class UpdaterMenu extends PacketMenu {
	
	protected static Button splitter;
	
	static 	{
		splitter = new Button() {
			
			@Override
			public BetterItem getButtonItem(Player player) {
				return new BetterItem(Material.STAINED_GLASS_PANE, 1, 15, "§r");
			}
			
			@Override
			public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
				event.setCancelled(true);
			}
		};
	}
	
	protected UpdatableButton toUpdate;
	protected BackButton back;
	protected APICallback<?> callback;
	protected int toUpdateSlot;
	
	public UpdaterMenu(String id, String title, int size, PacketMenu backMenu) {
		super(id, title, size, false);
		
		back = new BackButton(backMenu);
	}

	public UpdatableButton getToUpdate() {
		return toUpdate;
	}

	public void setToUpdate(UpdatableButton toUpdate) {
		this.toUpdate = toUpdate;
	}

	public APICallback<?> getCallback() {
		return callback;
	}

	public void setCallback(APICallback<?> callback) {
		this.callback = callback;
	}
	
}
