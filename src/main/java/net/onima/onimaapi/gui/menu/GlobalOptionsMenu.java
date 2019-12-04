package net.onima.onimaapi.gui.menu;

import net.onima.onimaapi.gui.menu.utils.OptionsMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.utils.PlayerOption;

public class GlobalOptionsMenu extends OptionsMenu {

	public GlobalOptionsMenu(APIPlayer apiPlayer) {
		super("global_options", "§8Options globale", MIN_SIZE * 3, apiPlayer);
	}

	@Override
	public void registerItems() {
		for (PlayerOption.GlobalOptions option : PlayerOption.GlobalOptions.values())
			buttons.put(option.slot(), new OptionsButton(option));
	}

}
