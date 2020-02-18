package net.onima.onimaapi.gui.menu.utils;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.report.Report;
import net.onima.onimaapi.report.filters.ReportFilter;

public interface FiltrableInventory {
	
	boolean addFilter(ReportFilter filter);
	boolean removeFilter(ReportFilter filter);
	boolean hasFilter(ReportFilter filter);
	
	void clearFilters();
	
	boolean isValid(Report report);
	
	PacketMenu asPacketMenu();

}
