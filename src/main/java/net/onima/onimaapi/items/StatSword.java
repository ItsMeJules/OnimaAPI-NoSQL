package net.onima.onimaapi.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;

public class StatSword {
	
	public static void addKill(ItemStack item, String killer, String killed) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
		
		if (lore.isEmpty())
			lore.add(ConfigurationService.STAT_SWORD_FIRST_LINE + " 1");
		else
			lore.set(0, ConfigurationService.STAT_SWORD_FIRST_LINE + ' ' + (Methods.toInteger(lore.get(0).split(":")[1].trim()) + 1));
			
		if (lore.size() > 5)
			lore.remove(1);
		
		lore.add(ConfigurationService.STAT_SWORD_KILL_LINE.replace("%killer%", killer).replace("%victim%", killed).replace("%date%", Methods.toFormatDate(System.currentTimeMillis(), ConfigurationService.DATE_FORMAT_HOURS)));
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

}
