package net.onima.onimaapi.gui.buttons.utils;

import net.onima.onimaapi.gui.PacketMenu;

public interface UpdatableButton extends Button {
	
	public default void update(PacketMenu menu) {
		menu.registerItems();
	}

}
