package net.onima.onimaapi.gui.menu;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.utils.UpdatableButton;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.players.utils.MinedOres;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.BetterItem;

public class OresMenu extends PacketMenu { //TODO Quand on clique sur un minerai ca affiche plusieurs infos genre les stats, les coordoonées ou c miné etc
	
	private MinedOres minedOres;
	private Map<Material, OreButton> items;

	{
		items = new HashMap<>();
	}
	
	public OresMenu(OfflineAPIPlayer offline) {
		super(offline.getName() + "_ores", "§eMinerais de : " + offline.getName(), 9, true);
		
		minedOres = offline.getMinedOres();
		permission = OnimaPerm.ONIMAAPI_COMMAND_ORES;
		
		items.put(Material.DIAMOND_ORE, new OreButton(Material.DIAMOND_ORE, "§bDiamants : %value%", 0));
		items.put(Material.EMERALD_ORE, new OreButton(Material.EMERALD_ORE, "§aEmeraudes : %value%", 1));
		items.put(Material.GOLD_ORE, new OreButton(Material.GOLD_ORE, "§eOrs : %value%", 2));
		items.put(Material.LAPIS_ORE, new OreButton(Material.LAPIS_ORE, "§9Lapis : %value%", 3));
		items.put(Material.REDSTONE_ORE, new OreButton(Material.REDSTONE_ORE, "§cRedstone : %value%", 4));
		items.put(Material.IRON_ORE, new OreButton(Material.IRON_ORE, "§7Fers : %value%", 5));
		items.put(Material.COAL_ORE, new OreButton(Material.COAL_ORE, "§9Charbons : %value%", 6));
		items.put(Material.QUARTZ_ORE, new OreButton(Material.QUARTZ_ORE, "§fQuartz : %value%", 7));
	}

	@Override
	public void registerItems() {
		for (OreButton button : items.values())
			buttons.put(button.slot, button);
	}
	
	public Map<Material, OreButton> getItems() {
		return items;
	}
	
	public class OreButton implements UpdatableButton {

		private Material material;
		private String name;
		private int slot;
		
		public OreButton(Material material, String name, int slot) {
			this.material = material;
			this.name = name;
			this.slot = slot;
		}
		
		@Override
		public BetterItem getButtonItem(Player player) {
			return new BetterItem(material, 1, 0, name.replace("%value%", String.valueOf(minedOres.getOre(material))));
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
		}
		
		@Override
		public void update(PacketMenu menu) {
			inventory.setItem(slot, getButtonItem(null).toItemStack());
		}

	}
	
}
