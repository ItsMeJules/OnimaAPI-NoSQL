package net.onima.onimaapi.crates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.crates.booster.KeyBooster;
import net.onima.onimaapi.crates.prizes.Prize;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.crates.utils.CrateType;
import net.onima.onimaapi.event.crates.CrateOpenEvent;
import net.onima.onimaapi.event.crates.CrateWinEvent;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.CallbackButton;
import net.onima.onimaapi.gui.buttons.DisplayButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.menu.CrateOpeningMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.OSound;

public class VirtualCrate extends Crate {
	
	public static final OSound TICK_SOUND;

	static {
		TICK_SOUND = new OSound(Sound.NOTE_PIANO, 0.5F, 1F);
	}
	
	public VirtualCrate(String name, int prizeAmount) {
		super(name, CrateType.VIRTUAL, prizeAmount);
	}
	
	@Override
	public void open(APIPlayer player, KeyBooster booster) {
		List<Prize> prizes = getPrizes(booster);
		CrateOpenEvent event = new CrateOpenEvent(this, prizes, player.toPlayer(), booster);
		
		Bukkit.getPluginManager().callEvent(event);
		
		if (event.isCancelled())
			return;
		
		OpeningMenu menu = new OpeningMenu(name, prizes);
		
		menu.open(player);
		menu.renderInventory(player, prizes);
		menu.startRoll(player);
		
		super.open(player, booster);
	}

	@Override
	public void close(APIPlayer player) {
		if (!player.getVirtualKeys().isEmpty())
			Bukkit.getScheduler().runTask(OnimaAPI.getInstance(), () -> player.getMenu("virtual_keys").open(player));
		else
			player.closeMenu();
		
		super.close(player);
	}

	@Override
	public void cancel(APIPlayer player) {
		PacketMenu menu = player.getViewingMenu();
		
		if (menu instanceof OpeningMenu) {
			OpeningMenu openingMenu = (OpeningMenu) menu;
			
			openingMenu.getTask().cancel();
			openingMenu.close(player, true);
		}
		
		super.close(player);
	}
	
	@Override
	public void sendShow(CommandSender sender) {
		sender.sendMessage(ConfigurationService.STAIGHT_LINE);
		sender.sendMessage("§7Crate : §d§o" + type.name() + " §f- " + (displayName == null ? name : displayName));
		sender.sendMessage("§7Nombre total de prix : §d" + prizes.size());
		
		if (OnimaPerm.ONIMAAPI_CRATE_INFO_ARGUMENT.has(sender)) {
			sender.sendMessage("§7Nombre de prix à drop : §d" + prizeAmount);
			sender.sendMessage("§7Pourcentage cumulé des prix : §d" + getTotalWeight() + '%');
		}
		
		sender.sendMessage(ConfigurationService.STAIGHT_LINE);
	}
	
	public class OpeningMenu extends CrateOpeningMenu {
		
		private Map<Integer, Prize> pos;
		DisplayButton button;
		
		{
			pos = new HashMap<>();
			button = new DisplayButton(new BetterItem(Material.STAINED_GLASS_PANE, 1, 15, "§r"));
		}

		public OpeningMenu(String crateName, List<Prize> prizes) {
			super(crateName, prizes, MIN_SIZE * 5);
		}
		
		@Override
		public void registerItems() {
			while (buttons.size() != size)
				buttons.put(buttons.size(), new DisplayButton(VirtualCrate.this.prizes.get(OnimaAPI.RANDOM.nextInt(VirtualCrate.this.prizes.size())).getDisplayItem()));
		}

		@Override
		public void closeCrate(APIPlayer player) {
			VirtualCrate.this.close(player);
		}

		@Override
		public void renderInventory(APIPlayer player, List<Prize> prizes) {
			Player online = player.toPlayer();
			
			if (!rolling) {
				registerItems();
				renderPrizes(prizes);
				inventory.clear();
			} else {
			}
			
			for (Entry<Integer, Button> entry : buttons.entrySet())
				inventory.setItem(entry.getKey(), createItemStack(online, entry.getValue()));
		}
		
		@Override
		public void renderPrizes(List<Prize> prizes) {
			if (!rolling) {
				Map<Integer, Prize> pos = new HashMap<Integer, Prize>(prizes.size());
				int index = 0;
				
				while (pos.size() != prizes.size()) {
					int generated = Methods.getRandomWithExclusion(OnimaAPI.RANDOM, 0, MIN_SIZE * 5 - 1, 22);
					
					if (pos.containsKey(generated))
						continue;
					else {
						pos.put(generated, prizes.get(index));
						index++;
					}
				}
				
				for (Entry<Integer, Prize> entry : pos.entrySet())
					buttons.put(entry.getKey(), new DisplayButton(entry.getValue().getDisplayItem()));
				
				this.pos = pos;
			}
		}

		@Override
		public void startRoll(APIPlayer player) {
			rolling = true;
			task = new BukkitRunnable() {
				
				int index = 0;
				
				@Override
				public void run() {
					if (index == 22) {
						Bukkit.getPluginManager().callEvent(new CrateWinEvent(VirtualCrate.this, prizes, player));
						
						CallbackButton<Crate> button = new CallbackButton<Crate>(new BetterItem(Material.RECORD_10, 1, 0, "§e§oRécupérer le loot.", "§7(TIP) §oVous pouvez aussi récupérer le loot en fermant l'inventaire."), VirtualCrate.this);
						
						button.setCallBack(crate -> {
							dropped = true;
							
							Player online = player.toPlayer();
							
							for (Prize prize : prizes)
								prize.give(online);
							
							crate.close(player);
							button.getEvent().setCancelled(true);
							return true;
						});
						
						updateTitle("§bLoot", player, false);
						renderInventory(player, prizes);
						
						buttons.put(22, button);
						inventory.setItem(22, createItemStack(player.toPlayer(), button));
						WIN_SOUND.play(player);
						Bukkit.broadcastMessage("§6" + player.getName() + " a obtenu " + Crate.constructMultiPrize(prizes) + " §6dans la crate " + displayName + '.');

						rolling = false;
						cancel();
						return;
					}
					
					int endIndex = size - index - 1;
					
					TICK_SOUND.play(player);
					
					if (!pos.containsKey(index)) {
						buttons.put(index, button);
						inventory.setItem(index, button.getButtonItem(null).toItemStack());
					}
					
					if (!pos.containsKey(endIndex)) {
						buttons.put(endIndex, button);
						inventory.setItem(endIndex, button.getButtonItem(null).toItemStack());
					}
					
					index++;
				}
			}.runTaskTimer(OnimaAPI.getInstance(), 0L, 10L);
		}

	}

}
