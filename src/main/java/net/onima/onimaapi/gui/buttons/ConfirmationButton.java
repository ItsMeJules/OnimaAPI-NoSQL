package net.onima.onimaapi.gui.buttons;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.OSound;
import net.onima.onimaapi.utils.callbacks.APICallback;

public class ConfirmationButton implements Button {

	private boolean confirm;
	private boolean closeOnResponse;
	private APICallback<Boolean> callback;
	
	public ConfirmationButton(boolean confirm, boolean closeOnResponse, APICallback<Boolean> callback) {
		this.confirm = confirm;
		this.closeOnResponse = closeOnResponse;
		this.callback = callback;
	}
	
	@Override
	public BetterItem getButtonItem(Player player) {
		return new BetterItem(Material.STAINED_GLASS_PANE, 1, confirm ? 5 : 14, confirm ? "§aConfirmer" : "§cAnnuler");
	}

	@Override
	public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
		event.setCancelled(true);
		
		new OSound(confirm ? Sound.NOTE_PIANO : Sound.DIG_GRAVEL, 0.1F, 20F).play(clicker);
		
		if (closeOnResponse)
			menu.close(APIPlayer.getPlayer(clicker), true);
		
		if (callback != null)
			callback.call(confirm);
	}
	
}