package net.onima.onimaapi.gui.menu.utils;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.IntegerChangerButton;
import net.onima.onimaapi.utils.callbacks.APICallback;

public class IntegerUpdaterMenu extends UpdaterMenu {
	
	{
		toUpdateSlot = getSlot(4, 2);
	}

	public IntegerUpdaterMenu(String title, PacketMenu backMenu) {
		super("integer_updater", title, MIN_SIZE * 3, backMenu);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void registerItems() {
		APICallback<Number> numCallback = (APICallback<Number>) callback;
		
		for (int x = 0; x < 4; x++) {
			buttons.put(3 - x, new IntegerChangerButton(x + 1, numCallback));
			buttons.put(5 + x, new IntegerChangerButton(- (x + 1), numCallback));
		}
		
		buttons.put(4, splitter);
		
		for (int x = 0; x < 9; x++)
			buttons.put(getSlot(x, 1), splitter);
		
		buttons.put(toUpdateSlot, toUpdate);
		buttons.put(size - 1, back);
	}

}
