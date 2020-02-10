package net.onima.onimaapi.gui.buttons.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.utils.BetterItem;

public interface Button {

	BetterItem getButtonItem(Player player);
	void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event);
	
}
