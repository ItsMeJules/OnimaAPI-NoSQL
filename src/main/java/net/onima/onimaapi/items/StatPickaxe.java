package net.onima.onimaapi.items;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.onima.onimaapi.players.utils.MinedOres.Ore;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;

public class StatPickaxe {
	
	public static int getCount(ItemStack item, Ore ore) {
		ItemMeta meta = item.getItemMeta();
		
		if (meta.hasLore()) {
			int value = Integer.valueOf(ChatColor.stripColor(meta.getLore().get(ore.getIndex()).split(":")[1].trim()));
			return value;
		}
		return 0;
	}
	
	public static void updatePickaxe(ItemStack item, Block block, int diamond, int emerald, int gold, int redstone, int lapis, int iron, int coal, int quartz) {
		switch (block.getType()) {
		case DIAMOND_ORE:
			diamond++;
			break;
		case EMERALD_ORE:
			emerald++;
			break;
		case GOLD_ORE:
			gold++;
			break;
		case REDSTONE_ORE:
			redstone++;
			break;
		case LAPIS_ORE:
			lapis++;
			break;
		case IRON_ORE:
			iron++;
			break;
		case COAL_ORE:
			coal++;
			break;
		case QUARTZ_ORE:
			quartz++;
			break;
		default:
			break;
		}
			
		ItemMeta meta = item.getItemMeta();
		
		if (!meta.hasDisplayName()) meta.setDisplayName(ConfigurationService.STAT_PICKAXE_NAME);
		meta.setLore(Methods.replacePlaceholder(ConfigurationService.STAT_PICKAXE_LORE, "%diamond%", diamond, "%emerald%", emerald, "%gold%", gold, "%redstone%", redstone, "%lapis%", lapis, "%iron%", iron, "%coal%", coal, "%quartz%", quartz));
		item.setItemMeta(meta);
	}
	
}
