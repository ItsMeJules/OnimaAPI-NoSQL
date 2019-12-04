package net.onima.onimaapi.limiter;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

public class PotionLimiter {
	
	private List<Short> disabled;
	
	{
		disabled = Lists.newArrayList((short) 8193, (short) 8201, (short) 8265, (short) 8233, (short) 16393, (short) 16457, (short) 16425, (short) 16387);
	}
	
	public boolean isDisabled(ItemStack item) {
		return item != null && item.getType() == Material.POTION ? disabled.contains(item.getDurability()) : false;
	}

}
