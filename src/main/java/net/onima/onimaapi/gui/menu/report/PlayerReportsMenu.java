package net.onima.onimaapi.gui.menu.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.BackButton;
import net.onima.onimaapi.gui.buttons.MenuOpenerButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.menu.utils.PageMenu;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.report.Report;
import net.onima.onimaapi.report.filters.ReportFilter;
import net.onima.onimaapi.utils.BetterItem;

public abstract class PlayerReportsMenu extends PageMenu {

	protected PacketMenu backMenu;
	protected OfflineAPIPlayer offline;
	
	private List<ReportFilter> filters;
	
	{
		filters = new ArrayList<>();
	}
	
	public PlayerReportsMenu(String id, String title, PacketMenu backMenu, OfflineAPIPlayer offline) {
		super(id, title, MAX_SIZE, false);

		this.backMenu = backMenu;
		this.offline = offline;
	}

	@Override
	public Map<Integer, Button> getGlobalButtons() {
		Map<Integer, Button> map = new HashMap<>();
		BetterItem item = new BetterItem(Material.BOOKSHELF, 1, 0, "§eFiltrer ces reports", "", "§6Clic §7pour filtrer ces reports.");
		
		if (filters.size() > 1)
			item.addLore("").addLore("§7Il y a actuellement §6" + (filters.size() - 1) + " §7filtre" + (filters.size() > 2 ? "s" : "") + '.');
		
		if (backMenu != null) {
			map.put(52, new BackButton(backMenu));
			map.put(51, new MenuOpenerButton(item, new ReportFiltersMenu(this)));
		} else
			map.put(52, new MenuOpenerButton(item, new ReportFiltersMenu(this)));
		
		return map;
	}
	
	@Override
	public int getMaxItemsPerPage() {
		return backMenu == null ? 52 : 51;
	}
	
	public boolean addFilter(ReportFilter filter) {
		return filters.add(filter);
	}
	
	public boolean removeFilter(ReportFilter filter) {
		return filters.remove(filter);
	}
	
	public boolean hasFilter(ReportFilter filter) {
		return filters.contains(filter);
	}
	
	public void clearFilters() {
		filters.clear();
	}
	
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

}
