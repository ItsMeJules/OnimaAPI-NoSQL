package net.onima.onimaapi.gui.menu;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.BackButton;
import net.onima.onimaapi.gui.buttons.ConfirmationButton;
import net.onima.onimaapi.gui.buttons.DisplayButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.buttons.utils.HeadButton;
import net.onima.onimaapi.gui.menu.utils.PageMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.players.utils.RestoreRequest;
import net.onima.onimaapi.saver.inventory.InventorySaver.InventoryItem;
import net.onima.onimaapi.saver.inventory.PlayerSaver;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.callbacks.APICallback;

public class PendingRestoresMenu extends PageMenu {
	
	private static DisplayButton separation;
	
	static {
		separation = new DisplayButton(new BetterItem(Material.STAINED_GLASS_PANE, 1, 10, "§4 "));
	}

	private OfflineAPIPlayer offline;

	public PendingRestoresMenu(OfflineAPIPlayer offline) {
		super("pending_restores", "§cInventaires en attente.", MAX_SIZE, false);
		
		this.offline = offline;
	}

	@Override
	public Map<Integer, Button> getAllPagesItems() {
		HashMap<Integer, Button> map = new HashMap<>();
		int index = 0;
		
		for (RestoreRequest request : offline.getRestoreRequests()) {
			map.put(index, new RestoreButton(request));
			index++;
		}
		
		return map;
	}

	@Override
	public int getMaxItemsPerPage() {
		return 53;
	}

	public class RestoreButton implements Button {

		private RestoreRequest request;
		
		public RestoreButton(RestoreRequest request) {
			this.request = request;
		}
		
		@Override
		public BetterItem getButtonItem(Player player) {
			return new BetterItem(Material.ENCHANTED_BOOK, 1, 0, Methods.toFormatDate(request.getSaver(APIPlayer.getPlayer(player)).getSavedTime(), ConfigurationService.DATE_FORMAT_HOURS), "§7Restauré par : §9" + Methods.getRealName(Bukkit.getOfflinePlayer(request.getRestorer())), "§7Raison : §9" + request.getReason(), "", "", "§cClick droit pour annuler", "§aClick gauche pour ouvrir.");
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
			
			ClickType click = event.getClick();
			APIPlayer apiPlayer = APIPlayer.getPlayer(clicker);
			
			switch (click) {
			case RIGHT:
				new ConfirmationMenu("§c§lAnnuler cet inventaire.", (bool) -> {
					if (bool) {
						offline.getRestoreRequests().remove(request);
						request.getSaver(APIPlayer.getPlayer(clicker)).setInRestoreRequest(false);
					}
					
					PendingRestoresMenu.this.open(apiPlayer);
					return true;
				}, false, new DisplayButton(getButtonItem(clicker))).open(apiPlayer);
				break;
			case LEFT:
				new PendingInventoryMenu(request, apiPlayer).open(apiPlayer);
				break;
			default:
				break;
			}
		}
		
	}
	
	public class PendingInventoryMenu extends PacketMenu {

		private RestoreRequest request;
		private APIPlayer apiPlayer;
		
		public PendingInventoryMenu(RestoreRequest request, APIPlayer apiPlayer) {
			super("pending_inv", "§9Inventaire en attente.", MAX_SIZE, false);
			
			this.request = request;
			this.apiPlayer = apiPlayer;
		}

		@Override
		public void registerItems() {
			PendingRestoresMenu pending = (PendingRestoresMenu) offline.getMenu("pending_restores");
			BackButton backButton = new BackButton(pending);
			PlayerSaver saver = request.getSaver(apiPlayer);
			
			buttons.put(36, separation);
			buttons.put(37, new DisplayButton(new BetterItem(Material.EXP_BOTTLE, 1, 0, "§cExperience", "§7Points d'expérience : §e" + saver.getXp())));
			buttons.put(38, separation);
			buttons.put(39, backButton);
			buttons.put(40, new DisplayHeadButton(offline));
			buttons.put(41, backButton);
			buttons.put(42, separation);
			buttons.put(43, new DisplayButton(new BetterItem(Material.BREWING_STAND_ITEM, saver.getEffects().size(), 0, "§cEffet" + (saver.getEffects().size() > 1 ? "s" : "") + " de potion", Methods.setEffectsAsInInventory(saver.getEffects(), "§o", "§7§o"))));
			buttons.put(44, separation);
			
			buttons.put(45, new ConfirmationButton(false, false, new APICallback<Boolean>() {
				
				@Override
				public boolean call(Boolean bool) {
					pending.open(apiPlayer);
					return false;
				}
			}));
			
			buttons.put(46, separation);
			buttons.put(47, new DisplayButton(new BetterItem(Material.BOOK, 1, 0, "§6Armure")));
			buttons.put(52, separation);
			buttons.put(53, new AcceptButton());
			
			for (InventoryItem item : saver.getItems()) {
				if (item != null)
					inventory.setItem(item.getSlot(), item.getItem());
			}
			
			for (int i = 0; i < 4; i++)
				inventory.setItem(i + 48, saver.getArmor()[i]);
		}
		
		private class DisplayHeadButton extends HeadButton {
			
			{
				lore();
			}

			public DisplayHeadButton(OfflineAPIPlayer owner) {
				super(owner);
			}

			@Override
			public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
				event.setCancelled(true);
			}

			@Override
			protected void lore() {
				PlayerSaver saver = request.getSaver(owner);
				
				lore.add(ConfigurationService.STAIGHT_LINE);
				lore.add("§7Mort le : §e" + Methods.toFormatDate(saver.getSavedTime(), ConfigurationService.DATE_FORMAT_HOURS));
				lore.add("§7Raison : " + saver.getMessage());
			}
			
		}
		
		private class AcceptButton implements Button {

			@Override
			public BetterItem getButtonItem(Player player) {
				return new BetterItem(Material.STAINED_GLASS_PANE, 1, 5, "§aRecevoir cet inventaire.");
			}

			@Override
			public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
				event.setCancelled(true);
				request.accept();
			}
			
		}
		
	}
	
}
