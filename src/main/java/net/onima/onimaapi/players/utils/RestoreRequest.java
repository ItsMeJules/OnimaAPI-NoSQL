package net.onima.onimaapi.players.utils;

import java.util.UUID;

import org.bson.Document;
import org.bukkit.entity.Player;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.saver.inventory.PlayerSaver;
import net.onima.onimaapi.saver.mongo.MongoSerializer;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;

public class RestoreRequest implements MongoSerializer {
	
	private UUID restorer, target;
	private int saver_id;
	private String reason;
	
	public RestoreRequest(UUID restorer, UUID target, int saver_id, String reason) {
		this.restorer = restorer;
		this.target = target;
		this.saver_id = saver_id;
		this.reason = reason;
	}
	
	public void register(PlayerSaver saver) {
		OfflineAPIPlayer.getPlayer(target, offline -> {
			OfflineAPIPlayer offlineRestorer = OfflineAPIPlayer.getOfflineAPIPlayers().get(restorer);
			
			if (offline.isOnline())
				((APIPlayer) offline).sendMessage(new JSONMessage(offlineRestorer.getColoredName(true) + " §7vous a rétabli un inventaire. §oCliquez sur ce message.", "§aCliquez pour avoir des informations\net restaurer votre inventaire.", true, "/invrestored"));
		
			OnimaAPI.broadcast(new JSONMessage("§9" + offlineRestorer.getName() + " §7a restauré un inventaire de §9" + Methods.getName(offline, true) + "§7.", "§7Cliquez pour plus d'informations.", true, "/invrestored " + offline.getName()), OnimaPerm.RESTORE_COMMAND);
			
			saver.setInRestoreRequest(true);
			offline.getRestoreRequests().add(this);
			return;
		});
	}
	
	public void accept() {
		APIPlayer apiPlayer = APIPlayer.getPlayer(target);
		Player player = apiPlayer.toPlayer();
		
		if (!Methods.hasNoArmor(player)) {
			player.sendMessage("§cVous avez une armure sur vous, assurez-vous d'avoir un inventaire vide !");
			return;
		}
		
		if (!Methods.isEmpty(player.getInventory())) {
			player.sendMessage("§cVous avez un item sur vous, assurez-vous d'avoir un inventaire vide !");
			return;
		}
		
		PlayerSaver saver = getSaver(apiPlayer);
		
		saver.restore(player);
		apiPlayer.closeMenu(true);
		apiPlayer.getPlayerDataSaved().remove(saver);
		apiPlayer.getRestoreRequests().remove(this);
	}
	
	public UUID getRestorer() {
		return restorer;
	}
	
	public UUID getTarget() {
		return target;
	}
	
	public int getSaverID() {
		return saver_id;
	}

	public PlayerSaver getSaver(OfflineAPIPlayer offline) {
		return offline.getPlayerDataSaved().stream().filter(playerSaver -> saver_id == playerSaver.getId()).findFirst().orElse(null);
	}
	
	public String getReason() {
		return reason;
	}
	
	@Override
	public Document getDocument(Object... objects) {
		return new Document("saver_id", saver_id).append("restorer_uuid", restorer.toString()).append("reason", reason);
	}

}
