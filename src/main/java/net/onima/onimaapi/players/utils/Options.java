package net.onima.onimaapi.players.utils;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import net.onima.onimaapi.mongo.saver.MongoSerializer;
import net.onima.onimaapi.players.utils.PlayerOption.GlobalOptions;
import net.onima.onimaapi.players.utils.PlayerOption.ModOptions;

public class Options implements MongoSerializer {
	
	private static Map<String, PlayerOption> transformer = new HashMap<>();
	private static Map<PlayerOption, Object> defaultSettings = new HashMap<>();
	
	private Map<PlayerOption, Object> settings = new HashMap<>();
	
	public static void register(PlayerOption playerOption, Object value) {
		transformer.put(playerOption.name(), playerOption);
		defaultSettings.put(playerOption, value);
	}
	
	public static PlayerOption transform(String name) {
		return transformer.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(PlayerOption playerOption) {
		if (settings.containsKey(playerOption)) {
			return (T) settings.get(playerOption);
		} else {
			settings.put(playerOption, defaultSettings.get(playerOption));
			return (T) defaultSettings.get(playerOption);
		}
	}
	
	public boolean reverseBoolean(PlayerOption playerOption) {
		boolean result = !getBoolean(playerOption);
		
		settings.put(playerOption, result);
		return result;
	}
	
	public Map<PlayerOption, Object> getSettings() {
		return settings;
	}
	
	public boolean getBoolean(PlayerOption playerOption) {
		return (boolean) get(playerOption);
	}
	
	public double getDouble(PlayerOption playerOption) {
		return (double) get(playerOption);
	}
	
	public int getInteger(PlayerOption playerOption) {
		return (int) get(playerOption);
	}
	
	@Override
	public Document getDocument(Object... objects) {
		return new Document("pickup_item", getBoolean(ModOptions.PICKUP_ITEM))
				.append("drop_item", getBoolean(ModOptions.DROP_ITEM)).append("break_block", getBoolean(ModOptions.BREAK_BLOCK))
				.append("place_block", getBoolean(ModOptions.PLACE_BLOCK)).append("chat_message", getBoolean(ModOptions.CHAT_MESSAGE))
				.append("attack_player", getBoolean(ModOptions.ATTACK_PLAYER)).append("player_booster", get(ModOptions.PLAYER_BOOSTER))
				.append("teleport_layer", get(ModOptions.TELEPORT_LAYER)).append("entity_booster", get(ModOptions.ENTITY_BOOSTER))
				.append("hide_player_time", get(ModOptions.HIDE_PLAYER_TIME)).append("silent_chest", getBoolean(ModOptions.SILENT_CHEST))
				.append("private_message", getBoolean(GlobalOptions.PRIVATE_MESSAGE)).append("social_spy", getBoolean(GlobalOptions.SOCIAL_SPY))
				.append("sounds", getBoolean(GlobalOptions.SOUNDS)).append("cobble_drop", getBoolean(GlobalOptions.COBBLE_DROP))
				.append("death_messages", getBoolean(GlobalOptions.DEATH_MESSAGES)).append("found_diamond", getBoolean(GlobalOptions.FOUND_DIAMONDS))
				.append("note_notify_connect", getBoolean(GlobalOptions.IMPORTANT_NOTE_NOTIFY_CONNECT))
				.append("show_players_spawn", getBoolean(GlobalOptions.SHOW_PLAYERS_WHEN_IN_SPAWN))
				.append("show_invisible_players", getBoolean(GlobalOptions.SHOW_INVISIBLE_PLAYERS))
				.append("capzone_messages", getBoolean(GlobalOptions.CAPZONE_MESSAGES));
	}
	
}

