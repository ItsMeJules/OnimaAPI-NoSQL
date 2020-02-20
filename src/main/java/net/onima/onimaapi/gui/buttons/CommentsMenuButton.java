package net.onima.onimaapi.gui.buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.menu.report.admin.CommentsMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.report.Report;
import net.onima.onimaapi.utils.BetterItem;

public class CommentsMenuButton implements Button {
	
	private Report report;
	private PacketMenu backMenu;
	private boolean canWrite;
	
	public CommentsMenuButton(Report report, PacketMenu backMenu, boolean canWrite) {
		this.report = report;
		this.backMenu = backMenu;
		this.canWrite = canWrite;
	}

	public CommentsMenuButton(Report report, PacketMenu backMenu) {
		this(report, backMenu, true);
	}

	@Override
	public BetterItem getButtonItem(Player player) {
		int size = report.getComments().size();
		return new BetterItem(Material.CHEST, size > 64 ? 64 : size, 0, "§eCommentaires sur ce report.", "", "§6Clic §7pour afficher les", "§7commentaires.");
	}

	@Override
	public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
		event.setCancelled(true);
		APIPlayer apiPlayer = APIPlayer.getPlayer(clicker);
		
		apiPlayer.openMenu(new CommentsMenu(report, apiPlayer, backMenu, canWrite));
	}
	
}