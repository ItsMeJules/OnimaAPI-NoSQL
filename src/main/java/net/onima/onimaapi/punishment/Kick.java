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

public class Kick extends Punishment {

	public Kick(UUID sender, UUID receiver) {
		super(PunishmentType.KICK, sender, receiver);
	}

	@Override
	public String errorMessage() {
		return "§2§m-|" + ConfigurationService.STAIGHT_LINE + "|" + ConfigurationService.STAIGHT_LINE + "|-"
				+ "\n§2❣ Vous êtes kick de ce serveur ❣"
				+ "\n§2» §aRaison : §6" + reason;
	}

	@Override
	public boolean execute() {
		String senderName = ConfigurationService.CONSOLE_UUID.equals(sender) ? "Console" : APIPlayer.getPlayer(sender).getName();
		OfflineAPIPlayer receiverPlayer = OfflineAPIPlayer.getOfflineAPIPlayers().get(receiver);
		String silentStr = (silent ? "§7[Silencieux] " : "") + "§f";
		
		BaseComponent[] builderPlayers = new ComponentBuilder(silentStr + receiverPlayer.getName() + " §aa été §ekick §apar §f" + senderName + "§a.")
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eCheh.").create())).create();
		
		BaseComponent[] builderStaff = new ComponentBuilder(silentStr + Methods.getName(receiverPlayer, true) + " §aa été §ekick §apar §f" + senderName + "§a.")
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eRaison : " + reason).create())).create();
		
		for (APIPlayer apiPlayer : APIPlayer.getOnlineAPIPlayers()) {
			if (!OnimaPerm.ONIMAAPI_KICK_COMMAND.has(apiPlayer.toPlayer())) {
				if (silent)
					continue;
				else
					apiPlayer.sendMessage(builderPlayers);
			} else
				apiPlayer.sendMessage(builderStaff);
		}
		
		Methods.sendJSON(Bukkit.getConsoleSender(), builderStaff);
		
		if (receiverPlayer.isOnline())
			((APIPlayer) receiverPlayer).toPlayer().kickPlayer(errorMessage());

		if (!receiverPlayer.getPunishments().contains(this))
			receiverPlayer.getPunishments().add(this);
		
		initID();
		
		return true;
	}

}
