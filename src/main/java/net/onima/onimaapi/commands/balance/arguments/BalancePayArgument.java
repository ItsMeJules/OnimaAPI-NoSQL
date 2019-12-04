package net.onima.onimaapi.commands.balance.arguments;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.Balance;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class BalancePayArgument extends BasicCommandArgument {

	public BalancePayArgument() {
		super("pay", OnimaPerm.ONIMAAPI_BALANCE_PAY_ARGUMENT);

		usage = new JSONMessage("§7/balance " + name + " <player> <amount>", "§d§oPaye un joueur.");
		playerOnly = true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 3, true))
			return false;
		
		APIPlayer apiPlayer = APIPlayer.getPlayer((Player) sender);
		Balance balance = apiPlayer.getBalance();
		
		if (balance.isBanned()) {
			sender.sendMessage("§cVotre argent est bloqué !");
			return false;
		}
		
		OfflineAPIPlayer.getPlayer(args[1], offline -> {
			if (offline == null) {
				sender.sendMessage("§c" + args[1] + " ne s'est jamais connecté sur le serveur !");
				return;
			}
			
			Balance offlineBalance = offline.getBalance();
			
			if (offlineBalance.isBanned()) {
				sender.sendMessage("§cL'argent de " + Methods.getNameFromArg(offline, args[1]) + " est bloqué !");
				return;
			}
			
			Double amount = Methods.toDouble(args[2]);
			
			if (amount == null) {
				sender.sendMessage("§c" + args[2] + " n'est pas un nombre !");
				return;
			}
			
			amount = Math.abs(amount);
			
			if (amount == 0) {
				sender.sendMessage("§cVous ne pouvez pas envoyer 0" + ConfigurationService.MONEY_SYMBOL + '.');
				return;
			}
			
			if (balance.getAmount() <= 0) {
				sender.sendMessage("§cVous n'avez pas assez d'argent pour pouvoir en envoyer.");
				return;
			}
			
			if (amount > balance.getAmount())
				amount = balance.getAmount();
			
			balance.removeAmount(amount);
			offlineBalance.addAmount(amount);
			
			sender.sendMessage("§dVous §7avez envoyé §d" + amount + ConfigurationService.MONEY_SYMBOL + " §7à §d" + Methods.getNameFromArg(offline, args[1]) + "§7.");
			
			if (offline.isOnline())
				((APIPlayer) offline).sendMessage("§d" + Methods.getName(sender) + " §7vous a envoyé §d" + amount + ConfigurationService.MONEY_SYMBOL + "§7.");
		});
		
		return true;
	}

}
