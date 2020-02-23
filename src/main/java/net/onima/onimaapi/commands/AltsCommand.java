package net.onima.onimaapi.commands;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Filters.or;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.mongo.OnimaMongo;
import net.onima.onimaapi.mongo.OnimaMongo.OnimaCollection;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.punishment.Ban;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;

public class AltsCommand implements CommandExecutor {
	
	private JSONMessage usage = new JSONMessage("§7/alts <player>", "§d§oAffiche les alts d'un joueur.");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ALTS_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (args.length < 1) {
			usage.send(sender, "§7Utilisation : ");
			return false;
		}
		
		UUID uuid = UUIDCache.getUUID(args[0]);
		
		if (uuid == null) {
			sender.sendMessage("§c" + args[0] + " ne s'est jamais connecté sur le serveur !");
			return false;
		}
		
		if (OfflineAPIPlayer.isLoaded(uuid)) {
			OfflineAPIPlayer offline = OfflineAPIPlayer.getOfflineAPIPlayers().get(uuid);
			OnimaMongo.executeAsync(OnimaCollection.PLAYERS, collection -> sendAlts(sender, offline.getIP(), offline.getAlts(), collection));
		} else {
			Bukkit.getScheduler().runTaskAsynchronously(OnimaAPI.getInstance(), () -> {
				Document document = OnimaMongo.get(OnimaCollection.PLAYERS).find(Filters.eq("uuid", uuid)).first();
				
				sendAlts(sender, document.getString("ip"), document.getList("alts", String.class).stream()
						.map(UUIDCache::getUUID).collect(Collectors.toList()), OnimaMongo.get(OnimaCollection.PLAYERS));
			});
		}
		
		return true;
	}

	private void sendAlts(CommandSender sender, String ip, List<UUID> alts, MongoCollection<Document> collection) {
		StringBuilder builder = new StringBuilder();
		long now = System.currentTimeMillis();
		Iterator<UUID> iterator = alts.iterator();
		
		while (iterator.hasNext()) {
			UUID uuid = iterator.next();
			OfflinePlayer offline = Bukkit.getOfflinePlayer(uuid);
			
			if (offline.isOnline()) 
				builder.append("§f[§a" + offline.getName() + "§f]");
			else if (collection.find(and(or(eq("uuid", uuid.toString()), eq("ip", ip)),
					in("punishments.punishment_type", "BAN", "TEMPBAN", "BLACKLIST"),
					or(gt("punishments.expire", now), eq("punishments.expire", Ban.CANT_EXPIRE_TIME)),
					eq("punishments.remover_uuid", null))).first() != null) {
				builder.append("§f[§c" + offline.getName() + "§f]");
			} else
				builder.append("§f[§7" + offline.getName() + "§f]");
			
			if (iterator.hasNext())
				builder.append(", ");
		}
		if (builder.length() <= 0) {
			sender.sendMessage("§cAucun compte alternatifs !");
			return;
		}
			
		sender.sendMessage("§f[§aConnecté§f], §f[§cBanni§f], §f[§7Déconnecté§f]");
		sender.sendMessage(builder.toString());
	}
	
}
