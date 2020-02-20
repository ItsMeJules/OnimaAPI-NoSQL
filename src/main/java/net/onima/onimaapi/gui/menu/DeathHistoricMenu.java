package net.onima.onimaapi.gui.menu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Sound;
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
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.saver.inventory.InventorySaver.InventoryItem;
import net.onima.onimaapi.saver.inventory.PlayerSaver;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.OSound;
import net.onima.onimaapi.utils.callbacks.APICallback;

public class DeathHistoricMenu extends PageMenu {
	
	private static DisplayButton separation;
	private static OSound yes;
	
	static {
		separation = new DisplayButton(new BetterItem(Material.STAINED_GLASS_PANE, 1, 10, "§4 "));
		yes = new OSound(Sound.NOTE_PIANO, 0.1F, 20F);
	}
	
	private OfflineAPIPlayer offline;

	public DeathHistoricMenu(OfflineAPIPlayer offline) {
		super("death_historic", "§eHistorique §c» §9" + offline.getName(), MAX_SIZE, false);
		
		this.offline = offline;
		permission = OnimaPerm.RESTORE_COMMAND;
	}

	@Override
	public Map<Integer, Button> getAllPagesItems() {
		HashMap<Integer, Button> map = new HashMap<>();
		int index = 0;
		
		for (PlayerSaver saver : offline.getPlayerDataSaved()) {
			if (saver.getSaveType() != PlayerSaver.SaveType.DEATH || saver.isInRestoreRequest())
				continue;
		
			map.put(index, new DeathHistoricButton(saver));
			index++;
		}
		
		return map;
	}
	
	@Override
	public Map<Integer, Button> getGlobalButtons() {
		HashMap<Integer, Button> map = new HashMap<>();
		
		map.put(52, new DeleteAllButton());
		
		return map;
	}

	@Override
	public int getMaxItemsPerPage() {
		return 52;
	}

	private class DeathHistoricButton implements Button {

		private PlayerSaver saver;

		public DeathHistoricButton(PlayerSaver saver) {
			this.saver = saver;
		}
		
		@Override
		public BetterItem getButtonItem(Player player) {
			List<String> lore = Arrays.asList("§7Raison de la mort : §r" + saver.getMessage(), "§cClick droit pour supprimer", "§aClick gauche pour ouvrir.");
			
			return new BetterItem(Material.BOOK, 1, 0, "§c§l" + Methods.toFormatDate(saver.getSavedTime(), ConfigurationService.DATE_FORMAT_HOURS), lore);
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
			
			ClickType click = event.getClick();
			APIPlayer apiPlayer = APIPlayer.getPlayer(clicker);
			
			switch (click) {
			case RIGHT:
				new ConfirmationMenu("§c§lSupprimer cet inventaire.", (bool) -> {
					if (bool)
						offline.getPlayerDataSaved().remove(saver);

					DeathHistoricMenu.this.open(apiPlayer);
					return true;
				}, false, new DisplayButton(getButtonItem(clicker))).open(apiPlayer);
				break;
			case LEFT:
				new RestoreMenu(offline, apiPlayer, saver).open(apiPlayer);
				break;
			default:
				break;
			}
		}
		
	}
	
	private class DeleteAllButton implements Button {

		@Override
		public BetterItem getButtonItem(Player player) {
			return new BetterItem(Material.BLAZE_POWDER, 1, 0, "§c§lTout supprimer");
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
			
			APIPlayer apiPlayer = APIPlayer.getPlayer(clicker);
			
			new ConfirmationMenu("§c§lTout supprimer.", (bool) -> {
				if (bool) {
					offline.getPlayerDataSaved().removeIf(sav -> sav.getSaveType() == PlayerSaver.SaveType.DEATH);
					clicker.sendMessage("§aVous avez §csupprimé §atous les inventaires sauvegardés de " + offline.getName());
				} else
					DeathHistoricMenu.this.open(apiPlayer);
				return true; 
			}, true, new DisplayButton(getButtonItem(clicker))).open(apiPlayer);
		}
		
	}
	
	public class RestoreMenu extends PacketMenu {

		private OfflineAPIPlayer offline;
		private APIPlayer restorer;
		private PlayerSaver saver;
		
		public RestoreMenu(OfflineAPIPlayer offline, APIPlayer restorer, PlayerSaver saver) {
			super("pdpd", "§7Restore §c» §e" + offline.getName(), MAX_SIZE, false);
			
			this.offline = offline;
			this.restorer = restorer;
			this.saver = saver;
		}

		@Override
		public void registerItems() {
			DeathHistoricMenu historic = (DeathHistoricMenu) offline.getMenu("death_historic");
			BackButton backButton = new BackButton(historic);
			
			buttons.put(36, separation);
			buttons.put(37, new DisplayButton(new BetterItem(Material.EXP_BOTTLE, 1, 0, "§cExperience", "§7Points d'expérience : §e" + saver.getXp())));
			buttons.put(38, separation);
			buttons.put(39, backButton);
			buttons.put(40, new DisplayHeadButton(offline));
			buttons.put(41, backButton);
			buttons.put(42, separation);
			buttons.put(43, new DisplayButton(new BetterItem(Material.BREWING_STAND_ITEM, saver.getEffects().size(), 0, "§cEffet" + (saver.getEffects().size() > 1 ? "s" : "") + " de potion", Methods.setEffectsAsInInventory(saver.getEffects(), "§f", "§7"))));
			buttons.put(44, separation);
			
			buttons.put(45, new ConfirmationButton(false, false, new APICallback<Boolean>() {
				
				@Override
				public boolean call(Boolean bool) {
					historic.open(restorer);
					return false;
				}
			}));
			
			buttons.put(46, separation);
			buttons.put(47, new DisplayButton(new BetterItem(Material.BOOK, 1, 0, "§6Armure", "§7Armure de §9" + offline.getName())));
			buttons.put(52, separation);
			buttons.put(53, new RestoreButton());
			
			for (InventoryItem item : saver.getItems()) {
				if (item != null) {
					int slot = item.getSlot();
					
					if (slot >= 0 && slot <= 8)
						slot += 27;
					else
						slot -= 9;
					
					inventory.setItem(slot, item.getItem());
				}
			
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
				lore.add(ConfigurationService.STAIGHT_LINE);
				lore.add("§7Mort le : §e" + Methods.toFormatDate(saver.getSavedTime(), ConfigurationService.DATE_FORMAT_HOURS));
				lore.add("§7Raison : " + saver.getMessage());
			}
			
		}
		
		private class RestoreButton implements Button {

			@Override
			public BetterItem getButtonItem(Player player) {
				return new BetterItem(Material.STAINED_GLASS_PANE, 1, 5, "§2Restaurer", "§aRestaurer cet inventaire...");
			}

			@Override
			public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
				event.setCancelled(true);

				new AnvilInputMenu(clicker.getUniqueId(), "lol", "lol", "Entrez une raison.") {
					
					@Override
					public void onEvent(AnvilClickEvent event) {
						if (event.getSlot() == AnvilSlot.OUTPUT) {
							new RestoreRequest(clicker.getUniqueId(), offline.getUUID(), saver.getId(), Methods.colors(event.getInput())).register(saver);
							yes.play(clicker);
							event.setWillClose(true);
							event.setWillDestroy(true);
						}
					}
					
				}.open(APIPlayer.getPlayer(clicker));
			}
			
		}
		
	}
	
}
