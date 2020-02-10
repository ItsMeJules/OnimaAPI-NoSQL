package net.onima.onimaapi.gui.menu.report;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.DisplayButton;
import net.onima.onimaapi.gui.buttons.ReportButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.buttons.utils.DroppableButton;
import net.onima.onimaapi.gui.sign.InputSign;
import net.onima.onimaapi.players.APIPlayer;
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

	public CommentsMenu(Report report, APIPlayer opener) {
		super("comments_menu", "§6Commentaires §cReport #" + report.getId(), MAX_SIZE, false);
		
		this.report = report;
	}

	@Override
	public void registerItems() {
		buttons.put(0, new ReportButton(report, false));
		buttons.put(4, new DisplayButton(new BetterItem(Material.WRITTEN_BOOK, report.getComments().size() > 64 ? 64 : report.getComments().size(), 0, "§eCommentaires sur ce report.")));
		buttons.put(8, new WriteCommentButton());
		
		for (int i = 9; i < 18; i++)
			buttons.put(i, splitter);
		
		int i = 18;
		for (ReportComment comment : report.getComments()) {
			if (i >= size)
				continue;
			
			buttons.put(i, new CommentButton(comment));
			i++;
		}
		
//		buttons.put(size - 1, null);
	}
	
	private class WriteCommentButton implements Button {

		@Override
		public BetterItem getButtonItem(Player player) {
			return new BetterItem(Material.BOOK_AND_QUILL, 1, 0, "§eEcrire un commentaire.",
					"§6Clic gauche §7pour rédiger un commentaire \n" + CommentStatus.PRIVATE.getTitle().toLowerCase() + " §7sur ce report.",
					"§6Clic autre que gauche §7pour rédiger un \n§7commentaire §aà envoyer §7sur ce report.");
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			APIPlayer apiPlayer = APIPlayer.getPlayer(clicker);
			
			menu.close(apiPlayer, true);
			
			new InputSign(clicker).open((player, lines) -> {
				ReportComment comment = new ReportComment(report, apiPlayer.getColoredName(true));
				
				for (String line : lines)
					comment.addToComment(line);
				
				comment.setStatus(event.isLeftClick() ? CommentStatus.PRIVATE : CommentStatus.SENT);
				comment.save();
				
				if (comment.getStatus() == CommentStatus.SENT)
					comment.sendToReporter();
				
				apiPlayer.openMenu(CommentsMenu.this);
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
			List<String> lore = Arrays.asList("",
					"§7Status : " + comment.getStatus().getTitle(),
					"§7Auteur : " + comment.getAuthor(),
					"§7Date : §e" + Methods.toFormatDate(comment.getTime(), ConfigurationService.DATE_FORMAT_HOURS),
					"§7Message : " + format(comment.getComment()),
					"");
			
			if (Methods.getRealName((OfflinePlayer) player).equalsIgnoreCase(ChatColor.stripColor(comment.getAuthor()))) {
				lore.add("§6Clic gauche §7pour éditer le commentaire.");
				isCreator = true;
			}
			
			if (comment.getStatus() == CommentStatus.PRIVATE)
				lore.add("§6Clic droit §7pour envoyer au joueur qui a report.");

			lore.add("§6Droppez l'item §7pour supprimer le commentaire.");
			return new BetterItem(Material.PAPER, 1, 0, "§eCommentaire §7#" + comment.getId(), lore);
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			if (event.isLeftClick() && isCreator) {
				new InputSign(clicker).open((player, lines) -> {
					String oldComment = comment.getComment();
					
					for (String line : lines)
						comment.addToComment(line);

					if (!oldComment.equalsIgnoreCase(comment.getComment()) && comment.getStatus() != CommentStatus.PRIVATE) {
						comment.setStatus(CommentStatus.SENT);
						comment.sendToReporter();
						APIPlayer.getPlayer(clicker).openMenu(CommentsMenu.this);
					}
				});
			} else if (event.isRightClick() && comment.getStatus() == CommentStatus.PRIVATE) {
				comment.setStatus(CommentStatus.SENT);
				comment.sendToReporter();
				menu.updateItems(clicker);
			}
		}

		@Override
		public void drop(PacketMenu menu, Player clicker, ItemStack current, PlayerDropItemEvent event) {
			comment.remove();
			menu.updateItems(clicker);
		}
		
	}
	
	private String format(String text) { //A améliorer
		StringBuilder sentence = new StringBuilder();
		int maxLength = 24;
		int actualLength = 0;
		String lastColors = "";
		
		for (String word : text.split(" ")) {
			int wordLength = ChatColor.stripColor(StringUtils.replace(word, "\n", "")).length();
			String lastSearch = ChatColor.getLastColors(word);
			
			lastColors = lastSearch.isEmpty() ? lastColors : lastSearch;
			
			if (wordLength >= maxLength) {
				sentence.append(word.substring(0, wordLength / 2))
				.append('\n').append(lastColors).append(word.substring(wordLength / 2, wordLength))
				.append(' ');
				
				actualLength = 0;
				continue;
			} else if (actualLength >= maxLength) {
				sentence.append('\n').append(lastColors).append(word);
				actualLength = 0;
			} else
				sentence.append(word);
			
			sentence.append(' ');
			actualLength += wordLength;
		}
		
		return sentence.toString();
	}
	
}
