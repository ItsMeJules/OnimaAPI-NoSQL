package net.onima.onimaapi.gui.buttons.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;

public interface DroppableButton extends Button {
	
	void drop(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event);

}
