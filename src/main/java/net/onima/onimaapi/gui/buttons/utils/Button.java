package net.onima.onimaapi.gui.buttons.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.utils.BetterItem;

public interface Button {

	public BetterItem getButtonItem(Player player);
	public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event);
	
}
