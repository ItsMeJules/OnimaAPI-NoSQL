package net.onima.onimaapi.gui.buttons;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import net.md_5.bungee.api.chat.ClickEvent;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.report.BugReport;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.CasualFormatDate;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;

public class BugReportInfoButton implements Button {

	private BugReport report;
	private int i;
	private String name;
	private List<String> lore;
	private Material material;
	
	{
		lore = Lists.newArrayList("");
	}

	public BugReportInfoButton(BugReport report, int i) {
		this.report = report;
		this.i = i;
		
		switch (i) {
		case 0:
			name = "§eCe que le joueur faisait";
			lore.addAll(Lists.newArrayList(report.getPlayerActionsDescription().split("(?<=\\G.{34})")));
			material = Material.WATER_LILY;
			break;
		case 1:
			name = "§eDate du bug";
			lore.add("§6" + Methods.toFormatDate(report.getTimeWhenBugOccured(), ConfigurationService.DATE_FORMAT_HOURS));
			lore.add("§6" + new CasualFormatDate("d u z hi").toNormalDate(report.getTimeWhenBugOccured()));
			material = Material.WATCH;
			break;
		case 2:
			name = "§eLien vers une vidéo/image";
			lore.addAll(Lists.newArrayList(report.getLinkToProof().split("(?<=\\G.{34})")));
			lore.add("");
			lore.add("§6Clic §7pour afficher le lien dans le chat.");
			material = Material.ITEM_FRAME;
			break;
		default:
			break;
		}
	}

	@Override
	public BetterItem getButtonItem(Player player) {
		return new BetterItem(material, 1, 0, name, lore);
	}

	@Override
	public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
		event.setCancelled(true);

		if (i == 2) {
			new JSONMessage(report.getLinkToProof(), "§eCliquez pour ouvrir le lien.", true, report.getLinkToProof(), ClickEvent.Action.OPEN_URL).send(clicker);
			menu.close(APIPlayer.getPlayer(clicker), true);
		}
	}
	
}