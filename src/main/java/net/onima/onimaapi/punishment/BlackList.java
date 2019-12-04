package net.onima.onimaapi.punishment;

import java.util.UUID;

import org.bson.Document;
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

public class BlackList extends Punishment implements ServerRestricted {
	
	private String ip;

	{
		expire = CANT_EXPIRE_TIME;
	}
	
	public BlackList(String ip, UUID sender, UUID receiver) {
		super(PunishmentType.BLACKLIST, sender, receiver);
		
		this.ip = ip;
	}
	
	@Override
	public String errorMessage() {
		return "§c§m-|" + ConfigurationService.STAIGHT_LINE + "|" + ConfigurationService.STAIGHT_LINE + "|-"
				+ "\n§4❣ §cVous êtes blacklist de ce serveur §4❣"
				+ "\n§4» §cBlacklist Le : §6" + Methods.toFormatDate(issued, ConfigurationService.DATE_FORMAT_HOURS)
				+ "\n§4» §cRaison : §6" + reason
				+ ""
				+ "\n\n§4» §cCe blacklist est §6définitif§c."
				+ "\n§4» §cVous ne pouvez pas faire appel.";
	}

	@Override
	public boolean execute() {
		boolean removed = isRemoved();
		String senderName = ConfigurationService.CONSOLE_UUID.equals(removed ? remover : sender) ? "Console" : APIPlayer.getPlayer(removed ? remover : sender).getName();
		OfflineAPIPlayer receiverPlayer = OfflineAPIPlayer.getOfflineAPIPlayers().get(receiver);
		String banned = "a été " + (removed ? "dé" : "§c") + "§8§lblacklist §apar §f";
		String silentStr = (silent ? "§7[Silencieux] " : "") + "§f";
		
		BaseComponent[] builderPlayers = new ComponentBuilder(silentStr + receiverPlayer.getName() + " §a" + banned + senderName + "§a.")
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(removed ? "§eLe retour du fisdeup." : "§eAdieu fisdeup.").create())).create();
		
		BaseComponent[] builderStaff = new ComponentBuilder(silentStr + Methods.getName(receiverPlayer, true) + " §a" + banned + senderName + "§a.")
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eRaison : " + (removed ? removeReason : reason)).create())).create();
		
		for (APIPlayer apiPlayer : APIPlayer.getOnlineAPIPlayers()) {
			if (!OnimaPerm.ONIMAAPI_BLACKLIST_COMMAND.has(apiPlayer.toPlayer())) {
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

		if (!receiverPlayer.getPunishments().contains(this)) {
			receiverPlayer.getPunishments().add(this);
			PunishmentListener.loadedPunishment.put(receiver, this);
		}
		
		if (!removed)
			initID();
		
		return true;
	}
	
	@Override
	public Document getDocument(Object... objects) {
		return super.getDocument().append("ip", ip);
	}
	
	public void setIP(String ip) {
		this.ip = ip;
	}
	
	public String getIP() {
		return ip;
	}
	
}
