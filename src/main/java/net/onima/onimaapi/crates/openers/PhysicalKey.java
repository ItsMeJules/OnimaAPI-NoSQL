package net.onima.onimaapi.crates.openers;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.onima.onimaapi.crates.booster.KeyBooster;
import net.onima.onimaapi.crates.booster.NoBooster;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;

public class PhysicalKey extends Key {
	
	private static final String LETTERS_ONLY;
	private static final BetterItem BASE_ITEM;
	
	static {
		LETTERS_ONLY = "[^a-zA-Z]";
		BASE_ITEM = new BetterItem(Material.TRIPWIRE_HOOK, 1);
	}
	
	public PhysicalKey(String crateName, KeyBooster booster) {
		super(crateName, booster);
	}
	
	@Override
	public void give(APIPlayer apiPlayer, boolean sendMessage) {
		BetterItem item = BASE_ITEM.clone();
		
		item.setName(ConfigurationService.KEY_NAME.replace("%crate%", crateName));
		item.setLore(Methods.replacePlaceholder(ConfigurationService.KEY_LORE,
				"%date%", Methods.toFormatDate(System.currentTimeMillis(), ConfigurationService.DATE_FORMAT_HOURS)/*,
				"%player%", apiPlayer.getName()*/));
		
		if (!(booster instanceof NoBooster))
			item.addLore(ConfigurationService.KEY_BOOSTER.replace("%booster%", booster.loreLine()));
			
		Set<Entry<Integer, ItemStack>> extra = apiPlayer.toPlayer().getInventory().addItem(item.toItemStack()).entrySet();
		
		if (!extra.isEmpty()) {
			if (sendMessage)
				apiPlayer.sendMessage("§cVotre clef pour la crate " + crateName + " a été drop au sol car votre inventaire est plein.");
			
			Location location = apiPlayer.toPlayer().getLocation();
			
			for (Entry<Integer, ItemStack> entry : extra)
				location.getWorld().dropItemNaturally(location, entry.getValue());
		} else if (sendMessage)
			apiPlayer.sendMessage("§eVous §7avez reçu une clef pour ouvrir la crate §e" + crateName + "§7.");
	}
	
	public static void updateKey(ItemStack item, Player player) {
		BetterItem betterItem = new BetterItem(item, item.getItemMeta().getDisplayName(), item.getItemMeta().getLore());
		
		betterItem.setLore(Methods.replacePlaceholder(ConfigurationService.KEY_LORE,
				"%date%", Methods.toFormatDate(System.currentTimeMillis(), ConfigurationService.DATE_FORMAT_HOURS)/*,
				"%player%", player.getName()*/));
	}
	
	public static PhysicalKey fromItem(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		
		if (item.getType() == Material.TRIPWIRE_HOOK && meta.hasDisplayName()) {
			String[] parts = meta.getDisplayName().split("\\(");

			if (parts[0].equalsIgnoreCase(ConfigurationService.KEY_NAME.split("\\(")[0])) {
				Crate crate = Crate.getByName(ChatColor.stripColor(parts[1]).replaceAll(LETTERS_ONLY, ""));
				
				if (crate != null) {
					KeyBooster booster = new NoBooster();
					List<String> lore = meta.getLore();
					
					if (lore.size() >= 3 && lore.get(2).startsWith("§eBooster "))
						booster = KeyBooster.fromLine(lore.get(2));
					
					return new PhysicalKey(crate.getDisplayName() == null ? crate.getName() : crate.getDisplayName(), booster);
				}
			}
		}
		
		return null;
	}

}
