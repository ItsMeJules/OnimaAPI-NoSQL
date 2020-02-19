package net.onima.onimaapi.gui.menu.report;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.BackButton;
import net.onima.onimaapi.gui.buttons.DisplayButton;
import net.onima.onimaapi.gui.buttons.TakeableButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.menu.report.admin.ReportsMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.report.Report;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.Methods;

public class RewardMenu extends PacketMenu {

	private Report report;
	private boolean admin;

	public RewardMenu(Report report, boolean admin) {
		super("report_reward", admin ? "§dRécompense pour le joueur" : "§dVos récompenses", MIN_SIZE, false);
	
		this.report = report;
		this.admin = admin;
	}

	@Override
	public void registerItems() {
		if (!report.getRewards().isEmpty()) {
			for (ItemStack reward : report.getRewards())
				buttons.put(buttons.size(), admin ? new TakeableButton(reward) : new DisplayButton(reward));
		}
		
		if (admin)
			buttons.put(8, new SetRewardsButton());
		else
			buttons.put(8, new BackButton(new MyReportsMenu(APIPlayer.getPlayer(report.getReporter()))));
	}
	
	private class SetRewardsButton implements Button {

		@Override
		public BetterItem getButtonItem(Player player) {
			return new BetterItem(Material.NETHER_STAR, 1, 0, "§eValider ces items comme récompense", "", "§7§oSi vous ne mettez aucun item", "§7§ole joueur ne recevra rien.", "§7§oSi vous souhaitez donc ne rien lui", "§7§olaissez cet inventaire vide.");
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
			report.getRewards().clear();
			
			for (int i = 0; i < MIN_SIZE - 1; i++) {
				ItemStack item = menu.getInventory().getItem(i);
				
				if (item == null)
					continue;
				
				report.getRewards().add(item);
			}

			if (!report.getRewards().isEmpty())
				report.setRewardedBy(Methods.getRealName(APIPlayer.getPlayer(clicker).getOfflinePlayer()));
				
			if (admin)
				APIPlayer.getPlayer(clicker).openMenu(new ReportsMenu(false));
		}
		
	}

}
