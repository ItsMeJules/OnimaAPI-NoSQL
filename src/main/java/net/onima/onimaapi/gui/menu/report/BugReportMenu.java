package net.onima.onimaapi.gui.menu.report;

import org.bukkit.Material;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.CallbackButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.report.BugReportTransaction;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.CasualFormatDate;

public class BugReportMenu extends PacketMenu {
	
	private APIPlayer reporter;
	private BugReportTransaction transaction;
	
	{
		transaction = new BugReportTransaction();
	}

	public BugReportMenu(APIPlayer reporter) {
		super("bug_report_menu", "§5Bug report", MIN_SIZE, false);
	
		this.reporter = reporter;
	}

	@Override
	public void registerItems() {
		BetterItem bugItem = new BetterItem(Material.NAME_TAG, 1, 0, "§eDécrivez le bug (§cobligatoire§e).", "", "§6Clic §7pour décrire le bug.");
		BetterItem playerItem = new BetterItem(1, "§eDécrivez ce que vous faisiez (§cobligatoire§e).", Lists.newArrayList("", "§6Clic §7pour décrire ce que vous faisiez"), reporter.getOfflinePlayer());
		BetterItem dateItem = new BetterItem(Material.WATCH, 1, 0, "§eEcrivez la date (§cobligatoire§e).", "", "§6Clic §7pour écrire la date sous le", "§7format suivant : §ejj/mm/yyyy - hh:MM");
		BetterItem linkItem = new BetterItem(Material.CHAINMAIL_CHESTPLATE, 1, 0, "§ePhoto ou vidéo.", "", "§6Clic §7pour ajouter le lien", "§7d'une photo ou d'une vidéo.");
		
		BetterItem finalItem = new BetterItem(Material.STAINED_GLASS, 1, 14);
		CallbackButton<BugReportTransaction> cbck = new CallbackButton<>(finalItem, transaction);
		
		if (transaction.hasBugDescription() && transaction.hasPlayerActionsDescription() && transaction.hasTimeWhenBugOccured()) {
			finalItem.setName("§aValider ce report et l'envoyer.");
			finalItem.setDamage(5);
		} else {
			finalItem.setName("§cReport incomplet !");
			finalItem.addLore("").addLore("§fVeuillez compléter les étapes obligatoires.");
		}
		
		finalItem.addLore("").addLore("§6Vous recevrez une récompense").addLore("§6si votre report a été jugé utile");
		
		
		cbck.setCallBack(val -> {
			cbck.getEvent().setCancelled(true);
			val.submitBug(reporter.getUUID());
			close(reporter, true);
			return true;
		});
		
		if (transaction.hasBugDescription()) {
			bugItem.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			bugItem.addLore("").addLore("§7Description actuelle : ");
			
			for (String desc : transaction.getBugDescription().split("(?<=\\G.{34})"))
				bugItem.addLore("§6" + desc);
		}
		
		if (transaction.hasPlayerActionsDescription()) {
			playerItem.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			playerItem.addLore("").addLore("§7Description actuelle : ");
			
			for (String desc : transaction.getPlayerActionsDescription().split("(?<=\\G.{34})"))
				playerItem.addLore("§6" + desc);
		}
		
		if (transaction.hasTimeWhenBugOccured()) {
			dateItem.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			dateItem.addLore("").addLore("§7Heure du bug : §6" + new CasualFormatDate("d u z hi").toNormalDate(transaction.getTimeWhenBugOccured()));
		}
		
		if (transaction.hasLinkToBugProof()) {
			linkItem.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			linkItem.addLore("").addLore("§7Lien :");
			
			for (String desc : transaction.getLinkToBugProof().split("(?<=\\G.{34})"))
				linkItem.addLore("§6" + desc);
		}
		
		buttons.put(0, new PromptButton(new BugDescriptionPrompt(), bugItem));
		buttons.put(2, new PromptButton(new PlayerDescriptionPrompt(), playerItem));
		buttons.put(4, new PromptButton(new TimePrompt(), dateItem));
		buttons.put(6, new PromptButton(new LinkPrompt(), linkItem));
		buttons.put(8, cbck);
	}
	
	private class PromptButton implements Button {

		private EasyCallPrompt prompt;
		private BetterItem item;

		public PromptButton(EasyCallPrompt prompt, BetterItem item) {
			this.prompt = prompt;
			this.item = item;
		}
		
		@Override
		public BetterItem getButtonItem(Player player) {
			return item;
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
			menu.close(reporter, true);
			prompt.call(clicker);
		}
		
	}
	
	private class LinkPrompt extends StringPrompt implements EasyCallPrompt {
		
		@Override
		public void call(Conversable conversable) {
			conversable.beginConversation(new ConversationFactory(OnimaAPI.getInstance()).withFirstPrompt(this).withEscapeSequence("/no").withModality(false).withLocalEcho(true).buildConversation(reporter.toPlayer()));
		}

		@Override
		public Prompt acceptInput(ConversationContext context, String input) {
			if (input.equalsIgnoreCase("non") || input.equalsIgnoreCase("aucun"))
				context.getForWhom().sendRawMessage("§cAucun lien n'a été fourni");
			else {
				transaction.setLinkToBugProof(input);
				context.getForWhom().sendRawMessage("§aLien ajouté.");
			}
			
			reporter.openMenu(BugReportMenu.this);
			
			return Prompt.END_OF_CONVERSATION;
		}

		@Override
		public String getPromptText(ConversationContext context) {
			return "§7§lSi vous avez une image ou une vidéo à fournir entrez la. Sinon tapez non";
		}
		
	}
	
	private class PlayerDescriptionPrompt extends StringPrompt implements EasyCallPrompt {
		
		@Override
		public void call(Conversable conversable) {
			conversable.beginConversation(new ConversationFactory(OnimaAPI.getInstance()).withFirstPrompt(this).withEscapeSequence("/no").withModality(false).withLocalEcho(true).buildConversation(reporter.toPlayer()));
		}

		@Override
		public Prompt acceptInput(ConversationContext context, String input) {
			transaction.setPlayerActionsDescription(input);
			context.getForWhom().sendRawMessage("§aDescription ajoutée.");
			reporter.openMenu(BugReportMenu.this);
			
			return Prompt.END_OF_CONVERSATION;
		}

		@Override
		public String getPromptText(ConversationContext context) {
			return "§7§lDécrivez ce que vous faisiez au moment où le bug a eu lieu.";
		}
		
	}
	
	private class BugDescriptionPrompt extends StringPrompt implements EasyCallPrompt {
		
		@Override
		public void call(Conversable conversable) {
			conversable.beginConversation(new ConversationFactory(OnimaAPI.getInstance()).withFirstPrompt(this).withEscapeSequence("/no").withModality(false).withLocalEcho(true).buildConversation(reporter.toPlayer()));
		}

		@Override
		public Prompt acceptInput(ConversationContext context, String input) {
			transaction.setBugDescription(input);
			context.getForWhom().sendRawMessage("§aDescription ajoutée.");
			reporter.openMenu(BugReportMenu.this);
			
			return Prompt.END_OF_CONVERSATION;
		}

		@Override
		public String getPromptText(ConversationContext context) {
			return "§7§lDécrivez le bug dont vous avez été témoin.";
		}
		
	}
	
	private class TimePrompt extends StringPrompt implements EasyCallPrompt {
		
		@Override
		public void call(Conversable conversable) {
			conversable.beginConversation(new ConversationFactory(OnimaAPI.getInstance()).withFirstPrompt(this).withEscapeSequence("/no").withModality(false).withLocalEcho(true).buildConversation(reporter.toPlayer()));
		}

		@Override
		public Prompt acceptInput(ConversationContext context, String input) {
			Conversable conversable = context.getForWhom();
			
			if (transaction.setTimeWhenBugOccured(input)) {
				conversable.sendRawMessage("§7Vous avez dit que ce bug a eu lieu un §e" + new CasualFormatDate("d u z hi").toNormalDate(transaction.getTimeWhenBugOccured()));
				reporter.openMenu(BugReportMenu.this);
				return Prompt.END_OF_CONVERSATION;
			}
			
			conversable.sendRawMessage("§cFormat invalide ! Si vous souhaiter abadonner tapez §l/no");
			return new TimePrompt();
		}

		@Override
		public String getPromptText(ConversationContext context) {
			return "§7Entrez une date et une heure sous le format suivant -> §ejj/mm/yyyy - hh:MM \n§7Exemple: §e§o16/02/2020 - 13:41§7.";
		}
		
	}
	
	private interface EasyCallPrompt {
		void call(Conversable conversable);
	}

}
