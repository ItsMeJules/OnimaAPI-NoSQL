package net.onima.onimaapi.mod.items;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import net.onima.onimaapi.mod.EntityClickable;
import net.onima.onimaapi.mod.ModItem;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.Methods;

public class InventoryOpener extends ModItem implements EntityClickable {

	public InventoryOpener() {
		super("inventory_opener", 4, new BetterItem(Material.BOOK, 1, 0, MOD_PREFIX + " §cInvsee §8(§7R&L§8)", "§7Cliquez sur un joueur pour ouvrir son inventaire."));
	}

	@Override
	public void rightClick(APIPlayer player) {
		LivingEntity target = Methods.getEntityTargeting(player.toPlayer(), 100);
		
		if (target == null) {
			player.sendMessage(MOD_PREFIX + " §cAucune entité trouvée. Vous êtes peut-être trop éloigné ?");
			return;
		}
		
		entityInteract(player, target);
	}

	@Override
	public void leftClick(APIPlayer player) {
		LivingEntity target = Methods.getEntityTargeting(player.toPlayer(), 100);
		
		if (target == null) {
			player.sendMessage(MOD_PREFIX + " §cAucune entité trouvée. Vous êtes peut-être trop éloigné ?");
			return;
		}
		
		entityAttack(player, target);
	}
	
	@Override
	public void update(APIPlayer... players) {
	// TODO Auto-generated method stub
			
	}
	
	@Override
	public void entityInteract(APIPlayer player, Entity entity) {
		if (!(entity instanceof Player)) return;
		
		player.toPlayer().performCommand("invsee " + ((Player) entity).getName());
		useSucces.play(player);
	}

	@Override
	public void entityAttack(APIPlayer player, Entity entity) {
		entityInteract(player, entity);
	}

}
