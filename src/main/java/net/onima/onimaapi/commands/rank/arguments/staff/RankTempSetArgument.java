package net.onima.onimaapi.commands.rank.arguments.staff;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.event.ranks.RankReceivedEvent;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.rank.Rank;
import net.onima.onimaapi.rank.RankType;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;
import net.onima.onimaapi.utils.time.Time.LongTime;
import net.onima.onimaapi.utils.time.TimeUtils;

public class RankTempSetArgument extends BasicCommandArgument {
	
	public RankTempSetArgument() {
		super("tempset", OnimaPerm.RANK_COMMAND_TEMPSET);
		
		usage = new JSONMessage("§7/rank " + name + " <player> <rank> <time>", "§d§oDéfini un rank avec un temps de reset.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 4) {
			usage.send(sender, "§7Utilisation : ");
			return false;
		}
		
		UUID uuid = UUIDCache.getUUID(args[1]);
		
		if (uuid == null) {
			sender.sendMessage("§c" + args[1] + " ne s'est jamais connecté sur le serveur !");
			return false;
		}
		
		OfflineAPIPlayer.getPlayer(uuid, offlinePlayer -> {
			RankType playerRank = sender instanceof Player ? APIPlayer.getPlayer((Player) sender).getRank().getRankType() : RankType.CONSOLE;
			RankType targetRank = offlinePlayer.getRank().getRankType();
			
			if (playerRank.getValue() < targetRank.getValue()) {
				sender.sendMessage("§cVous ne pouvez pas définir le rank d'un jouer plus haut gradé que vous !");
				sender.sendMessage(targetRank.getName() + " §f§oest supérieur à " + playerRank.getName() + "§f§o.");
				return;
			}
			
			RankType newRank = RankType.fromString(args[2]);
			
			if (newRank == null) {
				sender.sendMessage("§cLe rank " + args[2] + " n'existe pas !");
				return;
			}
			
			if (newRank.getValue() > playerRank.getValue()) {
				sender.sendMessage("§cVous ne pouvez pas définir un rank supérieur au votre !");
				sender.sendMessage(newRank.getName() + " §f§oest supérieur à " + playerRank.getName() + "§f§o.");
				return;
			}
			
			long time = TimeUtils.timeToMillis(args[3]);
			
			if (time == -1) {
				sender.sendMessage("§cLa valeur " + args[2] + " n'est pas un nombre !");
				return;
			} else if (time == -2) {
				sender.sendMessage("§cMauvais format pour : " + args[2] + " il faut écrire les deux premières lettres du temps.\n§cExemple : §o/rank tempset " + args[1] + ' ' + args[2] + " §c15mi pour 15 minutes.");
				return; 
			}
			
			String format = LongTime.setYMDWHMSFormat(time);
			Rank rank = new Rank(offlinePlayer, newRank);
			
			sender.sendMessage("§d§oVous §7avez défini le rank de §d§o" + Methods.getName(offlinePlayer, true) + " §7sur " + newRank.getName() + " §7pour §a" + format + "§7.");
			offlinePlayer.setRank(rank);
			
			if (offlinePlayer.isOnline()) {
				((APIPlayer) offlinePlayer).sendMessage(APIPlayer.getPlayer((Player) sender).getRankName(true) + " §7vous a donné le rank de " + newRank.getName() + " §7pour §a" + format + "§7.");
				Bukkit.getPluginManager().callEvent(new RankReceivedEvent(rank, (APIPlayer) offlinePlayer, true, sender));
			}
			
		});

		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> completions = new ArrayList<>();
		
		if (args.length == 2) {
			RankType senderRank = sender instanceof Player ? APIPlayer.getPlayer((Player) sender).getRank().getRankType() : RankType.CONSOLE;
			
			for (OfflineAPIPlayer offlineAPI : OfflineAPIPlayer.getDisconnectedOfflineAPIPlayers()) {
				if (offlineAPI == null)
					continue;
				
				Rank rank = offlineAPI.getRank();
				String name = Methods.getName(offlineAPI);
				
				if (rank == null) {
					completions.add(name);
					continue;
				}
				
				if (rank.getRankType().getValue() <= senderRank.getValue() && StringUtil.startsWithIgnoreCase(name, args[1]))
					completions.add(name);
			}
		} else if (args.length == 3) {
			RankType senderRank = sender instanceof Player ? APIPlayer.getPlayer((Player) sender).getRank().getRankType() : RankType.CONSOLE;
			
			for (RankType rank : RankType.values()) {
				if (rank == RankType.CONSOLE || rank.getValue() > senderRank.getValue())
					continue;
				
				if (StringUtil.startsWithIgnoreCase(rank.name(), args[2]))
					completions.add(rank.name());
			}
		}
		
		return completions;
	}

}
