package net.onima.onimaapi.mod.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.mod.ModItem;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.utils.PlayerOption;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.Methods;

public class RandomTeleport extends ModItem {
	
	private static List<String> lore;
	
	static {
		lore = Arrays.asList("§7Click gauche pour se téléporter aléatoirement à un joueur.", "§7Click droit pour se téléporter aléatoirement à un joueur en-dessous de la couche %layer%.");
	}
	
	public RandomTeleport() {
		super("random_tp", 7, new BetterItem(Material.IRON_FENCE, 1, 0, MOD_PREFIX + " §cTéléportation aléatoire §8(§7R&L§8)", lore));
	}

	@Override
	public void rightClick(APIPlayer player) {
		TeleportResult result = randomTeleport(player, player.getOptions().<Integer>get(PlayerOption.ModOptions.TELEPORT_LAYER));
		
		if (result.succed) {
			player.sendMessage(MOD_PREFIX + " §cVous §favez été téléporté aléatoirement à §c" + result.player.getName() + " §fqui est en couche §c" + result.player.toPlayer().getLocation().getBlockY() + "§f.");
			useSucces.play(player);
		} else {
			player.sendMessage(MOD_PREFIX + " §cAucun joueur n'a été trouvé...");
			useFail.play(player);
		}
	}

	@Override
	public void leftClick(APIPlayer player) {
		TeleportResult result = randomTeleport(player, Integer.MAX_VALUE);
		
		if (result.succed) {
			player.sendMessage(MOD_PREFIX + " §cVous §favez été téléporté aléatoirement à §c" + result.player.getName());
			useSucces.play(player);
		} else {
			player.sendMessage(MOD_PREFIX + " §cAucun joueur n'a été trouvé...");
			useFail.play(player);
		}
	}
	
	@Override
	public void update(APIPlayer... players) {
		for (APIPlayer player : players) {
			if (!player.isInModMode()) continue;
		
			PlayerInventory inventory = player.toPlayer().getInventory();
			ItemStack item = inventory.getItem(slot);
			ItemMeta meta = item.getItemMeta();
			
			meta.setLore(Methods.replacePlaceholder(lore, "%layer%", player.getOptions().<Integer>get(PlayerOption.ModOptions.TELEPORT_LAYER)));
			
			item.setItemMeta(meta);
			inventory.setItem(slot, item);
		}
	}
	
	private TeleportResult randomTeleport(APIPlayer player, int layer) {
		List<APIPlayer> toPick = new ArrayList<>(APIPlayer.getOnlineAPIPlayers().size());
		
		for (APIPlayer online : APIPlayer.getOnlineAPIPlayers()) {
			if (!OnimaPerm.MOD_RANDOM_TELEPORT_BYPASS.has(online.toPlayer()) && online.toPlayer().getLocation().getBlockY() < layer)
				toPick.add(online);
		}
		
		if (toPick.isEmpty())
			return new TeleportResult(null);
		
		APIPlayer picked = toPick.get(OnimaAPI.RANDOM.nextInt(toPick.size()));
		
		player.toPlayer().teleport(picked.toPlayer());
		return new TeleportResult(picked);
	}
	
	class TeleportResult {
		
		private APIPlayer player;
		private boolean succed;
		
		public TeleportResult(APIPlayer player) {
			this.player = player;
			succed = player != null;
		}
		
	}

}
