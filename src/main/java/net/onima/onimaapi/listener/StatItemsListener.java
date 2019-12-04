package net.onima.onimaapi.listener;

import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_7_R4.ItemArmor;
import net.onima.onimaapi.items.StatArmor;
import net.onima.onimaapi.items.StatPickaxe;
import net.onima.onimaapi.items.StatSword;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.utils.MinedOres.Ore;

public class StatItemsListener implements Listener {
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		ItemStack hand = event.getPlayer().getItemInHand();
		
		if (hand != null && hand.getType() != Material.AIR && hand.getType().toString().contains("PICKAXE") && !event.getBlock().hasMetadata("Placed") && event.getBlock().toString().contains("ORE"))
			StatPickaxe.updatePickaxe(hand, event.getBlock(), StatPickaxe.getCount(hand, Ore.DIAMOND), StatPickaxe.getCount(hand, Ore.EMERALD), StatPickaxe.getCount(hand, Ore.GOLD), StatPickaxe.getCount(hand, Ore.REDSTONE), StatPickaxe.getCount(hand, Ore.LAPIS), StatPickaxe.getCount(hand, Ore.IRON), StatPickaxe.getCount(hand, Ore.COAL), StatPickaxe.getCount(hand, Ore.QUARTZ));
	}
	
	@EventHandler
	public void onKill(PlayerDeathEvent event) {
		APIPlayer apiPlayer = APIPlayer.getPlayer(event.getEntity());
		Player killer = apiPlayer.toPlayer().getKiller();
		String killerName = null;
		
		if 	(killer != null) {
			killerName = APIPlayer.getPlayer(killer).getName();
			ItemStack hand = killer.getItemInHand();
			
			if (hand.getType().toString().contains("SWORD"))
				StatSword.addKill(hand, killerName, apiPlayer.getName());
		}
		
		Iterator<ItemStack> iterator = event.getDrops().iterator();
		
		while (iterator.hasNext()) {
			ItemStack item = iterator.next();

			if (CraftItemStack.asNMSCopy(item).getItem() instanceof ItemArmor)
				StatArmor.addDeath(item, killerName == null ? null : killerName, apiPlayer.getName());
		}
	}

}
