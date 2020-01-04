package net.onima.onimaapi.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.common.collect.Lists;

public class BetterItem implements Cloneable {
	
	private String name;
	private List<String> lore;
	private ItemStack item;
	private Material material;
	private int amount;
	private short damage;
	private Map<Enchantment, Integer> enchantments;
	private OfflinePlayer offlinePlayer;
	
	public BetterItem(Material material, int amount, int damage, String name, List<String> lore) {
		this.material = material;
		this.amount = amount;
		this.damage = (short) damage;
		this.name = name;
		
		if (lore != null && !lore.isEmpty())
			this.lore = lore;
	}
	
	public BetterItem(int amount, String name, List<String> lore, OfflinePlayer offlinePlayer) {
		this.name = name;
		this.offlinePlayer = offlinePlayer;
		this.amount = amount;
		
		if (lore != null && !lore.isEmpty())
			this.lore = lore;
	}
	
	public BetterItem(Material material, int amount, int damage, String name, String... lore) {
		this(material, amount, damage, name, lore == null ? null : Lists.newArrayList(lore));
	}
	
	public BetterItem(Material material, int amount, int damage, String name) {
		this(material, amount, damage, name, new String[0]);
	}
	
	public BetterItem(Material material, int amount, int damage) {
		this(material, amount, damage, null, new String[0]);
	}
	
	public BetterItem(Material material, int amount) {
		this(material, amount, 0, null, new String[0]);
	}
	
	public BetterItem(ItemStack item, String name, String... lore) {
		this(item.getType(), item.getAmount(), item.getDurability(), name, lore);
	}
	
	public BetterItem(ItemStack item, String name, List<String> lore) {
		this(item.getType(), item.getAmount(), item.getDurability(), name, lore);
	}
	
	public BetterItem setLore(String... lore) {
		this.lore = Arrays.asList(lore);
		return this;
	}
	
	public BetterItem setLore(List<String> lore) {
		this.lore = lore;
		return this;
	}
	
	public BetterItem addLore(String str) {
		if (lore == null) lore = new ArrayList<>();
		lore.add(str);
		return this;
	}
	
	public BetterItem removeLore(int index) {
		if (lore == null) return this;
		lore.remove(index);
		return this;
	}
	
	public BetterItem setLastInLore(String str) {
		if (lore == null) {
			lore = new ArrayList<>();
			lore.add(str);
		}

		lore.set(lore.size() - 1, str);
		return this;
	}
	
	public BetterItem setName(String name) {
		this.name = name;
		return this;
	}
	
	public BetterItem addEnchant(Enchantment enchantment, int level) {
		if (enchantments == null)
			enchantments = new HashMap<>();
		
		this.enchantments.put(enchantment, level);
		return this;
	}
	
	public BetterItem setEnchants(Map<Enchantment, Integer> enchantments) {
		this.enchantments = enchantments;
		return this;
	}
	
	public boolean hasLore() {
		return lore != null;
	}
	
	public boolean hasName() {
		return name != null;
	}
	
	public String getName() {
		return name;
	}
	
	public List<String> getLore() {
		return lore;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public short getDamage() {
		return damage;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public ItemStack toItemStack() {
		ItemMeta meta = null;
		
		if (material == null && offlinePlayer != null && (offlinePlayer.hasPlayedBefore() || offlinePlayer.isOnline())) {
			material = Material.SKULL_ITEM;
			damage = 3;
			SkullMeta sMeta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(material);
			
			sMeta.setDisplayName(name);
			sMeta.setOwner(Methods.getRealName(offlinePlayer));
			meta = sMeta;
		}
		
		item = new ItemStack(material, amount, damage);
		if (meta == null)
			meta = item.getItemMeta();
		
		if (name != null)
			meta.setDisplayName(name);
		if (lore != null && !lore.isEmpty())
			meta.setLore(lore);
		if (enchantments != null) {
			for (Entry<Enchantment, Integer> entry : enchantments.entrySet())
				meta.addEnchant(entry.getKey(), entry.getValue(), true);
		}
		
		item.setItemMeta(meta);
		
		return item;
	}
	
	@Override
	public BetterItem clone() {
		BetterItem cloned = offlinePlayer == null ? new BetterItem(material, amount, damage, name, lore == null ? null : Lists.newArrayList(lore)) : new BetterItem(amount, name, lore == null ? null : Lists.newArrayList(lore), offlinePlayer);
		
		cloned.setEnchants(enchantments);
		
		return cloned;
	}
	
}