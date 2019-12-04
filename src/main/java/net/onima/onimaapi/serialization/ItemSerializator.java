package net.onima.onimaapi.serialization;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemSerializator {
	
	private ItemStack item;
	private ItemStack deserialize;
    private String serialization = "";
	
	public ItemSerializator(ItemStack item) {
		this.item = item;
	}
	
	public ItemSerializator(String serialization) {
		this.serialization = serialization;
	}
	
	@SuppressWarnings("deprecation")
	public void serialize() {
		if(item != null) {
			serialization += "t@"+item.getTypeId();
			
			if(item.getDurability() != 0)
				serialization += "!d@"+item.getDurability();
			
			if(item.getAmount() != 1)
				serialization += "!a@"+item.getAmount();
			
	        Map<Enchantment, Integer> enchants = item.getEnchantments();
	        
	        if(enchants.size() > 0) {
	            for(Entry<Enchantment, Integer> ench : enchants.entrySet())
	            	serialization += "!e@"+ench.getKey().getId()+"@"+ench.getValue();
	        }
		}
	}

	@SuppressWarnings("deprecation")
	public void deserialize() {
		boolean created = false;
		String[] itemInfo = serialization.split("!");
		ItemMeta meta = null;
		
		for(String info : itemInfo) {
			String[] attribute = info.split("@");
			switch(attribute[0]) {
			case "t":
				deserialize = new ItemStack(Material.getMaterial(Integer.valueOf(attribute[1])));
				meta = deserialize.getItemMeta();
				created = true;
				break;
			case "d":
				if(created) deserialize.setDurability(Short.valueOf(attribute[1]));
				break;
			case "a":
				if(created) deserialize.setAmount(Integer.valueOf(attribute[1]));
				break;
			case "e":
				if(created) meta.addEnchant(Enchantment.getById(Integer.valueOf(attribute[1])), Integer.valueOf(attribute[2]), false);
				break;
			default:
				break;
			}
		}
		deserialize.setItemMeta(meta);
	}

	public void serializeWithDetails() {
		serialize();
		ItemMeta meta = item.getItemMeta();
		
		if (item != null) {
			if (meta.hasDisplayName())
				serialization += "!n@"+meta.getDisplayName().replace("§", "&");
			
			if (meta.hasLore()) {
				for (String lore : meta.getLore())
					serialization += "!l@"+lore.replace("§", "&");
			}
		}
	}

	public void deserializeWithDetails() {
		deserialize();
		ItemMeta meta = deserialize.getItemMeta();
		String[] itemInfo = serialization.split("!");
		ArrayList<String> lore = new ArrayList<>();
		
		for(String info : itemInfo) {
			String[] attribute = info.split("@");
			switch(attribute[0]) {
			case "n":
				meta.setDisplayName(attribute[1].replace("&", "§"));
				deserialize.setItemMeta(meta);
				break;
			case "l":
				lore.add(attribute[1].replace("&", "§"));
			default:
				break;
			}
		}
		meta.setLore(lore);
		deserialize.setItemMeta(meta);
	}

	public ItemStack getDeserialized() {
		return deserialize;
	}


	public String getSerialized(){
		return serialization;
	}

}
