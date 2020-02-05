package net.onima.onimaapi.mod.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.mod.ModItem;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.BetterItem;

public class StaffCounter extends ModItem {
	
	private static String name;
	
	static {
		name = MOD_PREFIX + " §cStaff §fen ligne §8(§c§l%staff%§8)";
	}

	public StaffCounter() {
		super("staff_counter", 6, new BetterItem(Material.PAPER, 1, 0, name, Arrays.asList("§7Cliquez pour ouvrir un inventaire avec tous les staff en ligne.")));
	}

	@Override
	public void rightClick(APIPlayer player) {
		PacketMenu menu = PacketMenu.getMenu("online_staff");
		
		menu.updateItems(player.toPlayer());
		menu.open(player);
		useSucces.play(player);
	}

	@Override
	public void leftClick(APIPlayer player) {
		rightClick(player);
	}
	
	@Override
	public void update(APIPlayer... players) {
		List<APIPlayer> onlineStaff = new ArrayList<>();
		
		for (APIPlayer online : APIPlayer.getOnlineAPIPlayers()) {
			if (OnimaPerm.STAFF_COUNTER_COUNT.has(online.toPlayer()))
				onlineStaff.add(online);
		}
		
		for (APIPlayer player : players) {
			if (!player.isInModMode()) continue;
			
			PlayerInventory inventory = player.toPlayer().getInventory();
			ItemStack item = inventory.getItem(slot);
			ItemMeta meta = item.getItemMeta();
			
			meta.setDisplayName(name.replace("%staff%", String.valueOf(onlineStaff.size())));

			item.setAmount(onlineStaff.size());
			item.setItemMeta(meta);
			inventory.setItem(slot, item);
		}
	}
	
}
