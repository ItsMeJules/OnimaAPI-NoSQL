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
import net.onima.onimaapi.utils.time.Time.LongTime;

public class Mute extends Punishment {

	public Mute(UUID sender, UUID receiver, long duration) {
		super(PunishmentType.MUTE, sender, receiver);
		
		setDuration(duration);
	}

	@Override
	public String errorMessage() {
		return ConfigurationService.STAIGHT_LINE
				+ "\n§aVous êtes mute, vous ne pouvez pas parler."
				+ "\n§aRaison du mute : §f" + reason
				+ "\n§aExpire dans : §f" + LongTime.setYMDWHMSFormat(getDuration())
				+ "\n" + ConfigurationService.STAIGHT_LINE;
	}

	@Override
	public boolean execute() {
		boolean removed = isRemoved();
		String senderName = ConfigurationService.CONSOLE_UUID.equals(removed ? remover : sender) ? "Console" : APIPlayer.getPlayer(removed ? remover : sender).getName();
		OfflineAPIPlayer receiverPlayer = OfflineAPIPlayer.getOfflineAPIPlayers().get(receiver);
		String muted = "a été " + (removed ? "dé" : "§7") + "mute par §f";
		String silentStr = (silent ? "§7[Silencieux] " : "") + "§f";
		
		BaseComponent[] builderPlayers = new ComponentBuilder(silentStr + receiverPlayer.getName() + " §a" + muted + senderName + "§a.")
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(removed ? "§eIl va la rouvrir..." : "§ePTDR go le faire rager.").create())).create();
		
		BaseComponent[] builderStaff = new ComponentBuilder(silentStr + Methods.getName(receiverPlayer, true) + " §a" + muted + senderName + "§a.")
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eRaison : " + (removed ? removeReason : reason) 
						+ "\n§eDurée : §6" + LongTime.setYMDWHMSFormat(getDuration())).create())).create();
		
		for (APIPlayer apiPlayer : APIPlayer.getOnlineAPIPlayers()) {
			if (!OnimaPerm.ONIMAAPI_MUTE_COMMAND.has(apiPlayer.toPlayer())) {
				if (silent)
					continue;
				else
					apiPlayer.sendMessage(builderPlayers);
			} else
				apiPlayer.sendMessage(builderStaff);
		}
		
		Methods.sendJSON(Bukkit.getConsoleSender(), builderStaff);
		
		if (!receiverPlayer.getPunishments().contains(this))
			receiverPlayer.getPunishments().add(this);
		
		if (!removed)
			initID();
		
		return true;
	}

}
