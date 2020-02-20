package net.onima.onimaapi.gui.menu.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.MenuOpenerButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.menu.utils.FiltrableInventory;
import net.onima.onimaapi.gui.menu.utils.PageMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.report.BugReport;
import net.onima.onimaapi.report.Report;
import net.onima.onimaapi.report.filters.ReportFilter;
import net.onima.onimaapi.report.struct.ReportStatus;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;

public class MyReportsMenu extends PageMenu implements FiltrableInventory {
	
	private List<ReportFilter> filters;
	private APIPlayer apiPlayer;
	
	{
		filters = new ArrayList<>();
	}

	public MyReportsMenu(APIPlayer apiPlayer) {
		super("my_reports", "§5§lMes reports", MAX_SIZE, false);
		
		this.apiPlayer = apiPlayer;
		
		filters.add(ReportFilter.getPlayerFilter(apiPlayer.getUUID(), true));
	}
	
	
	@Override
	public Map<Integer, Button> getAllPagesItems() {
		Map<Integer, Button> map = new HashMap<>();
		
		for (Report report : apiPlayer.getReports()) {
			if (isValid(report))
				map.put(map.size(), new ReportNoDeleteButton(report));
		}
		
		return map;
	}

	
	@Override
	public Map<Integer, Button> getGlobalButtons() {
		Map<Integer, Button> map = new HashMap<>();
		BetterItem item = new BetterItem(Material.BOOKSHELF, 1, 0, "§eFiltrer mes reports", "", "§6Clic §7pour filtrer vos reports.");
		
		if (filters.size() > 1)
			item.addLore("").addLore("§7Il y a actuellement §6" + (filters.size() - 1) + " §7filtre" + (filters.size() > 2 ? "s" : "") + '.');
		
		map.put(52, new MenuOpenerButton(item, new ReportFiltersMenu(this)));
		return map;
	}

	@Override
	public int getMaxItemsPerPage() {
		return 52;
	}
	
	@Override
	public boolean addFilter(ReportFilter filter) {
		return filters.add(filter);
	}
	
	@Override
	public boolean removeFilter(ReportFilter filter) {
		return filters.remove(filter);
	}
	
	@Override
	public boolean hasFilter(ReportFilter filter) {
		return filters.contains(filter);
	}
	
	@Override
	public void clearFilters() {
		filters.clear();
	}
	
	@Override
	public boolean isValid(Report report) {
		boolean valid = true;
		
		for (ReportFilter filter : filters) {
			if (!filter.filter(report)) {
				valid = false;
				break;
			}
		}
		
		return valid;
	}
	
	@Override
	public PacketMenu asPacketMenu() {
		return this;
	}
	
	public static class ReportNoDeleteButton implements Button {
		
		private Report report;

		public ReportNoDeleteButton(Report report) {
			this.report = report;
		}

		@Override
		public BetterItem getButtonItem(Player player) {
			ReportStatus status = report.getStatus();
			BetterItem item = new BetterItem(status.getMaterial(false), 1, status.getColor(false));
			
			item.setName((report instanceof BugReport ? "§aBug Report" : "§cReport") +  " §7#" + report.getId())
				.addLore("")
				.addLore("§7Status : " + status.getTitle(report.getDoneBy()))
				.addLore("§7Date : §e" + Methods.toFormatDate(report.getTime(), ConfigurationService.DATE_FORMAT_HOURS));
			
			if (report.getVerdict() != null)
				item.addLore("§7Verdict : " + report.getVerdict().getTitle());
		
			item.addLore("").addLore("§7Raison : §6" + report.getReason());
			
			if (!report.hasReceivedRewards() && !report.getRewards().isEmpty()) {
				item.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				item.addLore("").addLore("§eVous avez des récompenses !");
			}
			
			return item;
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
			new ReportPlayerInfoMenu(report).open(APIPlayer.getPlayer(clicker));
		}
		
	}

}
