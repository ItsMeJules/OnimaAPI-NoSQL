package net.onima.onimaapi.gui.menu.utils;

import java.util.Map;
import java.util.Map.Entry;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.PageButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.players.APIPlayer;

public abstract class PageMenu extends PacketMenu {
	
	protected int currentPage = 1;
	
	public PageMenu(String id, String title, int size, boolean createInventory) {
		super(id, title, size, createInventory);
	}
	
	@Override
	public void registerItems() {
		int minIndex = (currentPage - 1) * getMaxItemsPerPage(); //(2 - 1) * 18 = 18
		int maxIndex = currentPage * getMaxItemsPerPage(); // 2 * 18 = 36
		
		for (Entry<Integer, Button> entry : getAllPagesItems().entrySet()) {
			int index = entry.getKey();
			
			if (index >= minIndex && index < maxIndex)
				buttons.put((index -= getMaxItemsPerPage() * (currentPage - 1)), entry.getValue());
		}
		
		Map<Integer, Button> global = getGlobalButtons();

		if (global != null) {
			for (Map.Entry<Integer, Button> entry : global.entrySet())
				buttons.put(entry.getKey(), entry.getValue());
		}
		
		buttons.put(size - 1, new PageButton(this));
	}
	
	public abstract Map<Integer, Button> getAllPagesItems();
	public abstract int getMaxItemsPerPage();
	public abstract PageMenu getPage(int page);
	public abstract boolean changePage(APIPlayer apiPlayer, int toAdd);
	public abstract boolean openPage(APIPlayer apiPlayer, int page);
	
	public int getPages() {
		int buttonsSize = getAllPagesItems().size() + 1;
		
		if (buttonsSize == 0)
			return 1;
		
		return (int) Math.ceil(buttonsSize / size);
	}
	
	public Map<Integer, Button> getGlobalButtons() {
		return null;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
}
