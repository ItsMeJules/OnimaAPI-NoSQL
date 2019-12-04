package net.onima.onimaapi.gui.menu;

import net.onima.onimaapi.gui.menu.utils.OptionsMenu;
import net.onima.onimaapi.mod.ModItem;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.utils.PlayerOption;

public class ModOptionsMenu extends OptionsMenu {
	
	public ModOptionsMenu(APIPlayer apiPlayer) {
		super("mod_options", ModItem.MOD_PREFIX + " §8Customisation", MIN_SIZE * 2, apiPlayer);
	}
	
	@Override
	public void registerItems() {
		for (PlayerOption.ModOptions option : PlayerOption.ModOptions.values())
			buttons.put(option.slot(), new OptionsButton(option));
	}
	
}
