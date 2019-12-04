package net.onima.onimaapi.disguise;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.event.disguise.PlayerDisguiseEvent;
import net.onima.onimaapi.event.disguise.PlayerUndisguiseEvent;
import net.onima.onimaapi.mongo.OnimaMongo;
import net.onima.onimaapi.mongo.OnimaMongo.OnimaCollection;
import net.onima.onimaapi.rank.RankType;

public class DisguiseManager {
	
	private static Map<String, String> disguisedPlayers;
	
	static {
		disguisedPlayers = new HashMap<>();
	}

	private String playerName;
	private DisguiseSkin skin;
	private UUID disguisedUUID;
	private String name;
	private RankType rankType;
	
	public DisguiseManager(String playerName) {
		this.playerName = playerName;
	}

	public void disguise(UUID disguisedUUID, String name, RankType rankType) {
		Player player = Bukkit.getPlayer(playerName);
		
		if (name != null) {
			if (disguisedPlayers.containsKey(this.name))
				disguisedPlayers.remove(this.name);
			
			disguisedPlayers.put(name, playerName);
			
			GameProfile profile = ((CraftPlayer) player).getProfile();
			
            try {
                Field nameField = GameProfile.class.getDeclaredField("name");
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                
                nameField.setAccessible(true);

                modifiersField.setAccessible(true);
                modifiersField.setInt(nameField, nameField.getModifiers() & ~Modifier.FINAL);

                nameField.set(profile, name);
            } catch (IllegalAccessException | NoSuchFieldException ex) {
            	System.out.println("Erreur lors du changement de nom de : " + playerName);
                throw new IllegalStateException(ex);
            }
		}
		
		this.disguisedUUID = disguisedUUID;
		this.name = name;
		this.rankType = rankType == null ? RankType.DEFAULT : rankType;
		
		if (skin != null)
			skin.setInUse(true);
		else if (DisguiseSkin.getSkin(disguisedUUID) != null)
			(skin = DisguiseSkin.getSkin(disguisedUUID)).setInUse(true);
		
		Bukkit.getPluginManager().callEvent(new PlayerDisguiseEvent(player, disguisedUUID, playerName, name, rankType, skin));
		OnimaAPI.getInstance().getDisguiseFactory().changeDisplay(playerName, disguisedUUID);
		OnimaMongo.executeAsync(OnimaCollection.DISGUISE_LOGS, database -> {
			database.insertOne(new Document("uuid", player.getUniqueId().timestamp())
					.append("name", name).append("skinUUID", disguisedUUID.toString())
					.append("rank_type", rankType.name()).append("time", System.currentTimeMillis()));
		});
	}
	
	public void disguise(UUID disguisedUUID, RankType rankType) {
		disguise(disguisedUUID, null, rankType);
	}
	
	public void disguise(DisguiseSkin skin, RankType rankType) {
		this.skin = skin;
		
		disguise(skin.getUUID(), skin.getName(), rankType);
	}

	public void undisguise() {
		if (skin != null) {
			skin.setInUse(false);
			skin = null;
		}

		if (name != null)
			disguisedPlayers.remove(name);
		
		disguisedUUID = null;
		name = null;
		rankType = null;
		
		Player player = Bukkit.getPlayer(playerName);
		GameProfile profile = ((CraftPlayer) player).getProfile();
		
        try {
            Field nameField = GameProfile.class.getDeclaredField("name");
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            
            nameField.setAccessible(true);

            modifiersField.setAccessible(true);
            modifiersField.setInt(nameField, nameField.getModifiers() & ~Modifier.FINAL);

            nameField.set(profile, playerName);
        } catch (IllegalAccessException | NoSuchFieldException ex) {
        	System.out.println("Erreur lors du changement de nom de : " + playerName);
            throw new IllegalStateException(ex);
        }
		
		Bukkit.getPluginManager().callEvent(new PlayerUndisguiseEvent(player));
		OnimaAPI.getInstance().getDisguiseFactory().removeChanges(playerName);
	}
	
	public boolean isDisguised() {
		return disguisedUUID != null && rankType != null;
	}
	
	public UUID getDisguisedUUID() {
		return disguisedUUID;
	}
	
	public String getName() {
		return name;
	}

	public RankType getRankType() {
		return rankType;
	}

	public void setRankType(RankType rankType) {
		this.rankType = rankType == null ? RankType.DEFAULT : rankType;
	}
	
	public static Map<String, String> getDisguisedPlayers() {
		return disguisedPlayers;
	}
	
}
