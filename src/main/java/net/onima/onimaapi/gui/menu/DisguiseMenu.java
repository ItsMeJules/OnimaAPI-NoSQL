package net.onima.onimaapi.gui.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.disguise.DisguiseManager;
import net.onima.onimaapi.disguise.DisguiseSkin;
import net.onima.onimaapi.event.disguise.PlayerPreDisguiseEvent;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.PacketMenuType;
import net.onima.onimaapi.gui.buttons.CallbackButton;
import net.onima.onimaapi.gui.buttons.MenuOpenerButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.RankType;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.callbacks.APICallback;

public class DisguiseMenu extends PacketMenu {

	private APIPlayer apiPlayer;

	public DisguiseMenu(APIPlayer apiPlayer) {
		super("disguise_menu", "Disguise", PacketMenuType.HOPPER, false);
	
		this.apiPlayer = apiPlayer;
	}

	@Override
	public void registerItems() {
		BetterItem item = new BetterItem(Material.NAME_TAG, 1, 0, "§2Changer de skin & de nom.");
				
		CallbackButton<Boolean> button = new CallbackButton<>(item, true);
		MenuOpenerButton rankButton = new MenuOpenerButton(new BetterItem(Material.ANVIL, 1, 0, "§2Changez votre rang ici."), new RankChooseMenu());
		
		DisguiseManager disguise = apiPlayer.getDisguiseManager();
		
		if (!disguise.isDisguised())
			item.addLore("§4Vous n'êtes pas déguisé.");
		else {
			
			item.addLore("§7Déguisé : §f" + disguise.getName());
			item.addLore("§7Rank : " + disguise.getRankType().getName());
		}
		
		button.setCallBack(new APICallback<Boolean>() {
			@Override
			public boolean call(Boolean bool) {
				button.getEvent().setCancelled(true);
				
				DisguiseSkin skin = DisguiseSkin.getRandomNotInUse();
				
				PlayerPreDisguiseEvent event = new PlayerPreDisguiseEvent(apiPlayer, skin, disguise);
				Bukkit.getPluginManager().callEvent(event);
				
				if (event.isCancelled())
					return false;
				
				if (skin == null) {
					DisguiseMenu.this.close(apiPlayer, true);
					apiPlayer.sendMessage("§cAucun skin n'est disponible pour le moment. Veuillez réessayer plus tard.");
					return false;
				}
				
				disguise.disguise(skin, disguise.isDisguised() ? disguise.getRankType() : RankType.DEFAULT);
				updateItems(null);
				return true;
			}
		});
		
		if (disguise.isDisguised()) {
			CallbackButton<Boolean> undisguiseButton = new CallbackButton<>(new BetterItem(Material.EYE_OF_ENDER, 1, 0, "§cRevenir à la normale."), true);
			
			undisguiseButton.setCallBack(new APICallback<Boolean>() {
				@Override
				public boolean call(Boolean bool) {
					disguise.undisguise();
					undisguiseButton.getEvent().setCancelled(true);
					updateItems(null);
					return true;
				}
			});
			
			buttons.put(2, undisguiseButton);
		}
		
		buttons.put(0, button);
		buttons.put(4, rankButton);
	}
	
	private class RankChooseMenu extends PacketMenu {

		public RankChooseMenu() {
			super("rank_choose", "§7Choisissez votre rank.", MIN_SIZE, false);
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
				return new BetterItem(Material.WOOL, 1, wool, rankType.getName(), rankType.getPrefix() + (rankType == RankType.DEFAULT ? "" : ' ') + rankType.getNameColor() + apiPlayer.getDisplayName(false) + "§f: " + rankType.getSpeakingColor() + "Je suis en /disguise.");
			}

			@Override
			public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
				event.setCancelled(true);
				apiPlayer.getDisguiseManager().setRankType(rankType);
				clicker.sendMessage("§7Vous avez maintenant le rank : " + rankType.getName());
				DisguiseMenu.this.open(apiPlayer);
			}
			
		}
		
	}
	
}
