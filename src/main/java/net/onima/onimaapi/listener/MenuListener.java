package net.onima.onimaapi.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.crates.SupplyCrate;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.buttons.utils.DroppableButton;
import net.onima.onimaapi.gui.menu.utils.AnvilMenu;
import net.onima.onimaapi.players.APIPlayer;

public class MenuListener implements Listener {
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onMenuClick(InventoryClickEvent event) {
		ItemStack item = event.getCurrentItem();
		Player clicker = (Player) event.getWhoClicked();
		
		if (item == null || item.getType() == Material.AIR || !event.getClickedInventory().equals(event.getView().getTopInventory()))
			return;
		
		PacketMenu menu = APIPlayer.getPlayer(clicker).getViewingMenu();
		int slot = event.getSlot();
		
		if (menu == null || menu instanceof AnvilMenu /*|| menu instanceof PlayerInventoryMenu*/)
			return;
		
		if (slot == event.getRawSlot() && menu.getTitle().equalsIgnoreCase(event.getInventory().getName()) && menu.getButtons().containsKey(slot)) {
			Button button = menu.getButtons().get(slot);
		
			if (button instanceof DroppableButton && event.getAction().toString().contains("DROP"))
				((DroppableButton) button).drop(menu, clicker, item, event);
			else if (!event.getAction().toString().contains("DROP"))
				button.click(menu, clicker, item, event);
		}
	}
	
//	@EventHandler
//	public void onDrop(PlayerDropItemEvent event) {
//		ItemStack item = event.getItemDrop().getItemStack();
//		Player dropper = event.getPlayer();
//		PacketMenu menu = APIPlayer.getPlayer(dropper).getViewingMenu();
//		
//		if (menu == null || menu instanceof AnvilMenu /*|| menu instanceof PlayerInventoryMenu*/)
//			return;
//		
//		for (Button button : menu.getButtons().values()) {
//			if (button instanceof DroppableButton && button.getButtonItem(dropper).toItemStack().isSimilar(item))
//				((DroppableButton) button).drop(menu, dropper, item, event);
//		}
//	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		APIPlayer apiPlayer = APIPlayer.getPlayer(player);
		PacketMenu menu = apiPlayer.getViewingMenu();
		
		if (menu != null || !(menu instanceof AnvilMenu)) {
			apiPlayer.closeMenu();
			player.setMetadata("scanglitch", new FixedMetadataValue(OnimaAPI.getInstance(), true));
		}
		
		Crate crate = apiPlayer.getCrateOpening();
		
		if (crate != null && crate instanceof SupplyCrate) {
			crate.close(apiPlayer);
			((SupplyCrate) crate).destroy();
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		if (player.hasMetadata("scanglitch")) {
			player.removeMetadata("scanglitch", OnimaAPI.getInstance());

			for (ItemStack it : player.getInventory().getContents()) {
				if (it == null) 
					continue;
				
				ItemMeta meta = it.getItemMeta();
					
				if (meta != null && meta.hasDisplayName() && meta.getDisplayName().contains("§b§c§d§e"))
					player.getInventory().remove(it);
			}
		}
	}
	
}
