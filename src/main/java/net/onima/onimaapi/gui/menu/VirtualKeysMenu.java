package net.onima.onimaapi.gui.menu;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.crates.openers.VirtualKey;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.PacketMenuType;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.Methods;

public class VirtualKeysMenu extends PacketMenu {

	private OfflineAPIPlayer offline;
	
	public VirtualKeysMenu(OfflineAPIPlayer offline) {
		super("virtual_keys", "§2Vos clefs §5virtuelles", PacketMenuType.HOPPER, false);
		
		this.offline = offline;
	}

	@Override
	public void registerItems() {
		for (VirtualKey key : offline.getVirtualKeys().values())
			buttons.put(buttons.size(), new VirtualKeyButton(key, key.getAmount()));
	}
	
	public class VirtualKeyButton implements Button {
		
		private VirtualKey key;
		private int amount;

		public VirtualKeyButton(VirtualKey key, int amount) {
			this.key = key;
			this.amount = amount;
		}
		
		@Override
		public BetterItem getButtonItem(Player player) {
			List<String> lore = Arrays.asList("§5Crate : §r" + key.getCrateName(), "§c§l(§7§l!§c§l) §r§oClick gauche pour utiliser cette clef.", "§c§l(§7§l!§c§l) §r§oClick du milieu pour voir les prix.", "§c§l(§7§l!§c§l) §r§oClick droit pour la donner à quelqu'un.");
			
			if (amount <= 64)
				return new BetterItem(Material.NETHER_STAR, amount, 0, "§7§lClef virtuelle", lore);
			else {
				lore.add("§7§oVous avez " + amount + " clefs de ce type.");
				return new BetterItem(Material.NETHER_STAR, 64, 0, "§7§lClef virtuelle", lore);
			}
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
			APIPlayer apiPlayer = APIPlayer.getPlayer(clicker);
			
			if (event.getClick() == ClickType.RIGHT) {
				
				new AnvilInputMenu(clicker.getUniqueId(), "NOLOSE", "lololol", "Entrer le nom du joueur.") {
					
					@Override
					public void onEvent(AnvilClickEvent anvilEvent) {
						if (anvilEvent.getSlot() == AnvilSlot.OUTPUT) {
							UUID uuid = UUIDCache.getUUID(anvilEvent.getInput());
							
							if (uuid == null) {
								anvilEvent.setWillClose(false);
								anvilEvent.setWillDestroy(false);
								clicker.sendMessage("§cLe joueur " + anvilEvent.getInput() + " n'existe pas !");
								return;
							}
							
							OfflineAPIPlayer.getPlayer(uuid, offlineTarget -> {
								VirtualKey key = ((VirtualKeyButton) VirtualKeysMenu.this.buttons.get(event.getSlot())).key;
								
								offlineTarget.addVirtualKey(key);
								offline.removeVirtualKey(key);
								clicker.sendMessage("§dVous §7avez envoyé une clef pour la crate " + key.getCrateName() + " à §d" + Methods.getName(offlineTarget) + "§7.");
								
								if (offlineTarget.isOnline())
									((APIPlayer) offlineTarget).sendMessage("§d" + Methods.getRealName((OfflinePlayer) clicker) + " §7vous a envoyé une clef pour la crate " + key.getCrateName() + "§7.");
							});
						}
					}
					
				}.open(apiPlayer);
				
			} else if (event.getClick() == ClickType.MIDDLE) {
				VirtualKey key = ((VirtualKeyButton) buttons.get(event.getSlot())).key;
				
				for (Crate crate : Crate.getCrates()) {
					if (crate.getName().equalsIgnoreCase(key.getCrateName()) || crate.getDisplayName().equalsIgnoreCase(key.getCrateName()))
						crate.preview(apiPlayer);
				}
				
			} else if (event.getClick() == ClickType.LEFT) {
				VirtualKey key = ((VirtualKeyButton) buttons.get(event.getSlot())).key;
				
				for (Crate crate : Crate.getCrates()) {
					if (crate.getName().equalsIgnoreCase(key.getCrateName()) || crate.getDisplayName().equalsIgnoreCase(key.getCrateName())) {
						crate.open(apiPlayer, key.getBooster());
						apiPlayer.removeVirtualKey(key);
					}
				}
			}
		}
		
	}

}
