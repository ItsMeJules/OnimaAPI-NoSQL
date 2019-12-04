package net.onima.onimaapi.crates.prizes;

import java.util.AbstractMap;
import java.util.Map.Entry;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.saver.SerializableString;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.InstantFirework;
import net.onima.onimaapi.utils.OSound;

public abstract class Prize implements SerializableString {

	protected double chance;
	protected ItemStack displayItem;
	protected PrizeRarity rarity;
	protected FireworkEffect effect;
	protected Entry<Hologram, Item> entry;
	
	public Prize(double chance, ItemStack displayItem, PrizeRarity rarity) {
		this.chance = chance;
		this.rarity = rarity;
		
		BetterItem item = new BetterItem(displayItem, displayItem.getItemMeta().getDisplayName(), displayItem.getItemMeta().getLore());
		item.addLore(ConfigurationService.CRATE_PRIZE_LORE_LINE.replace("%rarity%", rarity.getNiceName()).replace("%chance%", String.valueOf(chance)));
		
		this.displayItem = item.toItemStack();
	}
	
	public void hologram(Location location) {
		ItemStack floattingItem = new BetterItem(displayItem, displayItem.getItemMeta().getDisplayName(), String.valueOf(OnimaAPI.RANDOM.nextInt(Integer.MAX_VALUE))).toItemStack();
		Item item = location.getWorld().dropItem(location, floattingItem);

		if (effect != null)
			firework(location);
		else
			new OSound(Sound.CHICKEN_EGG_POP, 1F, 2.0F).play(location);
			
		Hologram hologram = HologramsAPI.createHologram(OnimaAPI.getInstance(), item.getLocation().add(0, 0.2D, 0));
			
		hologram.appendTextLine(floattingItem.getItemMeta().getDisplayName());

		item.setMetadata("crate-display", new FixedMetadataValue(OnimaAPI.getInstance(), "FDP"));
		item.setPickupDelay(Integer.MAX_VALUE);
		item.setVelocity(new Vector(0, 0.2D, 0));
		
		entry = new AbstractMap.SimpleEntry<Hologram, Item>(hologram, item);
	}
	
	public void clearHologram() {
		entry.getKey().delete();
		entry.getValue().remove();
	}
	
	public void firework(Location location) {
		InstantFirework.spawn(location.add(0, 1D, 0), effect);
	}

	public abstract boolean give(Player player);
	
	public double getChance() {
		return chance;
	}
	
	public ItemStack getDisplayItem() {
		return displayItem;
	}
	
	public PrizeRarity getPrizeRarity() {
		return rarity;
	}
	
	public FireworkEffect getEffect() {
		return effect;
	}

	public void setEffect(boolean bool) {
		if (bool)
			effect = FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.RED).withColor(Color.AQUA).withColor(Color.ORANGE).withColor(Color.YELLOW).trail(false).flicker(false).build();
		else
			effect = null;
	}
	
	@Override
	public String asSerializableString() {
		return chance + ";" + rarity.name() + ";" + String.valueOf(effect != null);
	}
	
}
