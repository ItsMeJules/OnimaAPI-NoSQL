package net.onima.onimaapi.nbt;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;

import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.NBTBase;
import net.minecraft.server.v1_7_R4.NBTTagCompound;

public class NBTWrapper { //Trouver un moyen de sauvegarder les tags. (ils se perdent au drop, mouvement d'item reload etc)
	
	private ItemStack crafItem;
	private NBTTagCompound compound;
	
	public NBTWrapper(org.bukkit.inventory.ItemStack item) {
		crafItem = CraftItemStack.asNMSCopy(item);
		compound = crafItem.hasTag() ? crafItem.getTag() : new NBTTagCompound();
	}

	public void add(String key, NBTBase base) {
		compound.set(key, base);
	}
	
	public Object getValue(String key, NBTType type) {
		return type.asNormal(compound.get(key));
	}

	public void remove(String key) {
		compound.remove(key);
	}
	
	public org.bukkit.inventory.ItemStack getItem() {
		crafItem.setTag(compound);
		return CraftItemStack.asCraftMirror(crafItem);
	}
	
	@Override
	public String toString() {
		return compound.toString();
	}
	
}
