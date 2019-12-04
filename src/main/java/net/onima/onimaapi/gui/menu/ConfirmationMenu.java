package net.onima.onimaapi.gui.menu;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.ConfirmationButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.utils.callbacks.APICallback;

public class ConfirmationMenu extends PacketMenu {
	
	private APICallback<Boolean> callback;
	private boolean closeOnResponse;
	private Button[] middleButtons;
	
	public ConfirmationMenu(String title, APICallback<Boolean> callback, boolean closeOnResponse, Button... middleButtons) {
		super("confirm_menu", title, MIN_SIZE * 4, true);
		
		this.callback = callback;
		this.closeOnResponse = closeOnResponse;
		this.middleButtons = middleButtons;
	}

	@Override
	public void registerItems() {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 4; y++) {
				buttons.put(getSlot(x, y), new ConfirmationButton(true, closeOnResponse, callback));
				buttons.put(getSlot(8 - x, y), new ConfirmationButton(false, closeOnResponse, callback));
			}
		}
		
		if (middleButtons != null) {
			for (int i = 0; i < middleButtons.length; i++) {
				if (middleButtons[i] != null)
					buttons.put(getSlot(4, i), middleButtons[i]);
			}
		}
	}

	public APICallback<Boolean> getCallback() {
		return callback;
	}

	public boolean shouldCloseOnResponse() {
		return closeOnResponse;
	}

	public Button[] getMiddleButtons() {
		return middleButtons;
	}
	
}
