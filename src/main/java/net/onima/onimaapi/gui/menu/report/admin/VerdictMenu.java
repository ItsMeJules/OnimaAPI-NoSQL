package net.onima.onimaapi.gui.menu.report.admin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.BackButton;
import net.onima.onimaapi.gui.buttons.DisplayButton;
import net.onima.onimaapi.gui.buttons.ReportButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.menu.report.RewardMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.report.Report;
import net.onima.onimaapi.report.struct.ReportStat;
import net.onima.onimaapi.report.struct.Verdict;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.Methods;

public class VerdictMenu extends PacketMenu {
	
	private static Button splitter;
	
	static {
		splitter = new DisplayButton(new BetterItem(Material.STAINED_GLASS_PANE, 1, 8, "§6"));
	}

	private Report report;
	private PacketMenu backMenu;

	public VerdictMenu(Report report, PacketMenu backMenu) {
		super("verdict", "§7Soumettre un §6verdict", MIN_SIZE * 3, false);
		
		this.report = report;
		this.backMenu = backMenu;
	}

	@Override
	public void registerItems() {
		buttons.put(0, new ReportButton(report, false));
		
		for (int position : new Integer[] {1, 2, 3, 4, 5, 6, 7, 10, 16, 19, 20, 21, 22, 23, 24, 25})
			buttons.put(position, splitter);
	
		for (Verdict verdict : Verdict.values()) {
			int pos = 0;
			
			switch (verdict) {
			case FALSE:
				pos = 15;
				break;
			case TRUE:
				pos = 11;
				break;
			case UNCERTAIN:
				pos = 13;
				break;
			default:
				break;
			}
			
			buttons.put(pos, new VerdictButton(verdict));
		}
		
		buttons.put(18, new BackButton(backMenu));
	}
	
	private class VerdictButton implements Button {

		private Verdict verdict;

		public VerdictButton(Verdict verdict) {
			this.verdict = verdict;
		}
		
		@Override
		public BetterItem getButtonItem(Player player) {
			return new BetterItem(verdict.getMaterial(), 1, verdict.getDamage(), verdict.getTitle(), "", "§6Cliquez §7pour soumettre le", "§7verdict : " + verdict.getTitle());
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			APIPlayer apiPlayer = APIPlayer.getPlayer(clicker);
			
			event.setCancelled(true);
			report.process(verdict, Methods.getRealName(apiPlayer.getOfflinePlayer()));
			
			switch (verdict) {
			case FALSE:
				OfflineAPIPlayer.getPlayer(report.getReporter(), offline -> { 
					offline.addStatistic(ReportStat.FALSE_APPRECIATIONS, 1);
					offline.addStatistic(ReportStat.PROCESSED_REPORTS, 1);
				});
				break;
			case TRUE:
				OfflineAPIPlayer.getPlayer(report.getReporter(), offline -> { 
					offline.addStatistic(ReportStat.TRUE_APPRECIATIONS, 1);
					offline.addStatistic(ReportStat.PROCESSED_REPORTS, 1);
				});
				break;
			case UNCERTAIN:
				OfflineAPIPlayer.getPlayer(report.getReporter(), offline -> { 
					offline.addStatistic(ReportStat.UNCERTAIN_APPRECIATIONS, 1);
					offline.addStatistic(ReportStat.PROCESSED_REPORTS, 1);
				});
				break;
			default:
				break;
			}
			
			apiPlayer.openMenu(new RewardMenu(report, true));
		}
		
	}

}
