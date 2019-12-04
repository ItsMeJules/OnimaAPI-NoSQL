package net.onima.onimaapi.gui.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.utils.BetterItem;

public class MenuOpenerButton extends DisplayButton {

	private PacketMenu menu;
	
	public MenuOpenerButton(BetterItem item, PacketMenu menu) {
		super(item);
		
		this.menu = menu;
	}
	
	@Override
	public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
		event.setCancelled(true);
		this.menu.open(APIPlayer.getPlayer(clicker));
	}

}
