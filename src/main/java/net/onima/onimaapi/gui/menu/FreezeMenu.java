package net.onima.onimaapi.gui.menu;

import java.util.Arrays;
import java.util.Map.Entry;

import org.bukkit.Material;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.PacketMenuType;
import net.onima.onimaapi.gui.PacketStaticMenu;
import net.onima.onimaapi.gui.buttons.DisplayButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.utils.BetterItem;

public class FreezeMenu extends PacketMenu implements PacketStaticMenu {

	public FreezeMenu() {
		super("freeze_menu", "§c§lVous êtes freeze !", PacketMenuType.HOPPER, true);
	}

	@Override
	public void registerItems() {
		buttons.put(0, new DisplayButton(new BetterItem(Material.STAINED_GLASS_PANE, 1, 14, "§c")));
		buttons.put(1, new DisplayButton(new BetterItem(Material.STAINED_GLASS_PANE, 1, 14, "§c")));
		buttons.put(2, new DisplayButton(new BetterItem(Material.BOOK, 1, 0, "§cVous êtes freeze !", Arrays.asList(
				"§4§m---------------------------", 
				"§cVous êtes soupçonné de cheat \u26A0", 
				"§cRejoignez notre discord : discord.io/onima", 
				"§cOu notre TeamSpeak : ts.onima.fr", 
				"§cSi vous déconnectez vous serez banni !", 
				"§4§m---------------------------"))));
		buttons.put(3, new DisplayButton(new BetterItem(Material.STAINED_GLASS_PANE, 1, 14, "§c")));
		buttons.put(4, new DisplayButton(new BetterItem(Material.STAINED_GLASS_PANE, 1, 14, "§c")));
	}

	@Override
	public void setup() {
		inventory.clear();
		for (Entry<Integer, Button> entry : buttons.entrySet())
			inventory.setItem(entry.getKey(), createItemStack(null, entry.getValue()));
	}

}
