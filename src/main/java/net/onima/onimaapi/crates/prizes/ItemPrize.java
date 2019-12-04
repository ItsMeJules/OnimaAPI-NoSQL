package net.onima.onimaapi.crates.prizes;

import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.crates.openers.PhysicalKey;
import net.onima.onimaapi.utils.Methods;

public class ItemPrize extends Prize {
	
	private ItemStack item;

	public ItemPrize(double chance, ItemStack item, PrizeRarity rarity) {
		super(chance, item, rarity);
		
		this.item = item;
	}

	@Override
	public boolean give(Player player) {
		Location location = player.getLocation();
		boolean succes = true;
		
		if (PhysicalKey.fromItem(item) != null)
			PhysicalKey.updateKey(item, player);
		
		for (Entry<Integer, ItemStack> extraItem : player.getInventory().addItem(item).entrySet()) {
			ItemStack item = extraItem.getValue();
			succes = false;
			
			location.getWorld().dropItemNaturally(location, item);
			player.sendMessage(item.getItemMeta().getDisplayName() + 'x' + item.getAmount() + " §7a été drop au sol car vous n'avez pas de place dans votre inventaire.");
		}
		
		return succes;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	@Override
	public String asSerializableString() {
		return super.asSerializableString() + ";" + Methods.serializeItem(item, true);
	}
	
	public static ItemPrize fromString(String[] parts) {
		Double chance = Methods.toDouble(parts[0]);
		PrizeRarity rarity = PrizeRarity.fromName(parts[1]);
		boolean effect = Boolean.valueOf(parts[2]);
		
		ItemPrize prize = new ItemPrize(chance, Methods.deserializeItem(parts[3], true), rarity);
		
		prize.setEffect(effect);
		
		return prize;
	}
	
}
