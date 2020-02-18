package net.onima.onimaapi.gui.menu.report;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.menu.report.admin.ReportsMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.report.Report;
import net.onima.onimaapi.utils.BetterItem;

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
		if (admin)
			buttons.put(8, new SetRewardsButton());
	}
	
	private class SetRewardsButton implements Button {

		@Override
		public BetterItem getButtonItem(Player player) {
			return new BetterItem(Material.NETHER_STAR, 1, 0, "§eValider ces items comme récompense", "", "§7§oSi vous ne mettez aucun item", "§7§ole joueur ne recevra rien.", "§7§oSi vous souhaitez donc ne rien lui", "§7§olaissez cet inventaire vide.");
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
			
			for (int i = 0; i < MAX_SIZE - 1; i++)
				report.getRewards().add(menu.getInventory().getItem(i));
			
			if (admin)
				APIPlayer.getPlayer(clicker).openMenu(new ReportsMenu(false));
		}
		
	}

}
