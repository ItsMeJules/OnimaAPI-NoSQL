package net.onima.onimaapi.gui.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.utils.BetterItem;

public class DisplayButton implements Button {
	
	private BetterItem item;
	
	public DisplayButton(BetterItem item) {
		this.item = item;
	}
	
	public DisplayButton(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		
		if (meta != null)
			this.item = new BetterItem(item, meta.hasDisplayName() ? meta.getDisplayName() : null, meta.hasLore() ? meta.getLore() : Lists.newArrayList());
		else
			this.item = new BetterItem(item, null, Lists.newArrayList());
	}

	@Override
	public BetterItem getButtonItem(Player player) {
		return item;
	}

	@Override
	public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
		event.setCancelled(true);
	}

}
