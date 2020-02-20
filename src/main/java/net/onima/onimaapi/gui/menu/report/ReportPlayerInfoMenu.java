package net.onima.onimaapi.gui.menu.report;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.BackButton;
import net.onima.onimaapi.gui.buttons.BugReportInfoButton;
import net.onima.onimaapi.gui.buttons.CommentsMenuButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.buttons.utils.HeadButton;
import net.onima.onimaapi.gui.menu.report.MyReportsMenu.ReportNoDeleteButton;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.report.BugReport;
import net.onima.onimaapi.report.PlayerReport;
import net.onima.onimaapi.report.Report;
import net.onima.onimaapi.utils.BetterItem;

public class ReportPlayerInfoMenu extends PacketMenu {
	
	private Report report;
	private boolean isBugReport;

	public ReportPlayerInfoMenu(Report report) {
		super("report_info", (report instanceof BugReport ? "§aBug Report §7#" : "§7Report §c#") + report.getId(), MIN_SIZE * 3, false);
		
		this.report = report;
		
		isBugReport = report instanceof BugReport;
	}

	@Override
	public void registerItems() {
		buttons.put(0, new ReportNoDeleteButton(report));
		buttons.put(4, new RewardMenuButton());
		
		if (!isBugReport)
			OfflineAPIPlayer.getPlayer(((PlayerReport) report).getReported(), offline -> buttons.put(5, new ReportedButton(offline)));
		else {
			BugReport br = (BugReport) report;
			
			buttons.put(20, new BugReportInfoButton(br, 0));
			buttons.put(5, new BugReportInfoButton(br, 1));
		
			if (br.getLinkToProof() != null)
				buttons.put(24, new BugReportInfoButton(br, 2));
		}
		
		buttons.put(8, new BackButton(new MyReportsMenu(APIPlayer.getPlayer(report.getReporter()))));
		buttons.put(26, new CommentsMenuButton(report, this, false));
	}
	
	private class ReportedButton extends HeadButton {
		
		public ReportedButton(OfflineAPIPlayer owner) {
			super(owner);
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
		}

		@Override
		protected void lore() {}
		
	}
	
	private class RewardMenuButton implements Button {
		
		private boolean leftClick, rightClick;

		@Override
		public BetterItem getButtonItem(Player player) {
			BetterItem item = new BetterItem(Material.GOLD_INGOT, 1, 0, "§5Récompenses");
			
			if (!report.getRewards().isEmpty()) {
				leftClick = true;
				
				if (!report.hasReceivedRewards()) {
					item.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					item.addLore("").addLore("§eVous avez des récompenses à récupérer.").addLore("")
					.addLore("§6Clic gauche §7pour les visualiser.")
					.addLore("§6Clic droit §7pour les récupérer.");
					
					rightClick = true;
				} else
					item.addLore("").addLore("§6Clic gauche §7pour les visualiser.");
			}
			
			return item;
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
			
			if (leftClick && event.isLeftClick())
				new RewardMenu(report, false).open(APIPlayer.getPlayer(clicker));
			else if (rightClick && event.isRightClick()) {
				report.giftRewards(APIPlayer.getPlayer(clicker));
				menu.updateLocalized(clicker, event.getSlot());
			}
		}
		
	}

}
