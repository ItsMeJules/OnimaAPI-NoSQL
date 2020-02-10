package net.onima.onimaapi.gui.menu.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.PageButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.players.APIPlayer;

public abstract class PageMenu extends PacketMenu { //TODO Mettre le bouton changer de page quand la limite est atteinte, sinon rien mettre.
	
	protected int currentPage = 1;
	protected Map<Integer, Button> allItems;
	
	{
		allItems = new HashMap<>();
	}
	
	public PageMenu(String id, String title, int size, boolean createInventory) {
		super(id, title, size, createInventory);
	}
	
	@Override
	public void registerItems() {
		int minIndex = (currentPage - 1) * getMaxItemsPerPage(); //(2 - 1) * 18 = 18
		int maxIndex = currentPage * getMaxItemsPerPage(); // 2 * 18 = 36
		
		if (allItems.isEmpty())
			allItems = getAllPagesItems();
		
		for (Entry<Integer, Button> entry : allItems.entrySet()) {
			int index = entry.getKey();
				
			if (index >= minIndex && index < maxIndex)
				buttons.put(index -= minIndex, entry.getValue());
		}
		
		Map<Integer, Button> global = getGlobalButtons();

		if (global != null) {
			for (Map.Entry<Integer, Button> entry : global.entrySet())
				buttons.put(entry.getKey(), entry.getValue());
		}
		
		buttons.put(size - 1, new PageButton(this));
	}
	
	@Override
	public void updateItems(Player player) {
		buttons.clear();
		inventory.clear();
		allItems.clear();
		
		registerItems();
		
		for (Entry<Integer, Button> entry : buttons.entrySet())
			inventory.setItem(entry.getKey(), createItemStack(player, entry.getValue()));
	}
	
	public abstract Map<Integer, Button> getAllPagesItems();
	public abstract int getMaxItemsPerPage();
	
	public PageMenu getPage(int page) {
		this.currentPage = page;
		
		return this;
	}

	public boolean changePage(APIPlayer apiPlayer, int toAdd) {
		this.currentPage += toAdd;
		this.updateItems(apiPlayer.toPlayer());
		
		return true;
	}

	public boolean openPage(APIPlayer apiPlayer, int page) {
		this.currentPage = page;
		this.updateItems(apiPlayer.toPlayer());
		
		return true;
	}
	
	public int getPages() {
		if (allItems.isEmpty())
			allItems = getAllPagesItems();
		
		int buttonsSize = allItems.size() + 1;
		
		if (buttonsSize == 0)
			return 1;
		
		return (int) Math.ceil(buttonsSize / (double) size);
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
