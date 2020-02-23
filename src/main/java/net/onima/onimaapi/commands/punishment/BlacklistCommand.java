package net.onima.onimaapi.commands.punishment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.punishment.BlackList;
import net.onima.onimaapi.punishment.utils.Punishment;
import net.onima.onimaapi.punishment.utils.PunishmentType;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.rank.RankType;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;

public class BlacklistCommand implements CommandExecutor, TabCompleter {
	
	private JSONMessage usage = new JSONMessage("§7/blacklist <player> <reason>", "§d§oBlacklist un joueur.");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_BLACKLIST_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}

		if (args.length < 2) {
			usage.send(sender, "§7Utilisation : ");
			return false;
		}
		
		UUID uuid = UUIDCache.getUUID(args[0]);
		
		if (uuid == null) {
			sender.sendMessage("§cLe joueur " + args[0] + " n'existe pas !");
			return false;
		}
		
		OfflineAPIPlayer.getPlayer(uuid, offlineAPIPlayer -> {
			if (offlineAPIPlayer.hasPunishment(PunishmentType.BLACKLIST)) {
				sender.sendMessage("§c" + Methods.getName(offlineAPIPlayer, true) + " est déjà black listé !");
				return;
			}
			
			if (offlineAPIPlayer.getRank().getRankType().getValue() >= 10 && RankType.getRank(sender).getValue() < 100) {
				sender.sendMessage("§cVous devez d'abord dérank le joueur a un rank de joueur (par exemple " + RankType.DEFAULT.getName() + "§c) avant de le bannir !");
				return;
			}
			
			String reason = StringUtils.join(args, ' ', 1, args.length);
			Punishment blacklist = new BlackList(offlineAPIPlayer.getIP(), sender instanceof Player ? ((Player) sender).getUniqueId() : ConfigurationService.CONSOLE_UUID, offlineAPIPlayer.getUUID());
			
			blacklist.setSilent(args[args.length - 1].equalsIgnoreCase("-s"));
			blacklist.setReason(blacklist.isSilent() ? reason.substring(0, reason.length() - 2) : reason);
			blacklist.execute();
		});
		
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_BLACKLIST_COMMAND.has(sender) && args.length != 1)
			return Collections.emptyList();
		
		List<String> completions = new ArrayList<>();
		
		for (OfflineAPIPlayer offlineAPIPlayer : OfflineAPIPlayer.getDisconnectedOfflineAPIPlayers()) {
			if (offlineAPIPlayer.hasPunishment(PunishmentType.BLACKLIST))
				continue;
			
			String name = Methods.getName(offlineAPIPlayer);
			
			if (StringUtil.startsWithIgnoreCase(name, args[0]))
				completions.add(name);
		}
		
		return completions;
	}

}
