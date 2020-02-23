package net.onima.onimaapi.commands.balance.arguments.staff;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.Balance;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class BalanceAddArgument extends BasicCommandArgument {

	public BalanceAddArgument() {
		super("add", OnimaPerm.ONIMAAPI_BALANCE_ADD_ARGUMENT);
		
		usage = new JSONMessage("§7/balance " + name + " <player> <amount>", "§d§oGive de l'argent.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 3, true))
			return false;
		
		UUID uuid = UUIDCache.getUUID(args[1]);
		
		if (uuid == null) {
			sender.sendMessage("§cLe joueur " + args[1] + " n'existe pas !");
			return false;
		}
		
		OfflineAPIPlayer.getPlayer(uuid, offline -> {
			Balance balance = offline.getBalance();
			
			if (balance.isBanned()) {
				sender.sendMessage("§cL'argent de " + offline.getName() + " est bloqué !");
				return;
			}
			
			Double amount = Methods.toDouble(args[2]);
			
			if (amount == null) {
				sender.sendMessage("§c" + args[2] + " n'est pas un nombre !");
				return;
			}
			
			balance.addAmount(amount);
			
			if (offline.isOnline())
				((APIPlayer) offline).sendMessage("§d" + Methods.getRealName(sender) + " §7vous a give §d" + amount + ConfigurationService.MONEY_SYMBOL + "§7.");
			
			sender.sendMessage("§dVous §7avez give §d" + amount + ConfigurationService.MONEY_SYMBOL + " §7à §d" + Methods.getName(offline, true) + "§7.");
		});
		
		return true;
	}

}
