package net.onima.onimaapi.listener;

import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.items.Crowbar;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.signs.HCFSign;
import net.onima.onimaapi.signs.SignShop;
import net.onima.onimaapi.signs.SignShop.DisplayLines;
import net.onima.onimaapi.utils.Balance;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;

public class ShopSignListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSignChange(SignChangeEvent event) {
		if (!OnimaPerm.CREATE_SIGN_SHOP.has(event.getPlayer()))
			return;
		
		String[] lines = event.getLines();
		boolean buy = lines[0].equalsIgnoreCase(ChatColor.stripColor(ConfigurationService.SHOP_SIGN_BUY_LINE));
	
		if (buy || lines[0].equalsIgnoreCase(ChatColor.stripColor(ConfigurationService.SHOP_SIGN_SELL_LINE))) {
			Block block = event.getBlock();
			APIPlayer apiPlayer = APIPlayer.getPlayer(event.getPlayer());
			String[] matInfo = lines[1].split(":");
			String damageStr = matInfo.length == 1 ? "0" : matInfo[1]; 
			boolean fail = false;
			
			Integer materialID = Methods.toInteger(matInfo[0]);
			
			if (materialID == null) {
				apiPlayer.sendMessage("§c" + matInfo[0]  + " n'est pas un nombre !");
				fail = true;
			}
			
			Material material = Material.getMaterial(materialID);

			if (material == null && materialID != Crowbar.getID()) {
				apiPlayer.sendMessage("§cL'id " + materialID + " n'existe pas !");
				fail = true;
			}
			
			Integer damage = Methods.toInteger(damageStr);
			
			if (damage == null) {
				apiPlayer.sendMessage("§c" + damageStr  + " n'est pas un nombre !");
				fail = true;
			}
			
			Integer stackSize = Methods.toInteger(lines[2]);
			
			if (stackSize == null) {
				apiPlayer.sendMessage("§c" + lines[2]  + " n'est pas un nombre !");
				fail = true;
			}
			
			Double price = Methods.toDouble(lines[3]);
			
			if (price == null) {
				apiPlayer.sendMessage("§c" + lines[3]  + " n'est pas un nombre !");
				fail = true;
			}
			
			if (buy && materialID == Crowbar.getID()) {
				apiPlayer.sendMessage("§cUn joueur ne peut pas vendre de crowbar !");
				fail = true;
			}
			
			if (fail) {
				block.setType(Material.AIR);
				return;
			}
			
			SignShop sign = new SignShop((Sign) block.getState(), buy);
			
			sign.setStackSize(stackSize);
			sign.setMaterial(materialID, damage);
			sign.setPrice(price);
			sign.updateLines(true);
		}
	}
	
	@EventHandler
	public void onSignClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block clicked = event.getClickedBlock();
		
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		
		if (clicked.getType().toString().contains("SIGN")) {
			event.setCancelled(true);
			
			APIPlayer apiPlayer = APIPlayer.getPlayer(player);
			HCFSign hcfSign = HCFSign.getSign((Sign) clicked.getState());
			
			if (!(hcfSign instanceof SignShop)) return;
			
			SignShop sign = (SignShop) hcfSign;
			Balance balance = apiPlayer.getBalance();
			
			if (sign.isBuySign()) {
				ItemStack item = Methods.getItem(player.getInventory(), sign.getMaterialtID());
				
				if (balance.isBanned()) {
					sign.displayLines(apiPlayer, DisplayLines.BALANCE_BANNED);
					return;
				} else if (item == null) {
					sign.displayLines(apiPlayer, DisplayLines.ITEMS_MISSING);
					return;
				} else {
					int stackSize = sign.getStackSize();
					int quantity = Math.min(stackSize, item.getAmount());
					double price = sign.getPrice() / stackSize * quantity;
					
					Methods.removeItem(player.getInventory(), item.getType(), quantity);
					player.updateInventory();
					balance.addAmount(price);
					
					sign.displayLines(apiPlayer, price, quantity, DisplayLines.ITEMS_SOLD);
					return;
				}
			} else {
				if (balance.isBanned()) {
					sign.displayLines(apiPlayer, DisplayLines.BALANCE_BANNED);
					return;
				} else if (balance.getAmount() < sign.getPrice()) {
					sign.displayLines(apiPlayer, sign.getPrice(), 0, DisplayLines.NOT_ENOUGH_MONEY);
					return;
				} else {
					balance.removeAmount(sign.getPrice());
					sign.displayLines(apiPlayer, sign.getPrice(), sign.getStackSize(), DisplayLines.ITEMS_BOUGHT);
					
					World world = player.getWorld();
					Location location = player.getLocation();
					
					for (Entry<Integer, ItemStack> entry : player.getInventory().addItem(sign.getItem()).entrySet())
						world.dropItemNaturally(location, entry.getValue());

					player.updateInventory();
				}
			}
		}
	}
	
	@EventHandler
	public void onSignBreak(BlockBreakEvent event) {
		if (!OnimaPerm.CREATE_SIGN_SHOP.has(event.getPlayer()))
			return;
		
		if (event.getPlayer().isSneaking()) {
			HCFSign hcfSign = HCFSign.getSign(event.getBlock().getLocation());
			
			if (hcfSign != null && hcfSign instanceof SignShop)
				hcfSign.remove();
		
		} else {
			HCFSign hcfSign = HCFSign.getSign(event.getBlock().getLocation());
			
			if (hcfSign != null && hcfSign instanceof SignShop)
				event.setCancelled(true);
		}
	}
	
}
