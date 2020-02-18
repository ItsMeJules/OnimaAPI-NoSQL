package net.onima.onimaapi.gui.menu.report;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.MenuOpenerButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.menu.utils.FiltrableInventory;
import net.onima.onimaapi.report.filters.ReportFilter;
import net.onima.onimaapi.utils.BetterItem;

public class ReportFiltersMenu extends PacketMenu {

	private FiltrableInventory initialMenu;

	public ReportFiltersMenu(FiltrableInventory initialMenu) {
		super("report_filters", "§6Filtrer les reports.", MIN_SIZE * 2, false);
		
		this.initialMenu = initialMenu;
	}

	@Override
	public void registerItems() {
		buttons.put(0, new FilterButton(ReportFilter.BUG_REPORT_FILTER, Material.ENDER_CHEST, 0));
		buttons.put(1, new FilterButton(ReportFilter.PLAYER_REPORT_FILTER, Material.SKULL_ITEM, 1));
		buttons.put(2, new FilterButton(ReportFilter.COMMENTED_REPORT_FILTER, Material.BOOK, 2));
		buttons.put(3, new FilterButton(ReportFilter.DONE_REPORT_FILTER, Material.EMERALD, 3));
		buttons.put(4, new FilterButton(ReportFilter.NOT_DONE_REPORT_FILTER, Material.COAL, 4));
		buttons.put(5, new FilterButton(ReportFilter.WAITING_STATUS_REPORT_FILTER, Material.DISPENSER, 5));
		buttons.put(6, new FilterButton(ReportFilter.IN_PROGRESS_STATUS_REPORT_FILTER, Material.GOLD_NUGGET, 6));
		buttons.put(7, new FilterButton(ReportFilter.IMPORTANT_STATUS_REPORT_FILTER, Material.TNT, 7));
		buttons.put(8, new FilterButton(ReportFilter.TRUE_VERDICT_REPORT_FILTER, Material.INK_SACK, 10, 8));
		buttons.put(9, new FilterButton(ReportFilter.UNCERTAIN_VERDICT_REPORT_FILTER, Material.INK_SACK, 14, 9));
		buttons.put(10, new FilterButton(ReportFilter.FALSE_VERDICT_REPORT_FILTER, Material.INK_SACK, 1, 10));
		
		buttons.put(size - 1, new MenuOpenerButton(new BetterItem(Material.BLAZE_ROD, 1, 0, "§6Retourner au menu", "", "§6Clic §7pour appliquer les filtres", "§7et retourner au menu.", "", "§7§oAttention, si vous fermez l'inventaire,", "§7les filtres ne seront plus applicables."), initialMenu.asPacketMenu()));
	}

	private class FilterButton implements Button {
		
		private ReportFilter filter;
		private Material material;
		private int damage;
		private int slot;
		
		public FilterButton(ReportFilter filter, Material material, int damage, int slot) {
			this.filter = filter;
			this.material = material;
			this.damage = damage;
			this.slot = slot;
		}

		public FilterButton(ReportFilter filter, Material material, int slot) {
			this(filter, material, 0, slot);
		}

		@Override
		public BetterItem getButtonItem(Player player) {
			BetterItem item = new BetterItem(material, 1, damage, filter.getDispalyName(), "", "§6Clic §7pour " + (initialMenu.hasFilter(filter) ? "§cretirer" : "§aajouter") + " §7ce filtre.");
		
			if (initialMenu.hasFilter(filter))
				item.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			
			return item;
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
		
			if (!initialMenu.hasFilter(filter))
				initialMenu.addFilter(filter);
			else
				initialMenu.removeFilter(filter);
			
			updateLocalized(clicker, slot);
		}
		
	}
	
}
