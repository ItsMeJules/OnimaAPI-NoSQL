package net.onima.onimaapi.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;

public class Crowbar {
	
	public static final int SPAWNER_COUNT, PORTAL_COUNT;
	
	private static BetterItem item;
	
	static {
		SPAWNER_COUNT = 1;
		PORTAL_COUNT = 6;
		item = new BetterItem(Material.DIAMOND_HOE, 1, 0, ConfigurationService.CROWBAR_NAME, Methods.replacePlaceholder(ConfigurationService.CROWBAR_LORE, "%spawners%", SPAWNER_COUNT, "%portals%", PORTAL_COUNT));
	}
	
	private BetterItem itemCloned;
	
	public Crowbar() {
		itemCloned = item.clone();
	}
	
	public void setGiver(String name) {
		itemCloned.addLore(ConfigurationService.CROWBAR_GIVE_LINE + ' ' + name);
	}

	public ItemStack getItem() {
		return itemCloned.toItemStack();
	}
	
	public static int getCount(ItemStack item, CrowbarBlock block) {
		ItemMeta meta = item.getItemMeta();
		String line = meta.getLore().get(block.index);
		Integer count = Methods.toInteger(ChatColor.stripColor(line.split(":")[1].trim()));
		
		if (count != null)
			return count;
		
		return -1;
	}
	
	public static void updateCount(ItemStack item, int spawners, int portals) {
		ItemMeta meta = item.getItemMeta();
		
		meta.setLore(Methods.replacePlaceholder(ConfigurationService.CROWBAR_LORE, "%spawners%", spawners, "%portals%", portals));
		item.setItemMeta(meta);
	}
	
	public static int getID() {
		return 443;
	}
	
	public static boolean isCrowbar(ItemStack hand) {
		return hand != null && hand.hasItemMeta() && hand.getItemMeta().hasDisplayName() && hand.getItemMeta().getDisplayName().equalsIgnoreCase(ConfigurationService.CROWBAR_NAME);
	}
	
	public static enum CrowbarBlock {
		ENDER_PORTALS(2),
		SPAWNERS(1);
		
		private int index;
		
		private CrowbarBlock(int index) {
			this.index = index;
		}
		
		public int getIndex() {
			return index;
		}
	
	}

}
