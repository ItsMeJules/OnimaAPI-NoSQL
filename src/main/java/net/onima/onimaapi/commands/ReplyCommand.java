package net.onima.onimaapi.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.event.chat.PrivateMessageEvent;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.utils.Options;
import net.onima.onimaapi.players.utils.PlayerOption;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.PrivateMessage;

public class ReplyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.REPLY_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		CommandSender lastMessager = MessageCommand.getLastMessager(sender);
		
		if (args.length == 0) {
			if (lastMessager == null)
				sender.sendMessage("§cVous n'êtes pas en conversation.");
			else
				sender.sendMessage("§6Vous êtes en conversation avec §f" + lastMessager.getName());
			return false;
		}
		
		if (lastMessager == null || lastMessager instanceof OfflinePlayer && !((OfflinePlayer) lastMessager).isOnline()) {
			sender.sendMessage("§cLa personne à qui vous souhaitez répondre n'est malhereusement plus en ligne.");
			return false;
		}
		
		Options options = lastMessager instanceof Player ? APIPlayer.getPlayer((Player) lastMessager).getOptions() : null; 
		
		if (options != null && !options.getBoolean(PlayerOption.GlobalOptions.PRIVATE_MESSAGE)) {
			sender.sendMessage("§c" + Methods.getName(lastMessager) + " n'accepte pas les messages privés !");
			return false;
		}
		
		PrivateMessageEvent event = new PrivateMessageEvent(sender, lastMessager, StringUtils.join(args, ' ', 0, args.length));
		Bukkit.getPluginManager().callEvent(event);
		
		if (event.isCancelled()) 
			return false;
		
		boolean sound = options == null ? false : options.getBoolean(PlayerOption.GlobalOptions.SOUNDS);
		
		new PrivateMessage(sender, lastMessager)
		.message(event.getMessage())
		.sound(sound ? ConfigurationService.PRIVATE_MESSAGE_SOUND : null)
		.send(event.getRankType(), event.getReaderRank());
		MessageCommand.setLastMessager(lastMessager, sender);
		MessageCommand.setLastMessager(sender, lastMessager);
		
		return true;
	}

}
