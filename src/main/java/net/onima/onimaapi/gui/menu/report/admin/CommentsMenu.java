package net.onima.onimaapi.gui.menu.report.admin;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.BackButton;
import net.onima.onimaapi.gui.buttons.DisplayButton;
import net.onima.onimaapi.gui.buttons.ReportButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.buttons.utils.DroppableButton;
import net.onima.onimaapi.gui.menu.report.MyReportsMenu.ReportNoDeleteButton;
import net.onima.onimaapi.gui.sign.InputSign;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.report.Report;
import net.onima.onimaapi.report.ReportComment;
import net.onima.onimaapi.report.struct.CommentStatus;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;

public class CommentsMenu extends PacketMenu {
	
	private static Button splitter;
	
	static {
		splitter = new DisplayButton(new BetterItem(Material.STAINED_GLASS_PANE, 1, 8, "§6"));
	}

	private Report report;
	private PacketMenu menu;
	private boolean isStaff;

	public CommentsMenu(Report report, APIPlayer opener, PacketMenu menu) {
		super("comments_menu", "§6Commentaires §cReport #" + report.getId(), MAX_SIZE, false);
		
		this.report = report;
		this.menu = menu;
		isStaff = opener.getRank().getRankType().hasPermisssion(OnimaPerm.REPORTS_COMMAND);
	}

	@Override
	public void registerItems() {
		buttons.put(0, isStaff ? new ReportButton(report, false) : new ReportNoDeleteButton(report));
		buttons.put(4, new DisplayButton(new BetterItem(Material.WRITTEN_BOOK, report.getComments().size() > 64 ? 64 : report.getComments().size(), 0, "§eCommentaires sur ce report.")));
		
		if (isStaff)
			buttons.put(8, new WriteCommentButton());
		
		for (int i = 9; i < 18; i++)
			buttons.put(i, splitter);
		
		int i = 18;
		for (ReportComment comment : report.getComments()) {
			if (i + 1 >= size)
				continue;
			
			buttons.put(i, new CommentButton(comment));
			i++;
		}
		
		buttons.put(size - 1, new BackButton(menu));
	}
	
	private class WriteCommentButton implements Button {

		@Override
		public BetterItem getButtonItem(Player player) {
			return new BetterItem(Material.BOOK_AND_QUILL, 1, 0, "§eEcrire un commentaire.",
					"§6Clic gauche §7pour rédiger un commentaire", CommentStatus.PRIVATE.getTitle().toLowerCase() + " §7sur ce report.",
					"§6Clic autre que gauche §7pour rédiger un", "§7commentaire §aà envoyer §7sur ce report.");
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
			APIPlayer apiPlayer = APIPlayer.getPlayer(clicker);
			
			menu.close(apiPlayer, true);
			
			new InputSign(clicker).open((player, lines) -> {
				ReportComment comment = new ReportComment(report, apiPlayer.getColoredName(true));
				
				for (String line : lines) {
					if (!line.isEmpty())
						comment.addToComment(line);
				}
				
				comment.setStatus(event.isLeftClick() ? CommentStatus.PRIVATE : CommentStatus.SENT);
				comment.save();
				
				if (comment.getStatus() == CommentStatus.SENT)
					comment.sendToReporter();
				
				Bukkit.getScheduler().runTask(OnimaAPI.getInstance(), () -> apiPlayer.openMenu(CommentsMenu.this));
			});
		}
		
	}
	
	private class CommentButton implements DroppableButton {
		
		private ReportComment comment;
		private boolean isCreator;

		public CommentButton(ReportComment comment) {
			this.comment = comment;
		}

		@Override
		public BetterItem getButtonItem(Player player) {
			List<String> list = comment.getComment();
			List<String> lore = Lists.newArrayList("",
					"§7Status : " + comment.getStatus().getTitle(),
					"§7Auteur : " + comment.getAuthor(),
					"§7Date : §e" + Methods.toFormatDate(comment.getTime(), ConfigurationService.DATE_FORMAT_HOURS),
					"§7Message : " + list.get(0));
			
			for (int i = 1; i < list.size(); i++)
				lore.add(list.get(i));
			
			if (isStaff) {
				if (Methods.getRealName((OfflinePlayer) player).equalsIgnoreCase(ChatColor.stripColor(comment.getAuthor()))) {
					lore.add("§6Clic gauche §7pour éditer le commentaire.");
					isCreator = true;
				}
				
				if (comment.getStatus() == CommentStatus.PRIVATE)
					lore.add("§6Clic droit §7pour envoyer au joueur qui a report.");

				lore.add("§6Droppez l'item §7pour supprimer le commentaire.");
			}
			
			return new BetterItem(Material.PAPER, 1, 0, "§eCommentaire §7#" + comment.getId(), lore);
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
			
			if (!isStaff)
				return;
			
			if (event.isLeftClick() && isCreator) {
				new InputSign(clicker).open((player, lines) -> {
					List<String> oldComment = comment.getComment();
					
					for (String line : lines) {
						if (!line.isEmpty())
							comment.addToComment(line);
					}

					if (!oldComment.equals(comment.getComment()) && comment.getStatus() != CommentStatus.PRIVATE) {
						comment.setStatus(CommentStatus.SENT);
						comment.sendToReporter();
						Bukkit.getScheduler().runTask(OnimaAPI.getInstance(), () -> APIPlayer.getPlayer(clicker).openMenu(CommentsMenu.this));
					}
				});
			} else if (event.isRightClick() && comment.getStatus() == CommentStatus.PRIVATE) {
				comment.setStatus(CommentStatus.SENT);
				comment.sendToReporter();
				menu.updateItems(clicker);
			}
		}

		@Override
		public void drop(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
			
			if (!isStaff)
				return;
			
			comment.remove();
			menu.updateItems(clicker);
		}
		
	}
	
}
