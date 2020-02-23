package net.onima.onimaapi.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.crates.openers.VirtualKey;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.PacketMenuType;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.menu.AnvilInputMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.Methods;

public class VirtualKeysCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent exécuter cette commande.");
			return false;
		}
		
		APIPlayer apiPlayer = APIPlayer.getPlayer((Player) sender);
		
		if (!OnimaPerm.VIRTUAL_KEYS_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (args.length >= 2 && args[0].equalsIgnoreCase("asadmin") && OnimaPerm.VIRTUAL_KEYS_COMMAND_AS_ADMIN.has(sender)) {
			UUID uuid = UUIDCache.getUUID(args[1]);
			
			if (uuid == null) {
				sender.sendMessage("§c" + args[1] + " ne s'est jamais connecté sur le serveur !");
				return false;
			}
			
			OfflineAPIPlayer.getPlayer(uuid, offline -> {
				new VirtualKeysAdminMenu(offline).open(apiPlayer);
				apiPlayer.sendMessage("§dVous §7avez ouvert le menu de §dclefs virtuelles §7admin.");
			});
			
			return true;
		}
		
		apiPlayer.getMenu("virtual_keys").open(apiPlayer);
		apiPlayer.sendMessage("§dVous §7avez ouvert votre menu de §dclefs virtuelles§7.");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			if (OnimaPerm.VIRTUAL_KEYS_COMMAND_AS_ADMIN.has(sender) && sender instanceof Player && StringUtil.startsWithIgnoreCase("asadmin", args[0]))
				return Arrays.asList("asadmin");	
		} else if (args.length == 2 && OnimaPerm.VIRTUAL_KEYS_COMMAND_AS_ADMIN.has(sender))
			return OfflineAPIPlayer.getDisconnectedOfflineAPIPlayers().parallelStream().map(offline -> {return Methods.getName(offline);}).filter(name -> StringUtil.startsWithIgnoreCase(args[1], name)).collect(Collectors.toList());
		

		return Collections.emptyList();
	}	
	
	public class VirtualKeysAdminMenu extends PacketMenu {

		private OfflineAPIPlayer offline;

		public VirtualKeysAdminMenu(OfflineAPIPlayer offline) {
			super("virtual_keys_admin", "§7§oClefs §7§ode " + offline.getName(), PacketMenuType.HOPPER, false);
			
			this.offline = offline;
		}

		@Override
		public void registerItems() {
			for (VirtualKey key : offline.getVirtualKeys().values()) 
				buttons.put(buttons.size(), new VirtualKeyAdminButton(key, key.getAmount()));
		}
		
		public class VirtualKeyAdminButton implements Button {

			private VirtualKey key;
			private int amount;
			
			public VirtualKeyAdminButton(VirtualKey key, int amount) {
				this.key = key;
				this.amount = amount;
			}

			@Override
			public BetterItem getButtonItem(Player player) {
				List<String> lore = Arrays.asList("§5Crate : §r" + key.getCrateName(), "§c§l(§7§l!§c§l) §r§oClick gauche pour supprimer une clef.", "§c§l(§7§l!§c§l) §r§oClick du milieu pour tout supprimer.", "§c§l(§7§l!§c§l) §r§oClick droit pour la donner à quelqu'un.");
				
				if (amount <= 64)
					return new BetterItem(Material.NETHER_STAR, amount, 0, "§7§lClef virtuelle", lore);
				else {
					lore.add("§7§oIl y a " + amount + " clefs de ce type.");
					return new BetterItem(Material.NETHER_STAR, 64, 0, "§7§lClef virtuelle", lore);
				}
			}

			@Override
			public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
				event.setCancelled(true);
				
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
									VirtualKey key = ((VirtualKeyAdminButton) buttons.get(event.getSlot())).key;
									
									offlineTarget.addVirtualKey(key);
									offline.removeVirtualKey(key);
									clicker.sendMessage("§dVous §7avez envoyé une clef pour la crate " + key.getCrateName() + "§r à §d" + Methods.getName(offline, true) + "§7.");
									
									if (offlineTarget.isOnline())
										((APIPlayer) offlineTarget).sendMessage("§d" + Methods.getRealName((OfflinePlayer) clicker) + " §7vous a envoyé une clef pour la crate " + key.getCrateName() + "§7 qui appartenait à §d" + Methods.getName(offline) + "§7.");
									
									if (offline.isOnline())
										((APIPlayer) offline).sendMessage("§d" + Methods.getRealName((OfflinePlayer) clicker) + " §7a envoyé une de vos clef pour la crate " + key.getCrateName() + "§7à §d" + Methods.getName(offlineTarget) + "§7.");
								
									VirtualKeysAdminMenu.this.updateItems(clicker);
								});
							}
						}
						
					}.open(APIPlayer.getPlayer(clicker));
					
				} else if (event.getClick() == ClickType.MIDDLE) {
					offline.getVirtualKeys().remove(((VirtualKeyAdminButton) buttons.get(event.getSlot())).key.getCrateName());
					updateItems(clicker);
					clicker.sendMessage("§dVous §7avez supprimé toutes les clefs pour la crate " + key.getCrateName() + "§7.");
				} else if (event.getClick() == ClickType.LEFT) {
					VirtualKey key = ((VirtualKeyAdminButton) buttons.get(event.getSlot())).key;
					
					offline.removeVirtualKey(key);
					clicker.sendMessage("§dVous §7avez supprimé une clef pour la crate " + key.getCrateName() + "§r à §d" + Methods.getName(offline, true) + "§7.");
					
					if (offline.isOnline())
						((APIPlayer) offline).sendMessage("§d" + Methods.getRealName((OfflinePlayer) clicker) + " §7vous a supprimé une clef pour la crate " + key.getCrateName() + "§7.");
				
					updateItems(clicker);
				}
			}
			
		}
		
	}
	
}
