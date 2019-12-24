package net.onima.onimaapi.mod;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.mod.items.CompassBooster;
import net.onima.onimaapi.mod.items.InventoryOpener;
import net.onima.onimaapi.mod.items.PlayerFreezer;
import net.onima.onimaapi.mod.items.PlayerMounter;
import net.onima.onimaapi.mod.items.RandomTeleport;
import net.onima.onimaapi.mod.items.StaffCounter;
import net.onima.onimaapi.mod.items.VanishOFF;
import net.onima.onimaapi.mod.items.VanishON;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.saver.Saver;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.OSound;

public abstract class ModItem implements Saver {
	
	public static final String MOD_PREFIX;
	
	protected static OSound useSucces, useFail;
	protected static List<ModItem> modItems;
	
	static {
		MOD_PREFIX = "§8[§c§lMOD§8]§r";
	
		useSucces = new OSound(Sound.NOTE_PIANO, 15F, 20F);
		useFail = new OSound(Sound.DIG_GRASS, 0.1F, 20F);
		modItems = new ArrayList<>();
		
		modItems.add(new CompassBooster());
		modItems.add(new InventoryOpener());
		modItems.add(new PlayerFreezer());
		modItems.add(new PlayerMounter());
		modItems.add(new RandomTeleport());
		modItems.add(new StaffCounter());
		modItems.add(new VanishOFF());
		modItems.add(new VanishON());
	}
	
	protected String name;
	protected int slot;
	protected BetterItem item;
	
	public ModItem(String name, int slot, BetterItem item) {
		this.name = name;
		this.slot = slot;
		this.item = item;
		save();
	}
	
	public abstract void rightClick(APIPlayer player);
	public abstract void leftClick(APIPlayer player);
	public abstract void update(APIPlayer... players);
	
	public String getName() {
		return name;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public BetterItem getItem() {
		return item;
	}
	
	@Override
	public boolean isSaved() {
		return modItems.contains(this);
	}
	
	@Override
	public void remove() {
		modItems.remove(this);
	}
	
	@Override
	public void save() {
		modItems.add(this);
	}
	
	public static List<ModItem> getModItems() {
		return modItems;
	}
	
	public static ModItem fromStack(ItemStack item) {
		return modItems.stream().filter(stack -> Methods.isSimilar(item, stack.item.toItemStack())).findFirst().orElse(null);
	}
	
	public static ModItem fromName(String name) {
		return modItems.stream().filter(stack -> stack.name.equalsIgnoreCase(name)).findFirst().orElse(null);
	}

}
