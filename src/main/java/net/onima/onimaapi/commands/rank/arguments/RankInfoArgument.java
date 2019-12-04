package net.onima.onimaapi.commands.rank.arguments;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.rank.Rank;
import net.onima.onimaapi.rank.RankType;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;
import net.onima.onimaapi.utils.time.Time.LongTime;

public class RankInfoArgument extends BasicCommandArgument {

	public RankInfoArgument() {
		super("info", OnimaPerm.RANK_COMMAND_INFO);
		
		usage = new JSONMessage("§7/rank " + name + " <player>", "§d§oAffiche les informations du rank du joueur.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 2) {
			usage.send(sender, "§7Utilisation : ");
			return false;
		}
		
		UUID uuid = UUIDCache.getUUID(args[1]);
		
		if (uuid == null) {
			sender.sendMessage("§c" + args[1] + " ne s'est jamais connecté sur le serveur !");
			return false;
		}
		
		OfflineAPIPlayer.getPlayer(uuid, offlinePlayer -> {
			Rank rank = offlinePlayer.getRank();
			RankType rankType = rank.getRankType();
			
			sender.sendMessage("§8" + ConfigurationService.STAIGHT_LINE);
			Methods.sendJSON(sender, new ComponentBuilder("§7Le rank de " + Methods.getNameFromArg(offlinePlayer, args[1]) + " §7est : ")
					.append(rankType.getName())
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(rankType.getDescription()).create()))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/menu ranks")).create());
			
			if (rank.isTemporary())
				sender.sendMessage("§7Il lui reste §a" + LongTime.setYMDWHMSFormat(rank.getInstant().toEpochMilli()) + " §7avant de perdre son rank.");
			else
				sender.sendMessage("§7Son rank est à §avie§7.");
		});
		
		return true;
	}
	
}
