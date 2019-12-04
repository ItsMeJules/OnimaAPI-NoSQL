package net.onima.onimaapi.mongo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.bukkit.Bukkit;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.mongo.api.MongoAccessor;
import net.onima.onimaapi.utils.callbacks.VoidCallback;

public class OnimaMongo {
	
	private static MongoClient client;
	private static MongoDatabase database;
	private static Map<OnimaCollection, MongoCollection<Document>> collections;
	
	public static void connect() {
		client = MongoClients.create("mongodb+srv://Jules:WBxLEMpzzn4i6oj6@onimacluster-7wakg.gcp.mongodb.net/test?retryWrites=true&w=majority");
		database = client.getDatabase("onima");

		collections = new HashMap<>();
		
		collections.put(OnimaCollection.PLAYERS, database.getCollection("players"));
		collections.put(OnimaCollection.CONNECTION_LOGS, database.getCollection("connection_logs"));
		collections.put(OnimaCollection.DISGUISE_LOGS, database.getCollection("disguise_logs"));
		collections.put(OnimaCollection.FACTIONS, database.getCollection("factions"));
		collections.put(OnimaCollection.PLAYER_FACTIONS, database.getCollection("player_factions"));
	}
	
	public static void disconnect() {
		client.close();
	}
	
	public static <T> void executeAsync(OnimaCollection collection, VoidCallback<MongoCollection<Document>> callback) {
		executeAsync(collection, callback, false);
	}
		
	public static <T> void executeAsync(OnimaCollection collection, VoidCallback<MongoCollection<Document>> callback, boolean sync) {
		Bukkit.getScheduler().runTaskAsynchronously(OnimaAPI.getInstance(), () -> {
			if (sync)
				Bukkit.getScheduler().runTask(OnimaAPI.getInstance(), () -> callback.call(collections.get(collection)));
			else
				callback.call(collections.get(collection));
		});
	}
	
	public static void executeAsync(MongoAccessor accessor, boolean sync) {
		try {
			Bukkit.getScheduler().runTaskAsynchronously(OnimaAPI.getInstance(), () -> {
				if (sync)
					Bukkit.getScheduler().runTask(OnimaAPI.getInstance(), () -> accessor.execute());
				else
					accessor.execute();
			});
		} catch (MongoException e) {
			e.printStackTrace();
			accessor.getResult().setFailed(true);
		}
	}
	
	public static MongoCollection<Document> get(OnimaCollection collection) {
		return collections.get(collection);
	}
	
	public static Document getPlayer(UUID uuid) {
		return get(OnimaCollection.PLAYERS).find(Filters.eq("uuid", uuid.toString())).first();
	}
	
	public static void close() {
		if (client != null)
			client.close();
	}
	
	public static MongoDatabase getDB() {
		return database;
	}
	
	public enum OnimaCollection {
		PLAYERS,
		CONNECTION_LOGS,
		FACTIONS,
		PLAYER_FACTIONS,
		DISGUISE_LOGS;
	}
	
}
