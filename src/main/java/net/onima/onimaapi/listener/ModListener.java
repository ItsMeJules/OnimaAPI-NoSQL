package net.onima.onimaapi.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.mod.EntityClickable;
import net.onima.onimaapi.mod.ModItem;
import net.onima.onimaapi.mod.items.PlayerMounter;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.utils.PlayerOption;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.Methods;

public class ModListener implements Listener {
	
	private boolean entityRightClick;
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		ItemStack hand = event.getItem();
		
		if (hand != null && hand.getType() != Material.AIR) {
			Action action = event.getAction();
			APIPlayer player = APIPlayer.getPlayer(event.getPlayer());
			
			if (!player.isInModMode())
				return;
			
			ModItem modItem = ModItem.fromStack(hand);
			
			if (action.toString().contains("RIGHT") && !entityRightClick)
				modItem.rightClick(player);
			else if (action.toString().contains("LEFT") && Methods.getEntityTargeting(player.toPlayer(), event.getPlayer().getGameMode() == GameMode.CREATIVE ? 5 : 4) == null)
				modItem.leftClick(player);
			
			entityRightClick = false;
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		Player attacker = Methods.getLastAttacker(event);
		
		if (attacker != null) {
			ItemStack hand = attacker.getItemInHand();
			
			if (hand != null && hand.getType() != Material.AIR) {
				APIPlayer apiAttacker = APIPlayer.getPlayer(attacker);
				
				if (!apiAttacker.isInModMode())
					return;
				
				if (event.getDamage() == PlayerMounter.DAMAGE)
					return;
				
				ModItem modItem = ModItem.fromStack(hand);
				
				if (modItem instanceof EntityClickable) {
					EntityClickable clickable = (EntityClickable) modItem;
					
					clickable.entityAttack(apiAttacker, event.getEntity());
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		ItemStack hand = player.getItemInHand();
		
		if (hand != null && hand.getType() != Material.AIR) {
			APIPlayer apiPlayer = APIPlayer.getPlayer(player);
			
			if (!apiPlayer.isInModMode())
				return;
			
			ModItem modItem = ModItem.fromStack(hand);
			
			if (modItem instanceof EntityClickable) {
				EntityClickable clickable = (EntityClickable) modItem;
				
				entityRightClick = true;
				clickable.entityInteract(apiPlayer, event.getRightClicked());
			}
		}
	}
	
	@EventHandler
	public void onPickupItem(PlayerPickupItemEvent event) {
		APIPlayer player = APIPlayer.getPlayer(event.getPlayer());
		
		if (player.isInModMode() && player.getOptions().getBoolean(PlayerOption.ModOptions.PICKUP_ITEM))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onDropItem(PlayerDropItemEvent event) {
		APIPlayer player = APIPlayer.getPlayer(event.getPlayer());
		
		if (player.isInModMode()) {
			if (player.getOptions().getBoolean(PlayerOption.ModOptions.DROP_ITEM))
				event.setCancelled(true);
			else {
				if (ModItem.fromStack(event.getItemDrop().getItemStack()) != null)
					event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		APIPlayer player = APIPlayer.getPlayer(event.getPlayer());
		
		if (player.isInModMode() && player.getOptions().getBoolean(PlayerOption.ModOptions.BREAK_BLOCK))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		APIPlayer player = APIPlayer.getPlayer(event.getPlayer());
		
		if (player.isInModMode() && player.getOptions().getBoolean(PlayerOption.ModOptions.PLACE_BLOCK))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerAttack(EntityDamageByEntityEvent event) {
		Entity entity = event.getEntity();
		
		if (entity instanceof Player) {
			Player attacker = Methods.getLastAttacker(event);
			
			if (attacker != null) {
				APIPlayer player = APIPlayer.getPlayer(attacker);
				
				if (player.isInModMode() && player.getOptions().getBoolean(PlayerOption.ModOptions.ATTACK_PLAYER))
					event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent event) {
		if (OnimaPerm.STAFF_COUNTER_COUNT.has(event.getPlayer())) {
			ModItem.fromName("staff_counter").update(Methods.getPlayersInModMode().toArray(new APIPlayer[Methods.getPlayersInModMode().size()]));
			PacketMenu.getMenu("online_staff").updateItems(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		APIPlayer player = APIPlayer.getPlayer(event.getPlayer());
				
		if (player.isInModMode())
			player.setModMode(false);
		
		if (OnimaPerm.STAFF_COUNTER_COUNT.has(player.toPlayer())) {
			Bukkit.getScheduler().runTask(OnimaAPI.getInstance(), () -> {
				ModItem.fromName("staff_counter").update(Methods.getPlayersInModMode().toArray(new APIPlayer[Methods.getPlayersInModMode().size()]));
				PacketMenu.getMenu("online_staff").updateItems(player.toPlayer());
			});
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		
		if (entity instanceof Player)
			event.setCancelled(APIPlayer.getPlayer((Player) entity).isInModMode());
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if (player.isSneaking())
			return;
		
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.useInteractedBlock() == Event.Result.DENY)
				return;
		
			Block block = event.getClickedBlock();
			APIPlayer apiPlayer = null;
			
			if (block.getState() instanceof Chest
					&& (apiPlayer = APIPlayer.getPlayer(player)).getOptions().getBoolean(PlayerOption.ModOptions.SILENT_CHEST)
					&& !apiPlayer.silentChest(block.getLocation()))
				event.setCancelled(true);
		}
		
	}
	
}
