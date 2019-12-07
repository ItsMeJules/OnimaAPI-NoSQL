package net.onima.onimaapi.saver.inventory;

import java.util.Collection;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.mongo.saver.MongoSerializer;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.utils.ExperienceManager;
import net.onima.onimaapi.utils.Methods;

public class PlayerSaver extends InventorySaver implements MongoSerializer {
	
	public static Integer ID;
	
	private ItemStack[] armor;
	private Collection<PotionEffect> effects;
	private int xp, fireTicks, foodLevel, id;
	private double health;
	private float saturation;
	private SaveType saveType;
	private boolean restoreRequest;
	protected String message;

	public PlayerSaver(APIPlayer apiPlayer, SaveType saveType, String message) {
		super(apiPlayer.toPlayer().getInventory());
		
		this.saveType = saveType;
		this.message = message;
		
		Player player = apiPlayer.toPlayer();
		
		armor = player.getInventory().getArmorContents();
		effects = player.getActivePotionEffects();
		xp = apiPlayer.getExperienceManager().getCurrentExp();
		fireTicks = player.getFireTicks();
		foodLevel = player.getFoodLevel();
		health = ((Damageable) player).getHealth();
		saturation = player.getSaturation();
		
		id = ID++;
	}
	
	private PlayerSaver(Inventory inventory) {
		super(inventory);
	}
	
	/**
	 * Only call this method if your object is an instance of PlayerSaver, not {@link #restore(Inventory)}
	 * 
	 * @param player - The player to restore the inventory and data.
	 */
	public void restore(Player player) {
		Methods.clearInventory(player);
		
		super.restore(player.getInventory());
		
		player.getInventory().setArmorContents(armor);
		player.addPotionEffects(effects);
		player.setFireTicks(fireTicks);
		player.setFoodLevel(foodLevel);
		if (health > 0)
			player.setHealth(health);
		player.setSaturation(saturation);
		
		ExperienceManager expManager = APIPlayer.getPlayer(player).getExperienceManager();
		
		if (expManager.getCurrentExp() > 0)
			expManager.changeExp(xp);
		else
			expManager.setExp(xp);
	}
	
	public String getArmorAsString() {
		 StringBuilder builder = new StringBuilder();
		 
		 for (int i = 0; i < armor.length; i++) {
			 if (armor[i] == null || armor[i].getType() == Material.AIR)
				 continue;
			 
			 builder.append(i).append("#").append(Methods.serializeItem(armor[i], true)).append(";");
		 }
			 
		 return builder.toString();
	}
	
	public boolean isInRestoreRequest() {
		return restoreRequest;
	}
	
	public void setInRestoreRequest(boolean restoreRequest) {
		this.restoreRequest = restoreRequest;
	}
	
	public ItemStack[] getArmor() {
		return armor;
	}
	
	public SaveType getSaveType() {
		return saveType;
	}
	
	public Collection<PotionEffect> getEffects() {
		return effects;
	}
	
	public int getXp() {
		return xp;
	}
	
	public int getFireTicks() {
		return fireTicks;
	}

	public int getFoodLevel() {
		return foodLevel;
	}

	public double getHealth() {
		return health;
	}

	public float getSaturation() {
		return saturation;
	}

	public int getId() {
		return id;
	}
	
	public String getMessage() {
		return message;
	}
	
	@Override
	public Document getDocument(Object... objects) {
		return new Document("id", id).append("saved_time", savedTime)
				.append("items", getItemsAsString()).append("armor", getArmorAsString())
				.append("effects", Methods.serializePotionEffects(effects))
				.append("xp", xp).append("fire_ticks", fireTicks)
				.append("food_level", foodLevel).append("health", health)
				.append("saturation", (float) saturation).append("saved_type", saveType.name())
				.append("restore_request", restoreRequest).append("saver_message", message);
	}

	public static ItemStack[] getArmorFromString(String armor) {
		ItemStack[] armorItems = new ItemStack[4];
		
		if (armor != null && !armor.isEmpty()) {
			String[] values = armor.split(";");
			
			for (String val : values) {
				String[] info = val.split("#");
				
				armorItems[Integer.valueOf(info[0])] = Methods.deserializeItem(info[1], true);
			}
		}
		
		return armorItems;
	}
	
	public static PlayerSaver fromDB(int id, long savedTime, String items, String armor, String effects, int xp, int fireTicks, int foodLevel, double health, float saturation, SaveType saveType, boolean restoreRequest, String message) {
		PlayerSaver saver = new PlayerSaver(items.isEmpty() ? Bukkit.createInventory(null, 4 * PacketMenu.MIN_SIZE) : getItemsFromString(items));
		
		saver.id = id;
		saver.savedTime = savedTime;
		saver.armor = getArmorFromString(armor);
		saver.effects = Methods.deserializePotionEffects(effects);
		saver.xp = xp;
		saver.fireTicks = fireTicks;
		saver.foodLevel = foodLevel;
		saver.health = health;
		saver.saturation = saturation;
		saver.saveType = saveType;
		saver.restoreRequest = restoreRequest;
		saver.message = message;
		
		return saver;
	}
	
	public enum SaveType {
		MOD_MODE,
		DEATH;
	}
	
}
