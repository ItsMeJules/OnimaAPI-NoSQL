package net.onima.onimaapi.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.OSound;

public class Wand {
	
	private static BetterItem wandItem;
	private static OSound oSound1, oSound2;
	
	static {
		wandItem = new BetterItem(Material.GOLD_HOE, 1, 0, ConfigurationService.WAND_NAME, ConfigurationService.WAND_LORE);
		oSound1 = new OSound(Sound.ORB_PICKUP, 1F, 1F);
		oSound2 = new OSound(Sound.ORB_PICKUP, 0.5F, 1F);
	}
	
	private ItemStack wand;
	private Location location1, location2;
	
	public Wand(int amount) {
		wand = wandItem.toItemStack();
	}
	
	public boolean give(Player player) {
		return player.getInventory().addItem(wand).isEmpty();
	}

	public Location getLocation1() {
		return location1;
	}

	public void setLocation1(Location location1) {
		this.location1 = location1;
	}

	public Location getLocation2() {
		return location2;
	}

	public void setLocation2(Location location2) {
		this.location2 = location2;
	}
	
	public boolean hasAllLocationsSet() {
		return location1 != null && location2 != null;
	}
	
	public static OSound getOSound1() {
		return oSound1;
	}

	public static OSound getOSound2() {
		return oSound2;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean isZoneWand(ItemStack item) {
		return (item != null && item.getType() == wandItem.getMaterial() && item.getData().getData() == wandItem.getDamage() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(ConfigurationService.WAND_NAME));
	}
	
	public static boolean validWorlds(Player player, Location loc1, Location loc2) {
		if (loc1 != null && loc2 != null && !loc1.getWorld().getName().equalsIgnoreCase(loc2.getWorld().getName())) {
			player.sendMessage("§cLes locations doivent être dans les mêmes monde !");
			return false;
		}
		return true;
	}
	
}
