package net.onima.onimaapi.gui.buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import net.onima.onimaapi.gui.buttons.utils.NumberChangerButton;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.callbacks.APICallback;

public class IntegerChangerButton extends NumberChangerButton {

	public IntegerChangerButton(int number, APICallback<Number> callback) {
		super(number, callback);
	}

	@Override
	public BetterItem getButtonItem(Player player) {
		int value = number.intValue();
		
		return new BetterItem(Material.STAINED_GLASS_PANE, value, value >= 0 ? 4 : 14, (value >= 0 ? "§a§l+" : "§c§l") + value);
	}

}
