package net.onima.onimaapi.commands.punishment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.punishment.Ban;
import net.onima.onimaapi.punishment.utils.Punishment;
import net.onima.onimaapi.punishment.utils.PunishmentType;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.rank.RankType;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;

public class BanCommand implements CommandExecutor, TabCompleter {

	private JSONMessage usage = new JSONMessage("§7/ban <player> <reason>", "§d§oBan un joueur.");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_BAN_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}

		if (args.length < 2) {
			usage.send(sender, "§7Utilisation : ");
			return false;
		}
		
		OfflineAPIPlayer.getPlayer(args[0], offlineAPIPlayer -> {
			if (offlineAPIPlayer == null) {
				sender.sendMessage("§c" + args[0] + " ne s'est jamais connecté sur le serveur !");
				return;
			}

			if (offlineAPIPlayer.hasPunishment(PunishmentType.BAN)) {
				sender.sendMessage("§c" + Methods.getName(offlineAPIPlayer, true) + " est déjà banni !");
				return;
			}
			
			if (offlineAPIPlayer.getRank().getRankType().getValue() >= 10 && RankType.getRank(sender).getValue() < 100) {
				sender.sendMessage("§cVous devez d'abord dérank le joueur a un rank de joueur (par exemple " + RankType.DEFAULT.getName() + "§c) avant de le bannir !");
				return;
			}
			
			String reason = StringUtils.join(args, ' ', 1, args.length);
			Punishment ban = new Ban(sender instanceof Player ? ((Player) sender).getUniqueId() : ConfigurationService.CONSOLE_UUID, offlineAPIPlayer.getUUID());
			
			ban.setSilent(args[args.length - 1].equalsIgnoreCase("-s"));
			ban.setReason(ban.isSilent() ? reason.substring(0, reason.length() - 2) : reason);
			ban.execute();
		});
		
		return true;
		
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_BAN_COMMAND.has(sender) && args.length != 1)
			return Collections.emptyList();
		
		List<String> completions = new ArrayList<>();
		
		for (OfflineAPIPlayer offlineAPIPlayer : OfflineAPIPlayer.getDisconnectedOfflineAPIPlayers()) {
			if (offlineAPIPlayer.hasPunishment(PunishmentType.BAN))
				continue;
			
			String name = Methods.getName(offlineAPIPlayer);
			
			if (StringUtil.startsWithIgnoreCase(name, args[0]))
				completions.add(name);
		}
		
		return completions;
	}

}
