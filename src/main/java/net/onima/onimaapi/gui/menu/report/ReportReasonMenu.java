package net.onima.onimaapi.gui.menu.report;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.DisplayButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.report.PlayerReport;
import net.onima.onimaapi.report.struct.ReportReason;
import net.onima.onimaapi.utils.BetterItem;

public class ReportReasonMenu extends PacketMenu {
	
	private static Button splitter;
	
	static {
		splitter = new DisplayButton(new BetterItem(Material.STAINED_GLASS_PANE, 1, 8, "§6"));
	}
	
	private UUID reporter;
	private UUID reported;
	private String name;

	public ReportReasonMenu(UUID reporter, UUID reported) {
		super("report_reasons_menu", "§6Report §7» §c" + Bukkit.getOfflinePlayer(reported).getName(), MAX_SIZE, false);
		
		this.reporter = reporter;
		this.reported = reported;
		
		name = Bukkit.getOfflinePlayer(reported).getName();
	}

	@Override
	public void registerItems() {
		buttons.put(4, new DisplayButton(new BetterItem(Material.BOOK, 1, 0, "§eMotifs de report")));
		
		for (int i = 9; i < 18; i++)
			buttons.put(i, splitter);
		
		for (ReportReason reason : ReportReason.values())
			buttons.put(buttons.size() + 18, new ReportItem(reason));
	}
	
	private class ReportItem implements Button {
		
		private ReportReason reason;

		public ReportItem(ReportReason reason) {
			this.reason = reason;
		}

		@Override
		public BetterItem getButtonItem(Player player) {
			return new BetterItem(reason.getMaterial(), 1, reason.getDamage(), "§7Report pour : §e" + reason.getNiceName(), "§6Cliquez §7pour report §c" + name, "§7avec la raison : §e" + reason.getNiceName());
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
			new PlayerReport(reporter, reason.name(), reported).execute();
			menu.close(APIPlayer.getPlayer(clicker), true);
		}
		
	}

}
