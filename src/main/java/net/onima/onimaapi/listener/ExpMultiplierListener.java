package net.onima.onimaapi.listener;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import net.onima.onimaapi.utils.ConfigurationService;

/**
 * Listener that applies an Exp Multiplier for Fortune or Looting levels, etc.
 */
public class ExpMultiplierListener implements Listener {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		double amount = event.getDroppedExp();
		Player killer = event.getEntity().getKiller();
		
		if (killer != null && amount > 0) {
			ItemStack stack = killer.getItemInHand();
			
			if (stack != null && stack.getType() != Material.AIR) {
				int enchantmentLevel = stack.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
				
				if (enchantmentLevel > 0L)
					event.setDroppedExp((int) Math.ceil(amount * (enchantmentLevel * ConfigurationService.EXP_MULTIPLIER_LOOTING_PER_LEVEL)));
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		double amount = event.getExpToDrop();
		Player player = event.getPlayer();
		ItemStack stack = player.getItemInHand();
		if (stack != null && stack.getType() != Material.AIR && amount > 0) {
			int enchantmentLevel = stack.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
			
			if (enchantmentLevel > 0)
				event.setExpToDrop((int) Math.ceil(amount * (enchantmentLevel * ConfigurationService.EXP_MULTIPLIER_FORTUNE_PER_LEVEL)));
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPlayerPickupExp(PlayerExpChangeEvent event) {
		double amount = event.getAmount();

		if (amount > 0)
			event.setAmount((int) Math.ceil(amount * ConfigurationService.EXP_MULTIPLIER_GENERAL));
	}

	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerFish(PlayerFishEvent event) {
		double amount = event.getExpToDrop();
		
		if (amount > 0) {
			amount = Math.ceil(amount * ConfigurationService.EXP_MULTIPLIER_FISHING);
			ProjectileSource projectileSource = event.getHook().getShooter();
			
			if (projectileSource instanceof Player) {
				int enchantmentLevel = ((Player) projectileSource).getItemInHand().getEnchantmentLevel(Enchantment.LUCK);
				
				if (enchantmentLevel > 0L)
					amount = Math.ceil(amount * (enchantmentLevel * ConfigurationService.EXP_MULTIPLIER_LUCK_PER_LEVEL));
			}

			event.setExpToDrop((int) amount);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onFurnaceExtract(FurnaceExtractEvent event) {
		double amount = event.getExpToDrop();
		
		if (amount > 0)
			event.setExpToDrop((int) Math.ceil(amount * ConfigurationService.EXP_MULTIPLIER_SMELTING));
	}
	
}
