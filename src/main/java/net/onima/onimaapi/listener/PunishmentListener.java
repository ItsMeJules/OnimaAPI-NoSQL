package net.onima.onimaapi.listener;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Filters.or;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bson.Document;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import com.mongodb.MongoException;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.event.chat.ChatEvent;
import net.onima.onimaapi.event.chat.PrivateMessageEvent;
import net.onima.onimaapi.mongo.OnimaMongo;
import net.onima.onimaapi.mongo.OnimaMongo.OnimaCollection;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.punishment.Ban;
import net.onima.onimaapi.punishment.Mute;
import net.onima.onimaapi.punishment.utils.Punishment;
import net.onima.onimaapi.punishment.utils.PunishmentType;
import net.onima.onimaapi.punishment.utils.ServerRestricted;
import net.onima.onimaapi.utils.ConfigurationService;

public class PunishmentListener implements Listener {
	
	public static Map<UUID, Punishment> loadedPunishment = ExpiringMap.builder().expiration(2, TimeUnit.MINUTES).expirationPolicy(ExpirationPolicy.ACCESSED).build();
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogin(AsyncPlayerPreLoginEvent event) {
		UUID uuid = event.getUniqueId();
		OfflineAPIPlayer offline = OfflineAPIPlayer.getOfflineAPIPlayers().get(uuid);
		
		if (!OnimaAPI.getInstance().isLoaded()) {
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ConfigurationService.SERVER_NOT_LOADED);
			return;
		}
		
		if (offline == null) {
			long now = System.currentTimeMillis();
			Punishment punishment = loadedPunishment.get(uuid);
			
			if (punishment == null || !punishment.isActive()) {
				try {
					Document document = OnimaMongo.get(OnimaCollection.PLAYERS)
							.find(and(or(eq("uuid", uuid.toString()), eq("ip", event.getAddress().getHostAddress())),
									  in("punishments.punishment_type", "BAN", "TEMPBAN", "BLACKLIST"),
									  or(gt("punishments.expire", now), eq("punishments.expire", Ban.CANT_EXPIRE_TIME)),
									  eq("punishments.remover_uuid", null))).first();
					
					if (document == null)
						return;
					
					loop: for (Document punishDoc : document.getList("punishments", Document.class)) {
						switch (punishDoc.getString("punishment_type")) {
						case "BAN":
						case "TEMPBAN":
						case "BLACKLIST":
							long expire = punishDoc.getLong("expire");
							
							if (expire > now || expire == Ban.CANT_EXPIRE_TIME) {
								UUID removerUUID = null;
								String removerStrUUID = punishDoc.getString("remover_uuid");
								
								if (removerStrUUID != null)
									removerUUID = UUID.fromString(removerStrUUID);
								
								punishment = Punishment.fromDB(punishDoc.getInteger("id"),
										PunishmentType.valueOf(punishDoc.getString("punishment_type")),
										UUID.fromString(punishDoc.getString("sender_uuid")),
										uuid,
										removerUUID,
										punishDoc.getString("reason"),
										punishDoc.getString("remove_reason"),
										punishDoc.getBoolean("silent"),
										punishDoc.getLong("issued"),
										punishDoc.getLong("expire"));
								
								loadedPunishment.put(uuid, punishment);
								break loop;
							}
							break;
						default:
							break;
						}
					}
				} catch (MongoException e) {
					event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ConfigurationService.SQL_ERROR_LOADING_DATA);
					e.printStackTrace();
				}
			}
			
			if (punishment != null)
				event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, punishment.errorMessage());
		} else {
			for (Punishment punishment : offline.getActivePunishments()) {
				if (punishment instanceof ServerRestricted && punishment.isActive())
					event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, punishment.errorMessage());
			}
		}
	}
	
	@EventHandler
	public void onPrivateMessage(PrivateMessageEvent event) {
		CommandSender sender = event.getSender();
		
		if (!(sender instanceof Player))
			return;
		
		for (Punishment punishment : APIPlayer.getPlayer(((Player) sender)).getActivePunishments()) {
			if (punishment instanceof Mute && punishment.isActive()) {
				sender.sendMessage(punishment.errorMessage());
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onChat(ChatEvent event) {
		CommandSender sender = event.getSender();
		
		if (!(sender instanceof Player))
			return;
		
		APIPlayer player = APIPlayer.getPlayer((Player) sender);
		
		for (Punishment punishment : player.getActivePunishments()) {
			if (punishment instanceof Mute && punishment.isActive()) {
				player.sendMessage(punishment.errorMessage());
				event.setCancelled(true);
				return;
			}
		}
	}
	
}
