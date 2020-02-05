package net.onima.onimaapi.listener;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.minecraft.util.com.google.common.collect.Sets;
import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.commands.essentials.KillCommand.EntityDamageCommandEvent;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.utils.PlayerOption;
import net.onima.onimaapi.saver.inventory.PlayerSaver;
import net.onima.onimaapi.saver.inventory.PlayerSaver.SaveType;

public class DeathListener implements Listener { //TODO faire un deathmessage qui permet de cliquer dessus et par exemple d'ouvrir l'inventaire du mec mort.

	private static final Set<EntityType> MALE_ENTITIES;
	
	static {
		MALE_ENTITIES = Sets.newHashSet(EntityType.BLAZE, EntityType.CHICKEN, EntityType.CREEPER, EntityType.EGG, EntityType.ENDER_DRAGON,
				EntityType.ENDERMAN, EntityType.GHAST, EntityType.GIANT, EntityType.HORSE, EntityType.IRON_GOLEM, EntityType.MAGMA_CUBE,
				EntityType.MINECART_TNT, EntityType.OCELOT, EntityType.PIG,  EntityType.PIG_ZOMBIE, EntityType.SHEEP, EntityType.SNOWMAN,
				EntityType.SQUID, EntityType.VILLAGER, EntityType.WITHER, EntityType.WOLF, EntityType.ZOMBIE);
	}
	
	private String getName(Enum<?> enu) {
		return enu.name().replace("_", " ").toLowerCase();
	}
	
	private String getItemName(ItemStack item) {
		if (item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			if (meta.hasDisplayName())
				return meta.getDisplayName();

		}
		
		return item.getType().name().replace("_", " ").toLowerCase();
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Player killer = player.getKiller();
		APIPlayer apiPlayer = APIPlayer.getPlayer(player);
		APIPlayer apiKiller = null;
		
		apiPlayer.addDeath();
		
		if (killer != null) {
			apiKiller = APIPlayer.getPlayer(killer);
			apiKiller.addKill();
		}
		
		event.setDeathMessage(null);
		player.getWorld().strikeLightningEffect(player.getLocation());
		
		EntityDamageEvent entityDamage = player.getLastDamageCause();
		DamageCause cause = entityDamage.getCause();
		String message = "";
		
		if (cause == null)
			cause = DamageCause.CUSTOM;
		 
		if (entityDamage instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) entityDamage;
			Entity entityDamager = damageEvent.getDamager();
			
			if (entityDamager instanceof Player) {
				APIPlayer damager = APIPlayer.getPlayer((Player) entityDamager);
				ItemStack hand = damager.toPlayer().getItemInHand();
				
				if (hand != null && hand.getType() != Material.AIR)
					message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7a été tué par §9" + damager.getDisplayName() + "§f[§e" + damager.getKills() + "§f] §7utilisant un(e) §9" + getItemName(hand) + "§7.";
				else
					message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7a été tué par §9" + damager.getDisplayName() + "§f[§e" + damager.getKills() + "§f] §7.";
			
			} else if (entityDamage instanceof Projectile) {
				LivingEntity shooter = ((Projectile) entityDamager).getShooter();
				int distance = (int) shooter.getLocation().distance(player.getLocation());
				
				if (shooter instanceof Player) {
					APIPlayer damager = APIPlayer.getPlayer((Player) shooter);
					ItemStack hand = damager.toPlayer().getItemInHand();
					
					if (hand != null && hand.getType() != Material.AIR)
						message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7s'est fait tirer dessus par §9" + damager.getDisplayName() + "§f[§e" + damager.getKills() + "§f] §7à " + distance + " block " + (distance > 1 ? "s" : "") +" en utilisant un(e) §9" + getItemName(hand) + ".";
					else
						message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7s'est fait tirer dessus par §9" + damager.getDisplayName() + "§f[§e" + damager.getKills() + "§f] §7à " + distance + " block " + (distance > 1 ? "s" : "") + ".";
				
				} else if (shooter instanceof Entity)
					message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7s'est fait tirer dessus par §9" + (MALE_ENTITIES.contains(shooter.getType()) ? "un" : "une") + ' ' + getName(shooter.getType()) + '.';
				else
					message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7s'est fait tirer dessus par un être mystérieux.";
			} else
				message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7a été tué par §9" + (MALE_ENTITIES.contains(entityDamager.getType()) ? "un" : "une") + ' ' + getName(entityDamager.getType()) + '.';
		
		} else if (cause == DamageCause.CUSTOM)
			message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7est mort.";
		else if (cause == DamageCause.BLOCK_EXPLOSION)
			message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7a été tué par une explosion.";
		else if (cause == DamageCause.CONTACT)
			message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7caressé un cactus trop fort.";
		else if (cause == DamageCause.DROWNING)
			message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7a cru qu'il pouvait respirer sous l'eau.";
		else if (cause == DamageCause.ENTITY_EXPLOSION)
			message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7s'est fait péter par un creeper.";
		else if (cause == DamageCause.FALL)
			message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7s'est fait rekt par le sol.";
		else if (cause == DamageCause.FALLING_BLOCK)
			message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7est mort par un block soumis à la gravité.";
		else if(cause == DamageCause.FIRE || cause == DamageCause.FIRE_TICK)
			message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7a cramé.";
		else if (cause == DamageCause.LAVA)
			message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7a essayé de nager dans la lave.";
		else if (cause == DamageCause.LIGHTNING)
			message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7s'est fait foudroyer.";
		else if (cause == DamageCause.MAGIC)
			message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7est mort à cause d'une potion.";
		else if (cause == DamageCause.MELTING)
			message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7est mort à cause d'une bonhomme de neige... PTDRRRR.";
		else if (cause == DamageCause.POISON)
			message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7s'est fait empoisonner.";
		else if (cause == DamageCause.STARVATION)
			message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7est mort de faim.";
		else if (cause == DamageCause.SUFFOCATION)
			message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7a suffoqué.";
		else if (cause == DamageCause.SUICIDE) {
			if (entityDamage instanceof EntityDamageCommandEvent && !((EntityDamageCommandEvent) entityDamage).isSelf())
				message = ((EntityDamageCommandEvent) entityDamage).getMessage();
			else
				message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7s'est suicidé.";
		} else if (cause == DamageCause.THORNS)
			message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7est mort à cause de thorns.";
		else if (cause == DamageCause.VOID)
			message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7a sauté dans le vide.";
		else if (cause == DamageCause.WITHER)
			message = "§9" + apiPlayer.getDisplayName() + "§f[§e" + apiPlayer.getKills() + "§f] §7s'est fait empoisonner à mort par un wither.";
		
		for (APIPlayer online : APIPlayer.getOnlineAPIPlayers()) {
			if (online.getOptions().getBoolean(PlayerOption.GlobalOptions.DEATH_MESSAGES))
				online.sendMessage(message);
		}
		
		apiPlayer.getPlayerDataSaved().add(new PlayerSaver(apiPlayer, SaveType.DEATH, message));
		OnimaAPI.sendConsoleMessage(message, null);
	}

}
