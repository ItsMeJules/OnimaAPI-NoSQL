package net.onima.onimaapi.commands.balance.arguments;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.callbacks.VoidCallback;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class BalanceCheckArgument extends BasicCommandArgument {

	public BalanceCheckArgument() {
		super("check", OnimaPerm.ONIMAAPI_BALANCE_CHECK_ARGUMENT);
		
		usage = new JSONMessage("§7/balance " + name + " <player>", "§d§oAffiche le solde.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 1, true))
			return false;
		
		VoidCallback<OfflineAPIPlayer> callback = player -> {
			String name = args.length > 1 ? args[1] : Methods.getName(player);
			
			if (player == null) {
				sender.sendMessage("§c" + name + " ne s'est jamais connecté !");
				return;
			}
			
			String msg = Methods.getNameFromArg(sender, name).equalsIgnoreCase(name) ? "§dVous §7avez" : "§d" + name + " §7a";
			
			sender.sendMessage(msg + " §c" + player.getBalance().getAmount() + ConfigurationService.MONEY_SYMBOL + "§7.");
		};
		
		if (args.length <= 1) {
			if (sender instanceof Player)
				OfflineAPIPlayer.getPlayer((Player) sender, callback);
			else {
				sender.sendMessage("§cLa console n'a pas de solde !");
				return false;
			}
		} else {
			UUID uuid = UUIDCache.getUUID(args[1]);
			
			if (uuid == null) {
				sender.sendMessage("§cLe joueur " + args[1] + " n'existe pas !");
				return false;
			}
			
			OfflineAPIPlayer.getPlayer(uuid, callback);
		}
		
		return true;
	}
	
}
