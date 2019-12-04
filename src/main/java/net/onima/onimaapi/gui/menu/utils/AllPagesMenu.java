package net.onima.onimaapi.gui.menu.utils;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.BackButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.OSound;

public class AllPagesMenu extends PacketMenu {
	
	private PageMenu menu;

	public AllPagesMenu(PageMenu menu) {
		super("all_pages_menu", "Sauter à une page", Methods.menuSizeFromInteger(menu.getPages()), false);
		
		this.menu = menu;
	}

	@Override
	public void registerItems() {
		buttons.put(0, new BackButton(menu));
	
		for (int i = 0; i < menu.getPages(); i++)
			buttons.put(buttons.size(), new JumpToPageButton(i + 1, menu.getPage(i), menu.getCurrentPage() == i));
	}
	
	public class JumpToPageButton implements Button {

		private int page;
		private PageMenu menu;
		private boolean current;
		
		public JumpToPageButton(int page, PageMenu menu, boolean current) {
			this.page = page;
			this.menu = menu;
			this.current = current;
		}
		
		@Override
		public BetterItem getButtonItem(Player player) {
			BetterItem item = new BetterItem(current ? Material.ENCHANTED_BOOK : Material.BOOK, page, 0, "§ePage " + page);
			
			if (current) {
				item.addLore("");
				item.addLore("§aPage actuelle.");
			}
			
			return item;
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			new OSound(Sound.CLICK, 1F, 1F).play(clicker);
			this.menu.open(APIPlayer.getPlayer(clicker));
		}
		
	}

}
