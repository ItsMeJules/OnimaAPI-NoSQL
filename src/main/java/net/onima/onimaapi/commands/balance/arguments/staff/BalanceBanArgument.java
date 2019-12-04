package net.onima.onimaapi.commands.balance.arguments.staff;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.Balance;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class BalanceBanArgument extends BasicCommandArgument {

	public BalanceBanArgument() {
		super("ban", OnimaPerm.ONIMAAPI_BALANCE_BAN_ARGUMENT);
		
		usage = new JSONMessage("§7/balance " + name + " <player>", "§d§oBloque l'argent d'un joueur.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 2, true))
			return false;
		
		OfflineAPIPlayer.getPlayer(args[1], offline -> {
			if (offline == null) {
				sender.sendMessage("§c" + args[1] + " ne s'est jamais connecté sur le serveur !");
				return;
			}
			
			Balance balance = offline.getBalance();
			
			balance.setBan(!balance.isBanned());
			
			String banned = balance.isBanned() ? "§cbloqué" : "§adébloqué";
			
			if (offline.isOnline())
				((APIPlayer) offline).sendMessage("§d" + Methods.getRealName(sender) + " §7a " + banned + " §7votre argent !");
			
			sender.sendMessage("§dVous §7avez " + banned + " §7l'argent de §d" + Methods.getName(offline, true) + "§7.");
		});
		
		return true;
	}

}
