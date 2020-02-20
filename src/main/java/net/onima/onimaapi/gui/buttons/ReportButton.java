package net.onima.onimaapi.gui.buttons;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.utils.DroppableButton;
import net.onima.onimaapi.gui.buttons.utils.UpdatableButton;
import net.onima.onimaapi.gui.menu.report.admin.ReportInfoMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.report.PlayerReport;
import net.onima.onimaapi.report.Report;
import net.onima.onimaapi.utils.BetterItem;

public class ReportButton implements DroppableButton, UpdatableButton {
	
	private Report report;
	private boolean performActions;

	public ReportButton(Report report, boolean performActions) {
		this.report = report;
		this.performActions = performActions;
	}

	@Override
	public BetterItem getButtonItem(Player player) {
		if (performActions)
			return report.getItem().addLore("").addLore("§6Cliquez §7pour afficher les détails.").addLore("§6Droppez l'item §7pour le supprimer.");
		else
			return report.getItem();
	}

	@Override
	public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
		event.setCancelled(true);
		
		if (!performActions)
			return;
		
		new ReportInfoMenu(report).open(APIPlayer.getPlayer(clicker));
	}

	@Override
	public void drop(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
		event.setCancelled(true);
		
		if (!performActions)
			return;
		
		report.remove();
		
		Bukkit.getScheduler().runTask(OnimaAPI.getInstance(), () -> menu.updateItems(clicker));
		OfflineAPIPlayer.getPlayer(report.getReporter(), offline -> offline.getReports().remove(report));
		
		if (report instanceof PlayerReport)
			OfflineAPIPlayer.getPlayer(((PlayerReport) report).getReported(), offline -> offline.getReports().remove(report));
	}
	
	@Override
	public void update(PacketMenu menu) {
		menu.updateItems(null);
	}

}
