package net.onima.onimaapi.punishment;

import java.util.UUID;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.punishment.utils.Punishment;
import net.onima.onimaapi.punishment.utils.PunishmentType;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.time.Time;

public class Warn extends Punishment {
	
	public static final int DAYS_BAN_15;
	public static final int DAYS_BAN_30;
	public static final int PERMANENT_BAN;
	
	static {
		DAYS_BAN_15 = 3;
		DAYS_BAN_30 = 6;
		PERMANENT_BAN = 9;
	}

	public Warn(UUID sender, UUID receiver) {
		super(PunishmentType.WARN, sender, receiver);
	}

	@Override
	public String errorMessage() {
		return " §2» Vous avez été warn : §a" + reason;
	}

	@Override
	public boolean execute() {
		String senderName = ConfigurationService.CONSOLE_UUID.equals(sender) ? "Console" : APIPlayer.getPlayer(sender).getName();
		OfflineAPIPlayer receiverPlayer = OfflineAPIPlayer.getOfflineAPIPlayers().get(receiver);
		String silentStr = (silent ? "§7[Silencieux] " : "") + "§f";
		int warns = (int) receiverPlayer.getPunishments().stream().filter(punishment -> punishment instanceof Warn).count() + 1;
		
		BaseComponent[] builderPlayers = new ComponentBuilder(silentStr + receiverPlayer.getName() + " §aa été §6warn §apar §f" + senderName + "§a.")
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eAïe aïe aïe.").create())).create();
		
		BaseComponent[] builderStaff = new ComponentBuilder(silentStr + Methods.getName(receiverPlayer, true) + " §aa été §6warn §apar §f" + senderName + "§a.")
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eRaison : " + reason).create())).create();
		
		for (APIPlayer apiPlayer : APIPlayer.getOnlineAPIPlayers()) {
			if (!OnimaPerm.ONIMAAPI_WARN_COMMAND.has(apiPlayer.toPlayer())) {
				if (silent)
					continue;
				else
					apiPlayer.sendMessage(builderPlayers);
			} else
				apiPlayer.sendMessage(builderStaff);
		}
		
		Methods.sendJSON(Bukkit.getConsoleSender(), builderStaff);
		
		if (receiverPlayer.isOnline())
			((APIPlayer) receiverPlayer).sendMessage(errorMessage());
		
		if (!receiverPlayer.getPunishments().contains(this))
			receiverPlayer.getPunishments().add(this);
		
		initID();
		
		switch (warns) {
		case 3:
			Punishment tempBan = new TempBan(ConfigurationService.CONSOLE_UUID, receiver, 5 * Time.DAY);
			
			tempBan.setSilent(true);
			tempBan.setReason("Vous avez reçu trop de warns ! §f(§c" + warns + "§r)");
			tempBan.execute();
			break;
		case 6:
			Punishment temp = new TempBan(ConfigurationService.CONSOLE_UUID, receiver, 14 * Time.DAY);
			
			temp.setSilent(true);
			temp.setReason("Vous avez reçu trop de warns ! §f(§c" + warns + "§r)");
			temp.execute();
			break;
		case 9:
			Punishment tempban = new Ban(ConfigurationService.CONSOLE_UUID, receiver);
			
			tempban.setSilent(true);
			tempban.setReason("Vous avez reçu trop de warns ! §f(§c" + warns + "§r)");
			tempban.execute();
			break;
		case 12:
			Punishment ban = new Ban(ConfigurationService.CONSOLE_UUID, receiver);
			
			ban.setSilent(true);
			ban.setReason("Vous avez reçu trop de warns ! §f(§c" + warns + "§r)");
			ban.execute();
			break;
		default:
			break;
		}
		
		return true;
	}

}
