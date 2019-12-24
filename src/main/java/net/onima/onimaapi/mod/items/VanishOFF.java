package net.onima.onimaapi.mod.items;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.onima.onimaapi.mod.EntityClickable;
import net.onima.onimaapi.mod.ModItem;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.utils.PlayerOption;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.time.Time;

public class VanishOFF extends ModItem implements EntityClickable {

	public VanishOFF() {
		super("vanish_off", 2, new BetterItem(Material.INK_SACK, 1, 7, MOD_PREFIX + " §cVanish §8(§c§lOFF§8) (§7R&L§8)", "§7Click dans le vide pour switcher de vanish.", "§7Click sur un joueur pour le faire disparaitre."));
	}
	
	@Override
	public void rightClick(APIPlayer player) {
		player.toPlayer().performCommand("vanish");
		player.toPlayer().getInventory().setItem(slot, ModItem.fromName("vanish_on").getItem().toItemStack());
	}

	@Override
	public void leftClick(APIPlayer player) {
		rightClick(player);
	}

	@Override
	public void update(APIPlayer... players) {
	// TODO Auto-generated method stub
			
	}
	
	@Override
	public void entityInteract(APIPlayer player, Entity entity) {
		if (!(entity instanceof Player)) return;
		
		Player target = (Player) entity;
		int time = player.getOptions().<Integer>get(PlayerOption.ModOptions.HIDE_PLAYER_TIME);
		
		player.hidePlayer(target, time * Time.SECOND);
		player.sendMessage(MOD_PREFIX + " §cVous §favez fait disparaître §c" + Methods.getName(target, true) + " §fpendant §c" + time + " §fseconde" + (time > 1 ? "s" : "") + '.');
	}

	@Override
	public void entityAttack(APIPlayer player, Entity entity) {
		entityInteract(player, entity);
	}

}
