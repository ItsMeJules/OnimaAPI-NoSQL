package net.onima.onimaapi.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.event.chat.PrivateMessageEvent;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.utils.PlayerOption;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Options;
import net.onima.onimaapi.utils.PrivateMessage;

public class MessageCommand implements CommandExecutor {
	
	private JSONMessage usage = new JSONMessage("§7/m <player> <message>", "§d§oEnvoie un message privé à un joueur.");
	private static CommandSender lastConsoleMessager;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 2) {
			usage.send(sender, "§7Utilisation : ");
			return false;
		}
		
		if (!OnimaPerm.MESSAGE_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		APIPlayer apiPlayer = APIPlayer.getPlayer(args[0]);
		
		if (apiPlayer == null) {
			sender.sendMessage("§cLe joueur " + args[0] + " n'est pas connecté !");
			return false;
		}
		
		Options options = apiPlayer.getOptions();
		
		if (!options.getBoolean(PlayerOption.GlobalOptions.PRIVATE_MESSAGE)) {
			sender.sendMessage("§c" + apiPlayer.getDisplayName() + " n'accepte pas les messages privés !");
			return false;
		}
		
		PrivateMessageEvent event = new PrivateMessageEvent(sender, apiPlayer.toPlayer(), StringUtils.join(args, ' ', 1, args.length));
		Bukkit.getPluginManager().callEvent(event);
		
		if (event.isCancelled()) 
			return false;
		
		new PrivateMessage(sender, apiPlayer.toPlayer())
		.message(event.getMessage())
		.sound(options.getBoolean(PlayerOption.GlobalOptions.SOUNDS) ? ConfigurationService.PRIVATE_MESSAGE_SOUND : null)
		.send(event.getRankType(), event.getReaderRank());
		apiPlayer.setLastMessageSender(sender);
		setLastMessager(sender, apiPlayer.toPlayer());
		return false;
	}

	public static CommandSender getLastMessager(CommandSender sender) {
		if (sender instanceof Player)
			return APIPlayer.getPlayer((Player) sender).getLastMessageSender();
		else
			return lastConsoleMessager;
	}
	
	public static void setLastMessager(CommandSender sender, CommandSender receiver) {
		if (sender instanceof Player)
			APIPlayer.getPlayer((Player) sender).setLastMessageSender(receiver);
		else
			lastConsoleMessager = receiver;
	}
	
}
