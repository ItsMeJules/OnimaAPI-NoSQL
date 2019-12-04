package net.onima.onimaapi.limiter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantLimiter {
	
	private Map<Enchantment, Integer> limiters;
	
	{
		limiters = new HashMap<>();
	}
	
	public void init() {
		limiters.put(Enchantment.ARROW_KNOCKBACK, 0);
		limiters.put(Enchantment.ARROW_FIRE, 0);
		limiters.put(Enchantment.DAMAGE_ALL, 3);
		limiters.put(Enchantment.FIRE_ASPECT, 0);
		limiters.put(Enchantment.KNOCKBACK, 0);
		limiters.put(Enchantment.THORNS, 0);
		limiters.put(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
	}
	
	public Map<Enchantment, Integer> getLimitedEnchantments() {
		return limiters;
	}

	public boolean limit(ItemStack item) {
		if (item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			boolean applied = false;
			
			for (Entry<Enchantment, Integer> entry : limiters.entrySet()) {
				Enchantment enchant = entry.getKey();
				int limit = entry.getValue();
				
				if (meta.hasEnchant(enchant)) {
					int level = meta.getEnchantLevel(enchant);
					if (level > limit) {
						applied = true;
						meta.removeEnchant(enchant);
						
						if (limit > 0)
							meta.addEnchant(enchant, limit, false);
					}
				}
			}
			item.setItemMeta(meta);
			return applied;
		}
		return false;
	}
}
