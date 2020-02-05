package net.onima.onimaapi.mod.items;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import net.onima.onimaapi.mod.EntityClickable;
import net.onima.onimaapi.mod.ModItem;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.Methods;

public class PlayerMounter extends ModItem implements EntityClickable {
	
	public static final double DAMAGE;
	
	static {
		DAMAGE = 10000D;
	}

	public PlayerMounter() {
		super("player_mounter", 8, new BetterItem(Material.BLAZE_ROD, 1, 0, MOD_PREFIX + " §cChevaucher §f& §cTuer", "§7Click gauche pour tuer une entité", "§7Click droit pour chevaucher une entité"));
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
		if (!(entity instanceof LivingEntity)) return;
		
		entity.setPassenger(player.toPlayer());
		useSucces.play(player);
	}

	@Override
	public void entityAttack(APIPlayer player, Entity entity) {
		if (!(entity instanceof LivingEntity)) return;
		
		((LivingEntity) entity).damage(DAMAGE, player.toPlayer());
		useSucces.play(player);
	}

}
