package net.onima.onimaapi.gui.buttons;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.menu.utils.AllPagesMenu;
import net.onima.onimaapi.gui.menu.utils.PageMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.OSound;

public class PageButton implements Button {

	private PageMenu menu;
	
	public PageButton(PageMenu menu) {
		this.menu = menu;
	}
	
	@Override
	public BetterItem getButtonItem(Player player) {
		System.out.println("Page carpet = " + menu.getCurrentPage());
		BetterItem item = new BetterItem(Material.CARPET, 1, 0, "§aPage §f" + menu.getCurrentPage() + "/" + menu.getPages() + "§a.");
		
		if (hasPage(1))
			item.addLore(" §6» §aClick gauche pour avancer.");
		else
			item.addLore(" §6» §aClick gauche pour aller à la §npremière§a page.");
	
		item.addLore(" §6» §fClick du milieu pour sélectionner une page.");
		
		if (hasPage(-1))
			item.addLore(" §6» §cClick droit pour reculer.");
		else
			item.addLore(" §6» §cClick droit pour aller à la §ndernière§c page.");
		
		return item;
	}
	
	@Override
	public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
		event.setCancelled(true);
		
		ClickType type = event.getClick();
		APIPlayer apiPlayer = APIPlayer.getPlayer(clicker);
		
		if (type == ClickType.LEFT) {
			new OSound(Sound.CLICK, 1F, 1F).play(apiPlayer);
			
			if (hasPage(1))
				this.menu.changePage(apiPlayer, 1);
			else
				this.menu.openPage(apiPlayer, 1);
			
		} else if (type == ClickType.MIDDLE) {
			new OSound(Sound.CLICK, 1F, 1F).play(apiPlayer);
			new AllPagesMenu(this.menu).open(apiPlayer);
		} else if (type == ClickType.RIGHT) {
			new OSound(Sound.CLICK, 1F, 1F).play(apiPlayer);
			
			if (hasPage(-1))
				this.menu.changePage(apiPlayer, -1);
			else
				this.menu.openPage(apiPlayer, this.menu.getPages());
			
		}
	}

	public boolean hasPage(int mod) {
		int page = menu.getCurrentPage() + mod;
		
		System.out.println(page + " > 0 && " + page + " <= " + menu.getPages());
		
		return page > 0 && page <= menu.getPages();
	}
	
}
