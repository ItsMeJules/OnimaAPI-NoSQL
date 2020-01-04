package net.onima.onimaapi.gui.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.utils.HeadButton;
import net.onima.onimaapi.gui.buttons.utils.UpdatableButton;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.utils.time.Time.LongTime;

public class TeleportHeadButton extends HeadButton implements UpdatableButton {

	public TeleportHeadButton(APIPlayer owner) {
		super(owner);
	}

	@Override
	protected void lore() {
		lore.add("§7Temps de jeu : §a" + LongTime.setYMDWHMSFormat(owner.getPlayTime().getPlayTime()));
		lore.add("§7En ligne depuis : §a" + LongTime.setYMDWHMSFormat(System.currentTimeMillis() - ((APIPlayer) owner).toPlayer().getLastLogin()));
		lore.add(" ");
		lore.add("§aCliquez pour vous téléporter.");
	}

	@Override
	public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
		event.setCancelled(true);
		
		if (!owner.isOnline()) {
			update(menu);
			clicker.sendMessage("§c" + owner.getName() + " n'est plus en ligne !");
			return;
		}
		
		clicker.teleport(((APIPlayer) owner).toPlayer());
		clicker.sendMessage("§cVous §favez été téléporté à §c" + owner.getOfflinePlayer().getName() + "§f.");
	}

}
