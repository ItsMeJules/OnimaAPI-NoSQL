package net.onima.onimaapi.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.disguise.DisguiseManager;
import net.onima.onimaapi.disguise.DisguiseSkin;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.buttons.utils.HeadButton;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.rank.RankType;
import net.onima.onimaapi.utils.BetterItem;

public class DisguiseCommand implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_DISGUISE_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}
		
		APIPlayer apiPlayer = APIPlayer.getPlayer((Player) sender);
		
		if (args.length == 0) {
			apiPlayer.getMenu("disguise_menu").open(apiPlayer);
			return true;
		} else if (args.length > 0 && args[0].equalsIgnoreCase("list") && OnimaPerm.ONIMAAPI_DISGUISE_COMMAND_LIST.has(sender)) {
			new DisguiseManagerMenu().open(apiPlayer);
			return true;
		}
		
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_DISGUISE_COMMAND_LIST.has(sender) || args.length != 1)
			return Collections.emptyList();
		
		return Arrays.asList("list");
	}
	
	private class DisguiseManagerMenu extends PacketMenu {

		public DisguiseManagerMenu() {
			super("disguse_manager", "§6Gérez les disguises.", MAX_SIZE, false);
		}
		
		@Override
		public void registerItems() {
			for (String disguisedName : DisguiseManager.getDisguisedPlayers().values())
				buttons.put(buttons.size(), new DisguiseItem(APIPlayer.getPlayer(disguisedName)));
		}

		private class DisguiseItem extends HeadButton {

			public DisguiseItem(APIPlayer owner) {
				super(owner);
				
				lore();
			}
			
			@Override
			protected void lore() {
				APIPlayer apiPlayer = (APIPlayer) owner;
				
				lore.add(apiPlayer.getName() + " §7est déguisé en : " + apiPlayer.getDisplayName());
				lore.add("§7Il a le rank : " + apiPlayer.getDisguiseManager().getRankType().getPrefix());
				lore.add("");
				lore.add("§c§oCliquez droit pour lui enlever son disguise.");
				lore.add("§c§oCliquez gauche pour lui changer son disguise.");
				lore.add("§c§oClick du milieu pour lui changer son rank.");
			}

			@Override
			public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
				event.setCancelled(true);
				
				ClickType click = event.getClick();
				APIPlayer apiPlayer = (APIPlayer) owner;
				
				switch (click) {
				case RIGHT:
					apiPlayer.getDisguiseManager().undisguise();
					apiPlayer.sendMessage("§d " + APIPlayer.getPlayer(clicker).getName() + " §7§lvous a enlevé votre disguise.");
					DisguiseManagerMenu.this.registerItems();
					break;
				case LEFT:
					DisguiseSkin skin = DisguiseSkin.getRandomNotInUse();
					
					if (skin == null) {
						clicker.sendMessage("§cAucun skin de disponible, veuillez réessayer plus tard.");
						return;
					}
					
					apiPlayer.getDisguiseManager().disguise(skin, apiPlayer.getDisguiseManager().getRankType());
					apiPlayer.sendMessage("§d " + APIPlayer.getPlayer(clicker).getName() + " §7§lvous a changé de disguise. Faites /disguise pour plus d'informations.");
					break;
				case MIDDLE:
					new RankChooseManagerMenu(apiPlayer).open(APIPlayer.getPlayer(clicker));
					break;
				default:
					break;
				}
			}
			
		}
		
		private class RankChooseManagerMenu extends PacketMenu {
			
			private APIPlayer apiPlayer;
			
			public RankChooseManagerMenu(APIPlayer apiPlayer) {
				super("rank_choose_manager", "§6Choisissez le rank du joueur.", MIN_SIZE, false);
				
				this.apiPlayer = apiPlayer;
			}

			@Override
			public void registerItems() {
				RankButton defaultRank = new RankButton(RankType.DEFAULT, 0);
				RankButton ninjaRank = new RankButton(RankType.NINJA, 8);
				RankButton roninRank = new RankButton(RankType.RONIN, 9);
				RankButton komonoRank = new RankButton(RankType.KOMONO, 13);
				RankButton kachiRank = new RankButton(RankType.KACHI, 1);
				RankButton shogunRank = new RankButton(RankType.SHOGUN, 10);
				
				buttons.put(0, defaultRank);
				buttons.put(1, ninjaRank);
				buttons.put(2, roninRank);
				buttons.put(3, komonoRank);
				buttons.put(4, kachiRank);
				buttons.put(5, shogunRank);
			}
			
			private class RankButton implements Button {
				
				private RankType rankType;
				private int wool;

				public RankButton(RankType rankType, int wool) {
					this.rankType = rankType;
					this.wool = wool;
				}

				@Override
				public BetterItem getButtonItem(Player player) {
					return new BetterItem(Material.WOOL, 1, wool, rankType.getName(), rankType.getPrefix() + (rankType == RankType.DEFAULT ? "" : ' ') + rankType.getNameColor() + apiPlayer.getDisplayName() + "§f: " + rankType.getSpeakingColor() + "Je suis en /disguise.");
				}

				@Override
				public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
					event.setCancelled(true);
					
					apiPlayer.getDisguiseManager().setRankType(rankType);
					apiPlayer.sendMessage("§d " + apiPlayer.getName() + " §7§lvous a changé de rank de disguise. Vous êtes maintenant " + rankType.getPrefix() + "§7§l.");
					DisguiseManagerMenu.this.open(APIPlayer.getPlayer(clicker));
				}
				
			}
			
		}
		
	}
 
}
