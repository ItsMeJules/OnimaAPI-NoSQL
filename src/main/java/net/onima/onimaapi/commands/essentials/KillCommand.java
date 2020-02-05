package net.onima.onimaapi.commands.essentials;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.Methods;

public class KillCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player target;
		
		if (args.length > 0 && OnimaPerm.KILL_COMMAND.has(sender)) {
			UUID uuid = UUIDCache.getUUID(args[0]);
			
			if (uuid == null) {
				sender.sendMessage("§cLe joueur " + args[0] + " ne s'est jamais connecté sur le serveur !");
				return false;
			}
			
			target = Bukkit.getPlayer(uuid);
		} else {
			if (!(sender instanceof Player)) {
				sender.sendMessage("§cCette commande est seulement utilisable pour les joueurs !");
				return false;
			}
			
			target = (Player) sender;
		}
		

        if (target == null || (sender instanceof Player && !((Player) sender).canSee(target))) {
            sender.sendMessage("§c" + args[0] + " n'est pas connecté !");
            return false;
        }
        
        if (target.isDead()) {
            sender.sendMessage("§c" + Methods.getRealName((OfflinePlayer) target) + " est déjà mort.");
            return false;
        }
        
        final EntityDamageCommandEvent event = new EntityDamageCommandEvent((Entity) target, EntityDamageEvent.DamageCause.SUICIDE, Integer.MAX_VALUE, sender.equals(target));
        Bukkit.getPluginManager().callEvent((Event) event);
        
        if (event.isCancelled()) {
            sender.sendMessage("§cVous ne pouvez pas kill " + Methods.getRealName((OfflinePlayer) target) + '.');
            return false;
        }
        
        if (!event.isSelf())
        	event.setMessage("§7" + Methods.getRealName(sender) + " a kill §e" + Methods.getRealName((OfflinePlayer) target));
        
        target.setLastDamageCause(event);
        target.setHealth(0.0);
        
		return true;
	}
	
	public class EntityDamageCommandEvent extends EntityDamageEvent {

		private boolean self;
		private String message;

		@SuppressWarnings("deprecation")
		public EntityDamageCommandEvent(Entity damagee, DamageCause cause, int damage, boolean self) {
			super(damagee, cause, damage);
			
			this.self = self;
		}
		
		public boolean isSelf() {
			return self;
		}
		
		public void setMessage(String message) {
			this.message = message;
		}
		
		public String getMessage() {
			return message;
		}
		
	}

}
