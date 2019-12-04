package net.onima.onimaapi.gui.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.BackButton;
import net.onima.onimaapi.gui.buttons.MenuOpenerButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.menu.utils.PageMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.punishment.utils.Punishment;
import net.onima.onimaapi.punishment.utils.PunishmentType;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;

public class PunishmentsMenu extends PacketMenu {

	private OfflineAPIPlayer offline;
	private boolean canRemove;

	public PunishmentsMenu(OfflineAPIPlayer offline, boolean canRemove) {
		super("punishments", "§cPunitions", 9, true);
		
		this.offline = offline;
		this.canRemove = canRemove;
	}

	@Override
	public void registerItems() {
		int bans = (int) offline.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.BAN).count();
		List<String> bansLore = Lists.newArrayList(ConfigurationService.STAIGHT_LINE, "§e" + bans + " au total.");
		BetterItem banItem = new BetterItem(Material.ANVIL, bans == 0 ? 1 : bans, 0, "§cBannisement" + (bans > 1 ? 's' : "") + '.', bansLore);
		
		if (offline.hasPunishment(PunishmentType.BAN))
			banItem.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		
		int blacklist = (int) offline.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.BLACKLIST).count();
		List<String> blacklistsLore = Lists.newArrayList(ConfigurationService.STAIGHT_LINE, "§e" + blacklist + " au total.");
		BetterItem blacklistItem = new BetterItem(Material.SKULL_ITEM, blacklist == 0 ? 1 : blacklist, 1, "§8§lBlacklist.", blacklistsLore);
		
		if (offline.hasPunishment(PunishmentType.BLACKLIST))
			blacklistItem.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		
		int kicks = (int) offline.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.KICK).count();
		List<String> kicksLore = Lists.newArrayList(ConfigurationService.STAIGHT_LINE, "§e" + kicks + " au total.");
		BetterItem kickItem = new BetterItem(Material.BOW, kicks == 0 ? 1 : kicks, 0, "§eKick" + (kicks > 1 ? 's' : "") + '.', kicksLore);
		
		if (offline.hasPunishment(PunishmentType.KICK))
			kickItem.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		
		int mutes = (int) offline.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.MUTE).count();
		List<String> mutesLore = Lists.newArrayList(ConfigurationService.STAIGHT_LINE, "§e" + mutes + " au total.");
		BetterItem muteItem = new BetterItem(Material.BOOK_AND_QUILL, mutes == 0 ? 1 : mutes, 0, "§7Mute" + (mutes > 1 ? 's' : "") + '.', mutesLore);
		
		if (offline.hasPunishment(PunishmentType.MUTE))
			muteItem.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		
		int tempbans = (int) offline.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.TEMPBAN).count();
		List<String> tempbansLore = Lists.newArrayList(ConfigurationService.STAIGHT_LINE, "§e" + tempbans + " au total.");
		BetterItem tempbanItem = new BetterItem(Material.RECORD_3, tempbans == 0 ? 1 : tempbans, 0, "§cTempban" + (tempbans > 1 ? 's' : "") + '.', tempbansLore);
		
		if (offline.hasPunishment(PunishmentType.TEMPBAN))
			tempbanItem.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		
		int warns = (int) offline.getPunishments().stream().filter(punishment -> punishment.getType() == PunishmentType.WARN).count();
		List<String> warnsLore = Lists.newArrayList(ConfigurationService.STAIGHT_LINE, "§e" + warns + " au total.");
		BetterItem warnItem = new BetterItem(Material.BONE, warns == 0 ? 1 : warns, 0, "§6Warn" + (warns > 1 ? 's' : "") + '.', warnsLore);
		
		if (offline.hasPunishment(PunishmentType.WARN))
			warnItem.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		
		buttons.put(0, new MenuOpenerButton(banItem, new PunishmentTypeMenu("§cBannissements", PunishmentType.BAN)));
		buttons.put(1, new MenuOpenerButton(blacklistItem, new PunishmentTypeMenu("§8§lBlacklist", PunishmentType.BLACKLIST)));
		buttons.put(2, new MenuOpenerButton(kickItem, new PunishmentTypeMenu("§eKicks", PunishmentType.KICK)));
		buttons.put(3, new MenuOpenerButton(muteItem, new PunishmentTypeMenu("§7Mutes", PunishmentType.MUTE)));
		buttons.put(4, new MenuOpenerButton(tempbanItem, new PunishmentTypeMenu("§cTempbans", PunishmentType.TEMPBAN)));
		buttons.put(5, new MenuOpenerButton(warnItem, new PunishmentTypeMenu("§6Warns", PunishmentType.WARN)));
	}
	
	public class PunishmentTypeMenu extends PageMenu {

		private PunishmentType punishmentType;

		public PunishmentTypeMenu(String name, PunishmentType punishmentType) {
			super(punishmentType.name().toLowerCase() + "_menu", name, MAX_SIZE, false);
			
			this.punishmentType = punishmentType;
		}

		@Override
		public Map<Integer, Button> getAllPagesItems() {
			HashMap<Integer, Button> map = new HashMap<>();
			int index = 0;
			
			for (Punishment punishment : offline.getPunishments().stream().filter(punishment -> punishment.getType() == punishmentType).collect(Collectors.toList())) {
				buttons.put(index, new PunishmentButton(punishment));
				index++;
			}
			
			map.put(52, new BackButton(PunishmentsMenu.this));
			
			return map;
		}

		@Override
		public int getMaxItemsPerPage() {
			return 54;
		}

		@Override
		public PageMenu getPage(int page) {
			PunishmentTypeMenu menu = new PunishmentTypeMenu(title, punishmentType);
			menu.setCurrentPage(page);
			
			return menu;
		}

		@Override
		public boolean changePage(APIPlayer apiPlayer, int toAdd) {
			PunishmentTypeMenu menu = new PunishmentTypeMenu(title, punishmentType);
			menu.setCurrentPage(menu.getCurrentPage() + toAdd);
			
			menu.open(apiPlayer);
			return true;
		}

		@Override
		public boolean openPage(APIPlayer apiPlayer, int page) {
			PunishmentTypeMenu menu = new PunishmentTypeMenu(title, punishmentType);
			menu.setCurrentPage(page);
			
			menu.open(apiPlayer);
			return true;
		}

		
	}
	
	protected class PunishmentButton implements Button {

		private Punishment punishment;
		
		public PunishmentButton(Punishment punishment) {
			this.punishment = punishment;
		}
		
		@Override
		public BetterItem getButtonItem(Player player) {
			String status;
			BetterItem item = new BetterItem(Material.RECORD_8, 1, 0, "§e#" + punishment.getID() + " §7- §e" + Methods.toFormatDate(punishment.getIssued(), ConfigurationService.DATE_FORMAT_HOURS));
			
			if (punishment.isActive()) {
				status = "§aActif";
				item.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			} else if (punishment.isRemoved())
				status = "§eAnnulé";
			else
				status = "§7Expiré";
			
			item.addLore(ConfigurationService.STAIGHT_LINE);
			item.addLore("§eType : §7" + punishment.getType().name());
			item.addLore("§eStatut : §7" + status);
			item.addLore("§eAjouté par : " + (punishment.getSender().equals(ConfigurationService.CONSOLE_UUID) ? "§cConsole" : Methods.getRealName(Bukkit.getOfflinePlayer(punishment.getSender()))));
			item.addLore("§eRaison : " + punishment.getReason());
			
			if (punishment.isRemoved()) {
				item.addLore("§eAnnulé par : §7" + (punishment.getRemover().equals(ConfigurationService.CONSOLE_UUID) ? "§cConsole" : Methods.getRealName(Bukkit.getOfflinePlayer(punishment.getRemover()))));
				item.addLore("§eAnnulé pour : " + punishment.getRemoveReason());
			} else if (punishment.isActive() && canRemove)
				item.addLore("§fCliquez pour annuler cette punition.");
			
			item.addLore(ConfigurationService.STAIGHT_LINE);
			
			return item;
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
			
			if (punishment.isActive() && canRemove) {
				new AnvilInputMenu(clicker.getUniqueId(), "lol", "lol", "Entrez une raison.") {
					
					@Override
					public void onEvent(AnvilClickEvent event) {
						if (event.getSlot() == AnvilSlot.OUTPUT) {
							punishment.setRemover(clicker.getUniqueId());
							punishment.setRemoveReason(Methods.colors(event.getInput()));
							punishment.execute();
							clicker.sendMessage("§2Vous §favez annulé la punition §e#" + punishment.getID() + " §fde §2" + Methods.getName(offline, true) + "§7.");
							event.setWillClose(true);
							event.setWillDestroy(true);
						}
					}
				}.open(APIPlayer.getPlayer(clicker));
			}
		}
		
	}

}
