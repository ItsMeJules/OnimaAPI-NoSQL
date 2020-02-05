package net.onima.onimaapi.mod.items;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import net.onima.onimaapi.mod.EntityClickable;
import net.onima.onimaapi.mod.ModItem;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.utils.PlayerOption;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.OSound;

public class CompassBooster extends ModItem implements EntityClickable { //TODO x, y, z multiplier not x y and z multiplier.
																		 //Ajouter un cooldown pour le boost d'autres entités.
	private static OSound boostSound;
	
	static {
		boostSound = new OSound(Sound.ENDERDRAGON_WINGS, 1.5F, 1F);
	}
	
	public CompassBooster() {
		super("compass_booster", 1, new BetterItem(Material.COMPASS, 1, 0, MOD_PREFIX + " §cBooster §8(§7R&L§8)", "§7Click gauche pour se booster.", "§7Click droit pour booster une entité."));
	}

	@Override
	public void rightClick(APIPlayer player) {
		LivingEntity target = Methods.getEntityTargeting(player.toPlayer(), 100);
		
		if (target == null) {
			player.sendMessage(MOD_PREFIX + " §cAucune entité trouvée. Vous êtes peut-être trop éloigné ?");
			return;
		}
		
		Methods.boost(target, player.getOptions().<Double>get(PlayerOption.ModOptions.ENTITY_BOOSTER), player.getOptions().<Double>get(PlayerOption.ModOptions.ENTITY_BOOSTER), player.getOptions().<Double>get(PlayerOption.ModOptions.ENTITY_BOOSTER));
		
		player.sendMessage(MOD_PREFIX + " §fWOOOOOOOSSSSHHH !!!!");
		boostSound.play(player);
	}

	@Override
	public void leftClick(APIPlayer player) {
		Methods.boost(player.toPlayer(), player.getOptions().<Double>get(PlayerOption.ModOptions.PLAYER_BOOSTER));
		boostSound.play(player);
	}
	
	@Override
	public void update(APIPlayer... players) {
	// TODO Auto-generated method stub
			
	}

	@Override
	public void entityInteract(APIPlayer player, Entity entity) {
		if (!(entity instanceof LivingEntity))
			return;
		
		Methods.boost((LivingEntity) entity, player.getOptions().<Double>get(PlayerOption.ModOptions.ENTITY_BOOSTER));
		
		player.sendMessage(MOD_PREFIX + " §fWOOOOOOOSSSSHHH !!!!");
		boostSound.play(player);
	}
	
	@Override
	public void entityAttack(APIPlayer player, Entity entity) {
		entityInteract(player, entity);
	}
	
}
