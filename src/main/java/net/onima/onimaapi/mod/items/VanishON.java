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

public class VanishON extends ModItem implements EntityClickable {

	public VanishON() {
		super("vanish_on", 2, new BetterItem(Material.INK_SACK, 1, 14, MOD_PREFIX + " §cVanish §8(§a§lON§8) (§7R&L§8)", "§7Click dans le vide pour switcher de vanish.", "§7Click sur un joueur pour le faire disparaitre."));
	}
	
	@Override
	public void rightClick(APIPlayer player) {
		player.toPlayer().performCommand("vanish");
		player.toPlayer().getInventory().setItem(slot, ModItem.fromName("vanish_off").getItem().toItemStack());
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
		useSucces.play(player);
		player.sendMessage(MOD_PREFIX + " §cVous §favez fait disparaître §c" + Methods.getName(target, true) + " §fpendant §c" + time + " §fseconde" + (time > 1 ? "s" : "") + '.');
	}

	@Override
	public void entityAttack(APIPlayer player, Entity entity) {
		entityInteract(player, entity);
	}

}
