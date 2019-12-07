package net.onima.onimaapi.signs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

import com.google.common.base.Preconditions;

import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;

public class DeathSign {
	
	private BetterItem item;
	
	public DeathSign(String killer, String victim) {
		item = new BetterItem(Material.SIGN, 1, 0, ConfigurationService.DEATH_SIGN_NAME);
		item.setLore(Methods.replacePlaceholder(ConfigurationService.DEATH_SIGN_LINES, "%killer%", killer, "%victim%", victim, "%date%", Methods.toFormatDate(System.currentTimeMillis(), ConfigurationService.DATE_FORMAT_NO_HOURS)));
		
	}
	
	public DeathSign(String[] lore) {
		Preconditions.checkArgument(lore.length <= 4, "Le lore du panneau doit être en dessous de 4 lignes.");
		item = new BetterItem(Material.SIGN, 1, 0, ConfigurationService.DEATH_SIGN_NAME);
		item.setLore(lore);
	}
	
	public BetterItem getItem() {
		return item;
	}
	
	public void drop(Location location) {
		location.getWorld().dropItemNaturally(location, item.toItemStack());
	}
	
	public static boolean isDeathSign(Block block) {
		BlockState state = block.getState();
		
		if (state instanceof Sign) {
			String[] lines = ((Sign) state).getLines();
			return lines.length > 0 && lines[1] != null && lines[1].equalsIgnoreCase(ConfigurationService.DEATH_SIGN_LINES.get(1)); 
		}
		
		return false;
	}
	
}
