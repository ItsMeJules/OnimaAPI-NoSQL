package net.onima.onimaapi.gui.buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.utils.BetterItem;

public class BackButton implements Button {

	private PacketMenu backMenu;
	
	public BackButton(PacketMenu backMenu) {
		this.backMenu = backMenu;
	}
	
	@Override
	public BetterItem getButtonItem(Player player) {
		return new BetterItem(Material.ARROW, 1, 0, "§cRetour au menu " + backMenu.getTitle());
	}

	@Override
	public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
		event.setCancelled(true);
		backMenu.open(APIPlayer.getPlayer(clicker));
	}

}
