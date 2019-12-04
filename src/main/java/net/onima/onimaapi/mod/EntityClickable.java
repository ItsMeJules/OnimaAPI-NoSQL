package net.onima.onimaapi.mod;

import org.bukkit.entity.Entity;

import net.onima.onimaapi.players.APIPlayer;

public interface EntityClickable {
	
	public void entityInteract(APIPlayer player, Entity entity);
	public void entityAttack(APIPlayer player, Entity entity);

}
