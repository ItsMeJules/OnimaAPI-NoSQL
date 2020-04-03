package net.onima.onimaapi.commands.balance.arguments;

import java.util.UUID;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.Balance;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;
import net.onima.onimaapi.utils.time.Time;
import net.onima.onimaapi.utils.time.Time.LongTime;

public class BalanceTopArgument extends BasicCommandArgument {
	
	private static final int MAX_BALANCE_PER_PAGE = 10;

	public BalanceTopArgument() {
		super("top", OnimaPerm.ONIMAAPI_BALANCE_TOP_ARGUMENT);
		
		usage = new JSONMessage("§7/balance " + name + " (page)", "§d§oAffiche les joueurs les plus riche du serveur.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 1, true))
			return true;
		
		if (args.length < 2) {
			showPage(sender, label, 1);
			return true;
		}
		
		Integer page = Methods.toInteger(args[1]);
		
		if (page != null)
			showPage(sender, label, page);
		else
			showPage(sender, label, 1);
			
		return true;
	}
	
	private void showPage(CommandSender sender, String label, int pageNumber) {
		Balance.getHighestBalances(list -> {
			Multimap<Integer, String> pages = ArrayListMultimap.create();
			int helps = 0;
			int index = 1;
			int position = 1;
			
			for (Document document : list) {
				helps++;
				pages.get(index).add(position + ". §e" + Methods.getRealName(Bukkit.getOfflinePlayer(UUID.fromString(document.getString("uuid")))) + " §7- §e" + ((Number) document.get("balance", Document.class).get("amount")).intValue() + ConfigurationService.MONEY_SYMBOL);
				position++;
				
				if (helps == MAX_BALANCE_PER_PAGE) {
					index++;
					helps = 0;
				}
			}
			
			if (!pages.containsKey(pageNumber)) {
				sender.sendMessage("§cLa page " + pageNumber + " n'existe pas !");
				return;
			}
			
			sender.sendMessage("§7" + ConfigurationService.STAIGHT_LINE);
			for (String line : pages.get(pageNumber)) 
				sender.sendMessage(line);
			
			sender.sendMessage("\n§7§oUpdate dans : " + LongTime.setYMDWHMSFormat((OnimaAPI.getLastSave() + 5 * Time.MINUTE) - System.currentTimeMillis()));
			sender.sendMessage("§7" + ConfigurationService.STAIGHT_LINE);
		}, pageNumber * MAX_BALANCE_PER_PAGE);

	}

}
