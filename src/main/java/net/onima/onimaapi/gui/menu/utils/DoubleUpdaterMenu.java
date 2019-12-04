package net.onima.onimaapi.gui.menu.utils;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.DoubleChangerButton;
import net.onima.onimaapi.utils.callbacks.APICallback;

public class DoubleUpdaterMenu extends UpdaterMenu {
	
	{
		toUpdateSlot = getSlot(4, 3);
	}
	
	public DoubleUpdaterMenu(String title, PacketMenu backMenu) {
		super("double_updater", title, MIN_SIZE * 4, backMenu);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void registerItems() {
		APICallback<Number> numCallback = (APICallback<Number>) callback;
		
		for (int y = 0; y < 2; y++) {
			for (int x = 0; x < 4; x++) {
				buttons.put(getSlot(3 - x, y), y == 0 ? new DoubleChangerButton(x < 3 ? (x + 1D) / 10 : (x + 2D) / 10, numCallback) : new DoubleChangerButton(x + 1, numCallback));
				buttons.put(getSlot(5 + x, y), y == 0 ? new DoubleChangerButton(x < 3 ? -(x + 1D) / 10 : -(x + 2D) / 10, numCallback) : new DoubleChangerButton(-(x + 1), numCallback));
			}
		}
		
		buttons.put(4, splitter);
		buttons.put(getSlot(4, 1), splitter);
		
		for (int x = 0; x < 9; x++)
			buttons.put(getSlot(x, 2), splitter);
		
		buttons.put(toUpdateSlot, toUpdate);
		buttons.put(size - 1, back);
	}

}
