package net.onima.onimaapi.players;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.cooldown.utils.Cooldown;
import net.onima.onimaapi.cooldown.utils.CooldownData;
import net.onima.onimaapi.crates.booster.NoBooster;
import net.onima.onimaapi.crates.booster.PrizeRarityBooster;
import net.onima.onimaapi.crates.openers.VirtualKey;
import net.onima.onimaapi.event.mongo.AbstractPlayerLoadEvent;
import net.onima.onimaapi.event.ranks.RankReceivedEvent;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.menu.DeathHistoricMenu;
import net.onima.onimaapi.gui.menu.OresMenu;
import net.onima.onimaapi.gui.menu.PendingRestoresMenu;
import net.onima.onimaapi.gui.menu.VirtualKeysMenu;
import net.onima.onimaapi.listener.PunishmentListener;
import net.onima.onimaapi.mongo.OnimaMongo;
import net.onima.onimaapi.mongo.OnimaMongo.OnimaCollection;
import net.onima.onimaapi.mongo.api.result.MongoQueryResult;
import net.onima.onimaapi.players.notes.Note;
import net.onima.onimaapi.players.notes.NotePriority;
import net.onima.onimaapi.players.utils.MinedOres;
import net.onima.onimaapi.players.utils.PlayTime;
import net.onima.onimaapi.players.utils.PlayerOption;
import net.onima.onimaapi.players.utils.RestoreRequest;
import net.onima.onimaapi.punishment.BlackList;
import net.onima.onimaapi.punishment.utils.Punishment;
import net.onima.onimaapi.punishment.utils.PunishmentType;
import net.onima.onimaapi.punishment.utils.ServerRestricted;
import net.onima.onimaapi.rank.Rank;
import net.onima.onimaapi.rank.RankType;
import net.onima.onimaapi.saver.inventory.PlayerSaver;
import net.onima.onimaapi.saver.mongo.NoSQLSaver;
import net.onima.onimaapi.tasks.CooldownEntryTask;
import net.onima.onimaapi.utils.Balance;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.Options;
import net.onima.onimaapi.utils.callbacks.VoidCallback;

public class OfflineAPIPlayer implements NoSQLSaver {
	
	private static Map<UUID, OfflineAPIPlayer> offlinePlayers;
	
	static {
		offlinePlayers = new HashMap<>();
	}

	protected OfflinePlayer offlinePlayer;
	protected UUID uuid;
	protected String name, ip;
	protected List<UUID> alts;
	protected int kills, deaths;
	protected Balance balance;
	protected List<PacketMenu> menus;
	protected MinedOres minedOres;
	protected Options options;
	protected PlayTime playTime;
	protected boolean vanish, modMode, loggerDead, frozen;
	protected Rank rank;
	protected List<PlayerSaver> playerDataSaved;
	protected List<Byte> cooldowns;
	protected Map<String, VirtualKey> virtualKeys;
	protected List<RestoreRequest> restoreRequests;
	protected List<Punishment> punishments;
	protected List<Note> notes;
	protected List<String> ipHistory;
	
	{
		alts = new ArrayList<>();
		balance = new Balance(ConfigurationService.DEFAULT_BALANCE);
		menus = new ArrayList<>();
		options = new Options();
		playTime = new PlayTime(0L, 0L);
		rank = new Rank(this, RankType.DEFAULT);
		playerDataSaved = new ArrayList<>();
		cooldowns = new ArrayList<>();
		virtualKeys = new HashMap<>();
		restoreRequests = new ArrayList<>();
		punishments = new ArrayList<>();
		notes = new ArrayList<>();
		ipHistory = new ArrayList<>();
	}

	public OfflineAPIPlayer(OfflinePlayer offlinePlayer) {
		this.offlinePlayer = offlinePlayer;
		
		uuid = offlinePlayer.getUniqueId();
		name = offlinePlayer.getName();
		
		if (this instanceof APIPlayer)
			transferInstance(uuid);
		
		menus.add(new VirtualKeysMenu(this));
		menus.add(new DeathHistoricMenu(this));
		menus.add(new PendingRestoresMenu(this));
		
		CooldownEntryTask.get().insert(this);
		
		(minedOres = new MinedOres()).setMenu(new OresMenu(this));
		
		offlinePlayers.put(uuid, this);
	}
	
	public OfflineAPIPlayer(UUID uuid) {
		this.uuid = uuid;		
		this.offlinePlayer = Bukkit.getOfflinePlayer(uuid);
		
		if (this instanceof APIPlayer)
			transferInstance(uuid);

		name = offlinePlayer.getName();
		
		offlinePlayers.put(uuid, this);
	}
	
	private void transferInstance(UUID uuid) {
		getPlayer(uuid, (old) -> {
			if (old == null)
				return;
			
			ip = old.ip;
			alts = old.alts;
			kills = old.kills;
			deaths = old.deaths;
			balance = old.balance;
			minedOres = old.minedOres;
			options = old.options;
			playTime = old.playTime;
			vanish = old.vanish;
			modMode = old.modMode;
			frozen = old.frozen;
			playerDataSaved = old.playerDataSaved;
			cooldowns = old.cooldowns;
			virtualKeys = old.virtualKeys;
			restoreRequests = old.restoreRequests;
			punishments = old.punishments;
			
			if (rank != old.rank) {
				rank = old.rank;
				Bukkit.getPluginManager().callEvent(new RankReceivedEvent(rank, (APIPlayer) this, false, null));
			}
		});
	}
	
	public String getName() {
		return name;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public boolean isOnline() {
		return offlinePlayer.isOnline();
	}
	
	public OfflinePlayer getOfflinePlayer() {
		return offlinePlayer;
	}
	
	/**
	 * This method returns the OfflineProfile's IP.
	 * 
	 * @return The OfflineProfile's IP.
	 */
	public String getIP() {
		return ip;
	}
	
	/**
	 * This method sets the ip for the OfflineProfile.
	 * 
	 * @param ip - The ip as a string.
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * This method is getting all this OfflineProfile's alts.
	 * 
	 * @return A list with the alts uuid.
	 */
	public List<UUID> getAlts() {
		return alts;
	}
	
	/**
	 * This method returns the amount of kills this profile has.
	 * 
	 * @return The amount of kills this profile has.
	 */
	public int getKills() {
		return kills;
	}
	
	/**
	 * This method sets the amount of kills for this profile.
	 * 
	 * @param kills - Kills to set.
	 */
	public void setKills(int kills) {
		this.kills = kills;
	}
	
	/**
	 * This method adds a kill to this profile.
	 */
	public void addKill() {
		kills++;
	}
	
	/**
	 * This method returns the amount of deaths this profile has.
	 * 
	 * @return The amount of deaths this profile has.
	 */
	public int getDeaths() {
		return deaths;
	}
	
	/**
	 * This method sets the amount of deaths for this profile.
	 * 
	 * @param deaths - Deaths to set.
	 */
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
	
	/**
	 * This method adds a death to this profile.
	 */
	public void addDeath() {
		deaths++;
	}
	
	/**
	 * This method returns the balance instance related to this offline profile.<br>
	 * Check {@link Balance}
	 * 
	 * @return The balance instance related to this profile.
	 */
	public Balance getBalance() {
		return balance;
	}
	
	/**
	 * This method sets the balance for this offline profile.
	 * 
	 * @param balance - Balance instance to set.
	 */
	public void setBalance(Balance balance) {
		this.balance = balance;
	}
	
	public MinedOres getMinedOres() {
		return minedOres;
	}
	
	public String getColoredName(boolean realName) {
		return rank.getRankType().getNameColor() + name;
	}
	
	public String getRankName(boolean realName) {
		return rank.getRankType().getPrefix() + ' ' + getColoredName(realName);
	}
	
	public PacketMenu getMenu(String id) {
		if (id == null || id.isEmpty()) return null;
		
		for (PacketMenu menu : menus) {
			if (menu.getId().equalsIgnoreCase(id))
				return menu;
		}
		return null;
	}
	
	public List<PacketMenu> getMenus() {
		return menus;
	}
	
	public Options getOptions() {
		return options;
	}
	
	public PlayTime getPlayTime() {
		return playTime;
	}
	
	public boolean isVanished() {
		return vanish;
	}
	
	public void setVanish(boolean vanish) {
		this.vanish = vanish;
	}
	
	public boolean isInModMode() {
		return modMode;
	}
	
	public void setModMode(boolean modMode) {
		this.modMode = modMode;
	}
	
	public boolean isLoggerDead() {
		return loggerDead;
	}
	
	public void setLoggerDead(boolean loggerDead) {
		this.loggerDead = loggerDead;
	}
	
	public boolean isFrozen() {
		return frozen;
	}
	
	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}
	
	public void setRank(Rank rank) {
		this.rank = rank;
	}
	
	public Rank getRank() {
		return rank;
	}
	 
	public List<PlayerSaver> getPlayerDataSaved() {
		return playerDataSaved;
	}	
	
	public void startCooldown(Class<? extends Cooldown> cooldown) {
		startCooldown(Cooldown.getCooldown(cooldown));
	}
	
	public void startCooldown(Class<? extends Cooldown> cooldown, long time) {
		startCooldown(Cooldown.getCooldown(cooldown), time);
	}
	
	public void startCooldown(Cooldown cooldown) {
		startCooldown(cooldown, cooldown.getDuration());
	}
	
	public void startCooldown(Cooldown cooldown, long time) {
		cooldown.onStart(this, time);
	}
	
	public void removeCooldown(Class<? extends Cooldown> cooldown) {
		removeCooldown(Cooldown.getCooldown(cooldown));
	}
	
	public void removeCooldown(Cooldown cooldown) {
		cooldown.onCancel(this);
	}
	
	public long getTimeLeft(Class<? extends Cooldown> cooldown) {
		return getTimeLeft(Cooldown.getCooldown(cooldown));
	}
	
	public long getTimeLeft(Cooldown cooldown) {
		return cooldown.getTimeLeft(uuid);
	}
	
	public List<Byte> getCooldownsById() {
		return cooldowns;
	}
	
	public List<Cooldown> getCooldowns() {
		return cooldowns.stream().map(Cooldown::getCooldown).collect(Collectors.toCollection(() -> new ArrayList<>(cooldowns.size())));
	}
	
	public Map<String, VirtualKey> getVirtualKeys() {
		return virtualKeys;
	}
	
	public void addVirtualKey(VirtualKey virtualKey) {
		boolean insert = true;
		String crateName = virtualKey.getCrateName();
		
		if (virtualKeys.containsKey(crateName)) {
			VirtualKey key = virtualKeys.get(crateName);
			
			if (key.getBooster().equals(virtualKey.getBooster())) {
				key.add();
				insert = false;
			}
		}
		
		if (insert)
			virtualKeys.put(crateName, virtualKey);
	}
	
	public void removeVirtualKey(VirtualKey virtualKey) {
		boolean remove = true;
		String crateName = virtualKey.getCrateName();
		
		if (virtualKeys.containsKey(crateName)) {
			VirtualKey key = virtualKeys.get(crateName);
			
			if (key.getBooster().equals(virtualKey.getBooster())) {
				key.remove();
				remove = false;
			}
		}
		
		if (remove)
			virtualKeys.remove(crateName);
	}
	
	public List<RestoreRequest> getRestoreRequests() {
		return restoreRequests;
	}
	
	public List<Punishment> getPunishments() {
		return punishments;
	}
	
	public List<Punishment> getActivePunishments() {
		return punishments.stream().filter(punishment -> punishment.isActive() && !punishment.isRemoved()).collect(Collectors.toList());
	}
	
	public boolean hasPunishment(PunishmentType type) {
		for (Punishment punishment : getActivePunishments())
			return punishment.getType() == type;
		
		return false;
	}
	
	public void lookupAlts() {
		OnimaMongo.executeAsync(OnimaCollection.PLAYERS, collection -> {
			try (MongoCursor<Document> cursor = collection.find(Filters.eq("ip", ip)).iterator()) {
				
				cursor.forEachRemaining(document -> {
					UUID uuid = UUID.fromString(document.getString("uuid"));
					List<String> docAlts = document.getList("alts", String.class);
					
					if (!uuid.equals(this.uuid)) {
						if (!alts.contains(uuid))
							alts.add(uuid);
						
						if (OfflineAPIPlayer.isLoaded(uuid))
							OfflineAPIPlayer.offlinePlayers.get(uuid).getAlts().add(this.uuid);
						else {
							if (!docAlts.contains(this.uuid.toString())) {
								docAlts.add(this.uuid.toString());
								collection.updateOne(Filters.eq("uuid", uuid.toString()), Updates.set("alts", docAlts));
							}
						}
					}
				});
				
			}
		});
	}
	
	public List<Note> getNotes() {
		return notes;
	}
	
	public List<String> getIpHistory() {
		return ipHistory;
	}
	
	@Override
	public void save() {
		offlinePlayers.put(uuid, this);
	}

	@Override
	public void remove() {
		offlinePlayers.remove(uuid);
		CooldownEntryTask.get().remove(this);
		Methods.initKills(this);
	}

	@Override
	public boolean isSaved() {
		return offlinePlayers.containsKey(uuid);
	}
	
	@Override
	public void queryDatabase(MongoQueryResult result) {
		Document document = result.getValue("player", Document.class);
		Document balanceDoc = result.getValue("balance", Document.class);
		Document oresDoc = result.getValue("ores", Document.class);
		Document rankDoc = result.getValue("rank", Document.class);
		Document optionsDoc = result.getValue("options", Document.class);
		
		kills = document.getInteger("kills");
		deaths = document.getInteger("deaths");
		ip = document.getString("ip");
		playTime = new PlayTime(System.currentTimeMillis(), ((Number) document.get("play_time")).longValue()); //MongoDB stores it as an Integer whenever it can.
		vanish = document.getBoolean("vanish");
		modMode = document.getBoolean("mod_mode");
		loggerDead = document.getBoolean("logger_dead");
		frozen = document.getBoolean("frozen");
		(balance = new Balance(balanceDoc.getDouble("amount"))).setBan(balanceDoc.getBoolean("banned"));
		(minedOres = MinedOres.fromInts(oresDoc.getInteger("diamonds"),
				oresDoc.getInteger("emeralds"), 
				oresDoc.getInteger("golds"),
				oresDoc.getInteger("lapis"),
				oresDoc.getInteger("redstones"),
				oresDoc.getInteger("irons"),
				oresDoc.getInteger("coals"),
				oresDoc.getInteger("quartzs"))).setMenu(new OresMenu(this));
		rank = new Rank(this, RankType.valueOf(rankDoc.getString("rank_type")), document.getLong("expire"));
		ipHistory = document.getList("ip_history", String.class);
		
		options.getSettings().put(PlayerOption.ModOptions.PICKUP_ITEM, optionsDoc.getBoolean("pickup_item"));
		options.getSettings().put(PlayerOption.ModOptions.DROP_ITEM, optionsDoc.getBoolean("drop_item"));
		options.getSettings().put(PlayerOption.ModOptions.BREAK_BLOCK, optionsDoc.getBoolean("break_block"));
		options.getSettings().put(PlayerOption.ModOptions.PLACE_BLOCK, optionsDoc.getBoolean("place_block"));
		options.getSettings().put(PlayerOption.ModOptions.CHAT_MESSAGE, optionsDoc.getBoolean("chat_message"));
		options.getSettings().put(PlayerOption.ModOptions.ATTACK_PLAYER, optionsDoc.getBoolean("attack_player"));
		options.getSettings().put(PlayerOption.ModOptions.TELEPORT_LAYER, optionsDoc.getInteger("teleport_layer"));
		options.getSettings().put(PlayerOption.ModOptions.ENTITY_BOOSTER, optionsDoc.get("entity_booster", Double.class));
		options.getSettings().put(PlayerOption.ModOptions.ENTITY_BOOSTER, optionsDoc.get("player_booster", Double.class));
		options.getSettings().put(PlayerOption.ModOptions.HIDE_PLAYER_TIME, optionsDoc.getInteger("hide_player_time"));
		options.getSettings().put(PlayerOption.ModOptions.SILENT_CHEST, optionsDoc.getBoolean("silent_chest"));
		
		options.getSettings().put(PlayerOption.GlobalOptions.PRIVATE_MESSAGE, optionsDoc.getBoolean("private_message"));
		options.getSettings().put(PlayerOption.GlobalOptions.SOCIAL_SPY, optionsDoc.getBoolean("social_spy"));
		options.getSettings().put(PlayerOption.GlobalOptions.SOUNDS, optionsDoc.getBoolean("sounds"));
		options.getSettings().put(PlayerOption.GlobalOptions.COBBLE_DROP, optionsDoc.getBoolean("cobble_drop"));
		options.getSettings().put(PlayerOption.GlobalOptions.DEATH_MESSAGES, optionsDoc.getBoolean("death_messages"));
		options.getSettings().put(PlayerOption.GlobalOptions.FOUND_DIAMONDS, optionsDoc.getBoolean("found_diamond"));
		
		for (Document doc : result.valueToList("player_data", Document.class)) {
			playerDataSaved.add(PlayerSaver.fromDB(doc.getInteger("id"),
					doc.getLong("saved_time"),
					doc.getString("items"),
					doc.getString("armor"),
					doc.getString("effects"),
					doc.getInteger("xp"),
					doc.getInteger("fire_ticks"),
					doc.getInteger("food_level"),
					doc.getDouble("health"),
					((Number) doc.get("saturation")).floatValue(), //I have to because mongo doesn't know about floats.
					PlayerSaver.SaveType.valueOf(doc.getString("saved_type")),
					doc.getBoolean("restore_request"),
					doc.getString("saver_message")));
		}
		
		long now = System.currentTimeMillis();
		
		for (Document doc : result.valueToList("cooldowns", Document.class)) {
			int id = doc.getInteger("id");
			long time = doc.getLong("time_left");
			
			Cooldown cooldown = Cooldown.getCooldown((byte) id);
			CooldownData data = new CooldownData(cooldown, uuid);
				
			data.setTimeLeft(id == 5 ? time : time - now);
			cooldown.getDatas().put(uuid, data);
			cooldowns.add((byte) id);
		}
		
		for (Document doc : result.valueToList("punishments", Document.class)) {
			 UUID removerUUID = null;
             String removerStringUID = doc.getString("remover_uuid");

             if (removerStringUID != null)
                removerUUID = UUID.fromString(removerStringUID);

             Punishment punishment = Punishment.fromDB(doc.getInteger("id"),
            		 PunishmentType.valueOf(doc.getString("punishment_type")),
            		 UUID.fromString(doc.getString("sender_uuid")),
            		 uuid,
            		 removerUUID,
            		 doc.getString("reason"),
            		 doc.getString("remove_reason"),
            		 doc.getBoolean("silent"),
            		 doc.getLong("issued"),
            		 ((Number) doc.get("expire")).longValue());
             
             if (punishment instanceof BlackList)
            	 ((BlackList) punishment).setIP(ip);
             
             if (punishment instanceof ServerRestricted && PunishmentListener.loadedPunishment.containsKey(uuid))
            	 punishment = PunishmentListener.loadedPunishment.remove(uuid);
             
             punishments.add(punishment);
		}
		
		for (Document doc : result.valueToList("restore_request", Document.class)) {
			restoreRequests.add(new RestoreRequest(UUID.fromString(doc.getString("restorer_uuid")),
					uuid,
					doc.getInteger("saver_id"),
					doc.getString("reason")));
		}
		
		for (Document doc : result.valueToList("virtual_keys", Document.class)) {
			 String boosterStr = doc.getString("booster");
			 String keyName = doc.getString("crate_name");
			 
             VirtualKey virtualKey = new VirtualKey(keyName, boosterStr.equals("") ? new NoBooster() : PrizeRarityBooster.fromString(boosterStr));
			 
             virtualKey.setAmount(doc.getInteger("amount"));
             virtualKeys.put(keyName, virtualKey);
		}
		
		for (Document doc : result.valueToList("notes", Document.class)) {
			Note note = new Note(UUID.fromString(doc.getString("sender")), doc.getString("note"), NotePriority.valueOf(doc.getString("priority")));
			
			note.setTime(((Number) doc.get("created")).longValue());
			note.setExpire(((Number) doc.get("expire")).longValue());
			
			if (note.isExpired())
				continue;
			
			notes.add(note);
		}
	}

	@Deprecated
	@Override
	public Document getDocument(Object... objects) {return null;}
	
//	public static void load(UUID uuid) {
//		if (!offlinePlayers.containsKey(uuid))
//			Bukkit.getPluginManager().callEvent(new PlayerLoadEvent(uuid));
//	}
	
	public static void getPlayer(UUID uuid, VoidCallback<OfflineAPIPlayer> callback) {
		if (!offlinePlayers.containsKey(uuid)) {
			AbstractPlayerLoadEvent event = new AbstractPlayerLoadEvent(uuid) {
				
				@Override
				public void done() {
					callback.call(offlinePlayers.get(uuid));
				}
			};
			
			Bukkit.getPluginManager().callEvent(event);
		} else
			callback.call(offlinePlayers.get(uuid));
	}
	
	public static void getPlayer(String name, VoidCallback<OfflineAPIPlayer> callback) {
		OfflinePlayer offline = Bukkit.getOfflinePlayer(UUIDCache.getUUID(name));

		if (offline.hasPlayedBefore())
			getPlayer(offline.getUniqueId(), callback);
	}
	
	public static void getPlayer(OfflinePlayer offline, VoidCallback<OfflineAPIPlayer> callback) {
		getPlayer(offline.getUniqueId(), callback);
	}
	
	public static Map<UUID, OfflineAPIPlayer> getOfflineAPIPlayers() {
		return offlinePlayers;
	}
	
	public static Collection<OfflineAPIPlayer> getDisconnectedOfflineAPIPlayers() {
		return offlinePlayers.values();
	}

	public static boolean isLoaded(UUID uuid) {
		return offlinePlayers.containsKey(uuid);
	}
	
}
