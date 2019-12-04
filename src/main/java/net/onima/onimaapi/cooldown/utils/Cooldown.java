package net.onima.onimaapi.cooldown.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import com.google.common.base.Predicate;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.cooldown.GodAppleCooldown;
import net.onima.onimaapi.event.CooldownEndEvent;
import net.onima.onimaapi.event.CooldownExtendEvent;
import net.onima.onimaapi.event.CooldownStartEvent;
import net.onima.onimaapi.event.CooldownStopEvent;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.saver.Saver;
import net.onima.onimaapi.saver.mongo.MongoSerializer;

public abstract class Cooldown implements Saver, MongoSerializer {
	
	protected static List<Cooldown> cooldowns;
	
	static {
		cooldowns = new ArrayList<>();
		
		register(new GodAppleCooldown());
	}
	
	protected String name;
	protected byte id; //Last id = 16 -> InsideStormCooldown
	protected long duration;
	protected Map<UUID, CooldownData> datas;
	
	{
		datas = new HashMap<>();
	}
	
	public Cooldown(String name, byte id, long duration) {
		this.name = name;
		this.id = id;
		this.duration = duration;
	}
	
	public abstract String scoreboardDisplay(long timeLeft);
	public abstract boolean action(OfflineAPIPlayer offline);
	
	public <T> boolean isApplicable(T t, Predicate<T> predicate) {
		return predicate.apply(t);
	}
	
	public void onStart(OfflineAPIPlayer offline) {
		onStart(offline, duration);
	}
	
	public void onStart(OfflineAPIPlayer offline, long time) {
		CooldownData data = null;
		
		if ((data = datas.putIfAbsent(offline.getUUID(), new CooldownData(this, offline.getUUID(), time))) != null) {
			Bukkit.getPluginManager().callEvent(new CooldownExtendEvent(this, offline, data.getTimeLeft()));
			data.asNew();
		} else {
			offline.getCooldownsById().add(id);
			Bukkit.getPluginManager().callEvent(new CooldownStartEvent(this, offline));
		}
	}
	
	public void onExpire(OfflineAPIPlayer offline) {
		if (datas.remove(offline.getUUID()) != null) {
			offline.getCooldownsById().remove(Byte.valueOf(id));
			Bukkit.getPluginManager().callEvent(new CooldownEndEvent(this, offline));
		}
	}
	
	public void onCancel(OfflineAPIPlayer offline) {
		if (datas.remove(offline.getUUID()) != null) {
			offline.getCooldownsById().remove(Byte.valueOf(id));
			Bukkit.getPluginManager().callEvent(new CooldownStopEvent(this, offline));
		}
	}
	
	public void startPause(OfflineAPIPlayer offline) {
		CooldownData data = null;
		
		if ((data = datas.putIfAbsent(offline.getUUID(), new CooldownData(this, offline.getUUID()))) == null)
			Bukkit.getPluginManager().callEvent(new CooldownStartEvent(this, offline));
		
		data.pause(true);
	}
	
	public void stopPause(OfflineAPIPlayer offline) {
		CooldownData data = null;
		
		if ((data = getData(offline.getUUID())) == null)
			return;
		
		data.pause(false);
	}
	
	public String getName() {
		return name;
	}
	
	public byte getId() {
		return id;
	}
	
	public long getDuration() {
		return duration;
	}
	
	public CooldownData getData(UUID uuid) {
		return datas.get(uuid);
	}
	
	public Map<UUID, CooldownData> getDatas() {
		return datas;
	}
	
	public long getTimeLeft(UUID uuid) {
		CooldownData data = getData(uuid);
		return data == null ? 0L : data.getTimeLeft();
	}
	
	public boolean isPaused(UUID uuid) {
		CooldownData data = getData(uuid);
		return data != null && data.isPaused();
	}
	 
	@Override
	public boolean isSaved() {
		return cooldowns.contains(this);
	}
	
	@Override
	public void remove() {
		cooldowns.remove(this);
	}
	
	@Override
	public void save() {
		cooldowns.add(this);
	}
	
	@Override
	public Document getDocument(Object... objects) {
		return new Document("id", id).append("time_left", (id != 5 ? System.currentTimeMillis() : 0)  + getTimeLeft((UUID) objects[0]));
	}
	
	public static void register(Cooldown cooldown) {
		cooldown.save();
		
		if (cooldown instanceof Listener)
			Bukkit.getPluginManager().registerEvents((Listener) cooldown, OnimaAPI.getInstance());
	}

	public static Cooldown getCooldown(Class<? extends Cooldown> cooldown) {
		return cooldowns.stream().filter(e -> e.getClass() == cooldown).findFirst().orElse(null);
	}
	
	public static Cooldown getCooldown(byte id) {
		return cooldowns.stream().filter(cooldown -> cooldown.id == id).findFirst().orElse(null);
	}
	
	public static Cooldown getCooldown(String name) {
		return cooldowns.stream().filter(cooldown -> cooldown.name.equalsIgnoreCase(name)).findFirst().orElse(null);
	}
	
	public static List<Cooldown> getCooldowns() {
		return cooldowns;
	}

}
