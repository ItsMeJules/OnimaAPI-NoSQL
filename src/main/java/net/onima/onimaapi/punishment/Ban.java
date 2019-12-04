package net.onima.onimaapi.punishment;

import java.util.UUID;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.onima.onimaapi.listener.PunishmentListener;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.punishment.utils.Punishment;
import net.onima.onimaapi.punishment.utils.PunishmentType;
import net.onima.onimaapi.punishment.utils.ServerRestricted;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;

public class Ban extends Punishment implements ServerRestricted {
	
//	private RankType previousRank;
//	private boolean claimedRank;
	
	{
		expire = CANT_EXPIRE_TIME;
	}
	
	public Ban(UUID sender, UUID receiver) {
		super(PunishmentType.BAN, sender, receiver);
	}
	
	@Override
	public String errorMessage() {
		return "§2§m-|" + ConfigurationService.STAIGHT_LINE + "|" + ConfigurationService.STAIGHT_LINE + "|-"
				+ "\n§2❣ Vous êtes banni de ce serveur ❣"
				+ "\n§2» §aBanni Le : §6" + Methods.toFormatDate(issued, ConfigurationService.DATE_FORMAT_HOURS)
				+ "\n§2» §aRaison : §6" + reason
				+ ""
				+ "\n\n§2» §aCe ban est §6permanent§2."
				+ "\n§2» §aVous pouvez faire appel sur : §ahttps://onima.fr/forum/punitions";
	}
	
	@Override
	public boolean execute() {
		boolean removed = isRemoved();
		String senderName = ConfigurationService.CONSOLE_UUID.equals(removed ? remover : sender) ? "Console" : APIPlayer.getPlayer(removed ? remover : sender).getName();
		OfflineAPIPlayer receiverPlayer = OfflineAPIPlayer.getOfflineAPIPlayers().get(receiver);
		String banned = "a été " + (removed ? "dé" : "§c") + "ban §apar §f";
		String silentStr = (silent ? "§7[Silencieux] " : "") + "§f";
		
		BaseComponent[] builderPlayers = new ComponentBuilder(silentStr + receiverPlayer.getName() + " §a" + banned + senderName + "§a.")
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(removed ? "§eLe retour MDR." : "§eUne pluie de F.").create())).create();
		
		BaseComponent[] builderStaff = new ComponentBuilder(silentStr + Methods.getName(receiverPlayer, true) + " §a" + banned + senderName + "§a.")
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eRaison : " + (removed ? removeReason : reason)).create())).create();
		
		for (APIPlayer apiPlayer : APIPlayer.getOnlineAPIPlayers()) {
			if (!OnimaPerm.ONIMAAPI_BAN_COMMAND.has(apiPlayer.toPlayer())) {
				if (silent)
					continue;
				else
					apiPlayer.sendMessage(builderPlayers);
			} else
				apiPlayer.sendMessage(builderStaff);
		}
		
		Methods.sendJSON(Bukkit.getConsoleSender(), builderStaff);
		
		if (receiverPlayer.isOnline() && !removed)
			((APIPlayer) receiverPlayer).toPlayer().kickPlayer(errorMessage());

//		if (!claimedRank && !removed) {
//			RankType rankType = receiverPlayer.getRank().getRankType();
//			
//			if (rankType.getValue() > 9) {
//				previousRank = rankType;
//				receiverPlayer.setRank(new Rank(receiverPlayer, RankType.DEFAULT));
//			}
//		} else if (!claimedRank && removed) {
//			receiverPlayer.setRank(new Rank(receiverPlayer, previousRank));
//			claimedRank = true;
//		}
//		
		if (!receiverPlayer.getPunishments().contains(this)) {
			receiverPlayer.getPunishments().add(this);
			PunishmentListener.loadedPunishment.put(receiver, this);
		}
		
		if (!removed)
			initID();
		
		return true;
	}
	
//	public RankType getPreviousRank() {
//		return previousRank;
//	}

}
