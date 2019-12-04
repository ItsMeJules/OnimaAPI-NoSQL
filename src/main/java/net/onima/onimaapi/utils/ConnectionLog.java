package net.onima.onimaapi.utils;

import java.util.UUID;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import net.onima.onimaapi.mongo.OnimaMongo;
import net.onima.onimaapi.mongo.OnimaMongo.OnimaCollection;

public class ConnectionLog {
	
	private UUID uuid;
	private String ipAddress;
	private AsyncPlayerPreLoginEvent.Result result;
	private String resultReason;
	private long time;
	
	public ConnectionLog(UUID uuid, String ipAddress, Result result, String resultReason, long time) {
		this.uuid = uuid;
		this.ipAddress = ipAddress;
		this.result = result;
		this.resultReason = resultReason;
		this.time = time;
	}

	public UUID getUuid() {
		return uuid;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	
	public AsyncPlayerPreLoginEvent.Result getResult() {
		return result;
	}
	
	public String getResultReason() {
		return resultReason;
	}
	
	public long getTime() {
		return time;
	}

	public void sendToDatabase() {
		if (Bukkit.isPrimaryThread())
			throw new RuntimeException("Impossible d'insérer des données sur le thread principal !");
		
		OnimaMongo.get(OnimaCollection.CONNECTION_LOGS).insertOne(getDocument());
	}
	
	public Document getDocument(Object... objects) {
		return new Document("uuid", uuid.toString())
				.append("ip", ipAddress).append("connection_result", result.name())
				.append("result_reason", resultReason).append("time", time);
	}

}
