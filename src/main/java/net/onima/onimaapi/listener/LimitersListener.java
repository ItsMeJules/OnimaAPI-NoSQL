package net.onima.onimaapi.listener;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.limiter.EnchantLimiter;
import net.onima.onimaapi.limiter.PotionLimiter;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;

public class LimitersListener implements Listener {
	
	private EnchantLimiter enchantLimiter;
	private PotionLimiter potionLimiter;
	
	{
		enchantLimiter = OnimaAPI.getInstance().getEnchantLimiter();
		potionLimiter = OnimaAPI.getInstance().getPotionLimiter();
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onEnchantItem(EnchantItemEvent event) {
		Iterator<Entry<Enchantment, Integer>> iterator = event.getEnchantsToAdd().entrySet().iterator();
		Map<Enchantment, Integer> limiters = enchantLimiter.getLimitedEnchantments();
		
		while (iterator.hasNext()) {
			Entry<Enchantment, Integer> enchant = iterator.next();
			
			if (limiters.containsKey(enchant.getKey())) {
				int max = limiters.get(enchant.getKey());
				
				if (max == 0)
					iterator.remove();
				
				if (enchant.getValue() > max)
					enchant.setValue(max);
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerFish(PlayerFishEvent event) {
		Entity entity = event.getCaught();
		
		if (entity instanceof Item)
			enchantLimiter.limit(((Item) entity).getItemStack());
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			for (ItemStack item : event.getDrops())
				enchantLimiter.limit(item);
		}
	}
	
	@EventHandler
	public void onItemConsume(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		
		if (potionLimiter.isDisabled(event.getItem())) {
			player.sendMessage("§cDésolé, mais cette potion est désactivée.");
			
			for (Player staff : Bukkit.getOnlinePlayers()) {
				if (OnimaPerm.FORBIDDEN_POTION_WARN.has(staff)) {
					APIPlayer apiPlayer = APIPlayer.getPlayer(player);
					
					new JSONMessage("§7[§4!§7] §c§o" + apiPlayer.getDisplayName(true) + " a essayé d'utiliser une potion interdite ! Cliquez pour vous téléporter.", "Se téléporter à " + apiPlayer.getDisplayName(true), true, "/tp " + apiPlayer.getName()).send(staff);
				}
			}
				
			event.setCancelled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPotionSplash(PotionSplashEvent event) {
		ThrownPotion potion = event.getEntity();
		
		if (potionLimiter.isDisabled(potion.getItem()) && potion.getShooter() instanceof Player) {
			Player shooter = (Player) potion.getShooter();
			
			if (OnimaPerm.FORBIDDEN_POTION_BYPASS.has(shooter))
				return;
			
			shooter.sendMessage("§cDésolé, mais cette potion est désactivée.");
			
			for (Player staff : Bukkit.getOnlinePlayers()) {
				if (OnimaPerm.FORBIDDEN_POTION_WARN.has(staff)) {
					APIPlayer apiPlayer = APIPlayer.getPlayer(shooter);
					
					new JSONMessage("§7[§4!§7] §c§o" + apiPlayer.getDisplayName(true) + " a essayé d'utiliser une potion interdite ! Cliquez pour vous téléporter.", "Se téléporter à " + apiPlayer.getDisplayName(true), true, "/tp " + apiPlayer.getName()).send(staff);
				}
			}
			
			event.setCancelled(true);
		}
	}
	
	  
	@EventHandler
	public void onBrew(BrewEvent event) {
	    if (potionLimiter.isDisabled(event.getContents().getItem(0)))
	    	event.setCancelled(true);
	}
	
}
