package net.onima.onimaapi.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import net.onima.onimaapi.OnimaAPI;

public class CombatLogger {
	
	private static Set<CombatLogger> combatLoggers;
	
	static {
		combatLoggers = new HashSet<>();
	}
	
	private Location location;
	private String name;
	private UUID uuid;
	private ItemStack[] items;
	private int experience;
	private BukkitTask task;
	private Set<UUID> teamMates;
	private LivingEntity entity;
	
	{
		items = new ItemStack[39];
		teamMates = new HashSet<>();
	}
	
	public CombatLogger(Location location, String name, UUID uuid) {
		this.location = location;
		this.name = name;
		this.uuid = uuid;
		
		combatLoggers.add(this);
	}
	
	public void spawn() {
		entity = (LivingEntity) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
		
		entity.setCustomName(name);
		entity.setCustomNameVisible(true);
		entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 100));
		entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 100));
		entity.setMaxHealth(ConfigurationService.LOGGER_HEALTH);
		entity.setHealth(ConfigurationService.LOGGER_HEALTH);
		
		task = Bukkit.getScheduler().runTaskLater(OnimaAPI.getInstance(), () -> {
			combatLoggers.remove(getCombatLogger(uuid));
			entity.remove();
		}, ConfigurationService.LOGGER_REMOVE_TIME);
	}
	
	public void kill() {
		combatLoggers.remove(this);
		entity.remove();
		task.cancel();
	}
	
	public Location getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}
	
	public UUID getUUID() {
		return uuid;
	}

	public ItemStack[] getItems() {
		return items;
	}

	public void setItems(ItemStack[] armor, ItemStack[] inventory) {
		items = Methods.mergeArrays(armor, inventory);
	}
	
	public int getExperience() {
		return experience;
	}
	
	public void setExperience(int experience) {
		this.experience = experience;
	}
	
	public BukkitTask getTask() {
		return task;
	}
	
	public Set<UUID> getTeamMates() {
		return teamMates;
	}
	
	public LivingEntity getEntity() {
		return entity;
	}

	public static CombatLogger getCombatLogger(UUID uuid) {
		for (CombatLogger logger : combatLoggers) {
			if (logger.uuid.equals(uuid))
				return logger;
		}
		return null;
	}
	
	public static CombatLogger getCombatLogger(String name) {
		for (CombatLogger logger : combatLoggers) {
			if (logger.name.equalsIgnoreCase(name))
				return logger;
		}
		return null;
	}
	
}
