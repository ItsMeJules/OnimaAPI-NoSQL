package net.onima.onimaapi.gui.menu;

import java.util.UUID;

import org.bukkit.Material;

import net.onima.onimaapi.gui.menu.utils.AnvilMenu;
import net.onima.onimaapi.utils.BetterItem;

public abstract class AnvilInputMenu extends AnvilMenu {
	
	public AnvilInputMenu(UUID uuid, String id, String title, String itemName) {
		super(uuid, id, title);
		
		items.put(AnvilSlot.INPUT_LEFT, new BetterItem(Material.NAME_TAG, 1, 0, itemName).toItemStack());
	}

	@Override
	public AnvilClickEventHandler handler() {
		return event -> onEvent(event);
	}
	
	@Override
	public void registerItems() {}
	
	public abstract void onEvent(AnvilClickEvent event);
	
}
