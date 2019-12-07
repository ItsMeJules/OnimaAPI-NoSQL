package net.onima.onimaapi.utils;

import java.lang.reflect.Array;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SplittableRandom;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import com.google.common.collect.Maps;

import net.md_5.bungee.api.chat.BaseComponent;
import net.onima.onimaapi.mongo.OnimaMongo;
import net.onima.onimaapi.mongo.OnimaMongo.OnimaCollection;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.serialization.ItemSerializator;
import net.onima.onimaapi.serialization.LocationSerializator;
import net.onima.onimaapi.utils.time.Time.IntegerTime;

/**
 * Class where are regrouped some divers methods.<br>
 * It is impossible to instantiate this class.
 */
public class Methods {
	
	private static Map<UUID, Integer> kills;
	
	static {
		kills = new HashMap<>();
	}
	
	private Methods() {}
	
	/**
	 * This method replaces the color char '&' for a String.
	 * 
	 * @param string message to color.
	 * @return The formatted message.<br>
	 * <tt>null</tt> if the given string is null.
	 */
	public static String colors(String string) {
		if (string == null)
			return null;
		
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	/**
	 * This method replaces the color char '&' for a list of string.
	 * 
	 * @param list - list of string to color.
	 * @return The formatted message string list.
	 */
	public static List<String> colors(List<String> list) {
		List<String> newList = new ArrayList<>();
		
		for (String str : list)
			newList.add(colors(str));
		
		return newList;
	}
	
	/**
	 * This method sends a sound to the whole server.
	 * 
	 * @param sound - {@link Sound} to play.
	 * @param volume - the sound volume.
	 * @param speed - the sound play speed.
	 */
	public static void playServerSound(Sound sound, float volume, float speed) {
		for (Player online : Bukkit.getOnlinePlayers())
			online.playSound(online.getLocation(), sound, volume, speed);
	}
	
	/**
	 * This method sends a sound to the whole server using OnimaAPI's sound management.
	 * 
	 * @param oSound - {@link OSound} the sound.
	 */
	public static void playServerSound(OSound oSound) {
		for (APIPlayer online : APIPlayer.getOnlineAPIPlayers())
			oSound.play(online);
	}
	

	/**
	 * This method gets all the online players that the given player can see.
	 * 
	 * @param player - The player to get the online players he can see.
	 * 
	 * @return The online players.
	 */
	public static List<Player> getOnlinePlayers(Player player) {
		List<Player> players = new ArrayList<>();
		
		for (Player online : Bukkit.getOnlinePlayers()) {
			if (player != null && !player.canSee(online))
				continue;
			else
				players.add(online);
		}
		
		return players;
	}
	
	/**
	 * This method parses a string into a Long.
	 * 
	 * @param string - String to parse as a Long.
	 * @return If the parse succeed the Long will have the string's value.<br>
	 * <tt>null</tt> if the parse failed.
	 */
	public static Long toLong(String string) {
		try {
			return Long.parseLong(string);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * This method parses a string into an Integer.
	 * 
	 * @param string - String to parse as a Integer.
	 * @return If the parse succeed the Integer will have the string's value.<br>
	 * <tt>null</tt> if the parse failed.
	 */
	public static Integer toInteger(String string) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * This method parses a string into a Double.
	 * 
	 * @param string - String to parse as a Double.
	 * @return If the parse succeed the Double will have the string's value.<br>
	 * <tt>null</tt> if the parse failed.
	 */
	public static Double toDouble(String string) {
		try {
			return Double.parseDouble(string);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * This method parses a string into a Float.
	 * 
	 * @param string - String to parse as a Float.
	 * @return If the parse succeed the Float will have the string's value.<br>
	 * <tt>null</tt> if the parse failed.
	 */
	public static Float toFloat(String string) {
		try {
			return Float.parseFloat(string);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * This method gets the {@link ChatColor} from the string.
	 * 
	 * @param str - String to get the ChatColor from.
	 * @return ChatColor enum value.<br>
	 * <tt>null</tt> if the string isn't a color.
	 */
	public static ChatColor colorFromString(String str) {
		switch (str) {
		case "§0":
			return ChatColor.BLACK;
		case "§1":
			return ChatColor.DARK_BLUE;
		case "§2":
			return ChatColor.DARK_GREEN;
		case "§3":
			return ChatColor.DARK_AQUA;
		case "§4":
			return ChatColor.DARK_RED;
		case "§5":
			return ChatColor.DARK_PURPLE;
		case "§6":
			return ChatColor.GOLD;
		case "§7":
			return ChatColor.GRAY;
		case "§8":
			return ChatColor.DARK_GRAY;
		case "§9":
			return ChatColor.BLUE;
		case "§a":
			return ChatColor.GREEN;
		case "§b":
			return ChatColor.AQUA;
		case "§c":
			return ChatColor.RED;
		case "§d":
			return ChatColor.LIGHT_PURPLE;
		case "§e":
			return ChatColor.YELLOW;
		case "§f":
			return ChatColor.WHITE;
		case "§k":
			return ChatColor.MAGIC;
		case "§l":
			return ChatColor.BOLD;
		case "§m":
			return ChatColor.STRIKETHROUGH;
		case "§n":
			return ChatColor.UNDERLINE;
		case "§o":
			return ChatColor.ITALIC;
		case "§r":
			return ChatColor.RESET;
		default: 
			return null;
		}
	}
	
	/**
	 * This method rounds a location by returning a new one with the blockX blockY and blockZ.
	 * 
	 * @param location - Location to round.
	 * @return The rounded location.
	 */
	public static Location roundLocation(Location location) {
		return new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}
	
	/**
	 * This method tests if two locations are equals by comparing their block location.
	 * 
	 * @param location - Location to test.
	 * @param location2 - Location to test.
	 * @return true if locations are the same.<br>
	 * false if they are different
	 */
	public static boolean locationEquals(Location location, Location location2) {
		if (location != null && location2 != null) {
			if (location.getBlockX() == location2.getBlockX() 
					&& location.getBlockY() == location2.getBlockY() 
					&& location.getBlockZ() == location2.getBlockZ())
				return true;
		}
		
		return false;
	}
	
	/**
	 * This method clones an array.
	 * 
	 * @param array - array to clone.
	 * @return the cloned array.<br>
	 * <tt>null if the given array was null</tt>
	 */
    public static <T> T[] clone(final T[] array) {
        if (array == null) return null;
        return array.clone();
    }
    
	/**
	 * This method tests if the inventory is empty (excluding armor slots).
	 * 
	 * @param inventory - Inventory to check if it's empty.
	 * @return true if inventory is empty.<br>
	 * false if inventory isn't empty.
	 */
	public static boolean isEmpty(Inventory inventory) {
		for (ItemStack item : inventory.getContents()) {
			if (item != null)
				return false;
		}
		return true;
	}
	
	/**
	 * This method test if a LivingEntity is wearing something in it's armor slots.
	 * 
	 * @param entity - LivingEntity to check the armor slots.
	 * @return true if slots are empty.<br>
	 * false if slots aren't empty.
	 */
	public static boolean hasNoArmor(LivingEntity entity) {
		EntityEquipment equipment = entity.getEquipment();
		
		for (ItemStack item : equipment.getArmorContents()) {
			if (item.getType() != Material.AIR)
				return false;
		}
		return true;
	}
	
	/**
	 * This method gets the index from a string in a list.
	 * 
	 * @param list - List to get the index from.
	 * @param arg - The arg in to find in the list.
	 * @return -1 if there was no match.<br>
	 * A number >= 0 if there was a match.
	 */
	public static int getIndexFromString(List<String> list, String arg) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).contains(arg))
				return i;
		}
		return -1;
	}
	
	/**
	 * This method finds the first item specified in an inventory.
	 * 
	 * @param inventory - The inventory to search the item.
	 * @param materialtID - The material id of the item.
	 * @return the item stack found.<br>
	 * <tt>null</tt> if there was no match.
	 */
	@SuppressWarnings("deprecation")
	public static ItemStack getItem(Inventory inventory, int materialtID) {
        ItemStack[] items = inventory.getContents();
        
        for (int i = 0; i < items.length; i++) {
        	ItemStack item = items[i];
        	
            if (item != null && item.getType() == Material.getMaterial(materialtID))
                return item;
        }
		return null;
	}
	
	/**
	 * This method removes a certain amount of items which correspond to the given material.
	 * 
	 * @param inventory - The inventory to remove the item.
	 * @param material - The material to remove.
	 * @param amount - The amount of item to remove.
	 */
	public static void removeItem(Inventory inventory, Material material, int amount) {
        ItemStack[] items = inventory.getContents();
        
        for (int i = 0; i < items.length; i++) {
        	ItemStack item = items[i];
        	
            if (item != null && item.getType() == material) {
            	if (item.getAmount() == 1)
					inventory.setItem(i, null);
				else {
					item.setAmount(item.getAmount()-amount);
					inventory.setItem(i, item);
				}
                break;
            }
        }
	}
	
	/**
	 * This method sends a JSON chat message to a CommandSender.
	 * 
	 * @param sender - The sender to send the message.
	 * @param components - The md5 api for JSON chat message.
	 */
	public static void sendJSON(CommandSender sender, BaseComponent[] components) {
		if (sender instanceof Player)
			((Player) sender).spigot().sendMessage(components);
		else {
			StringBuilder builder = new StringBuilder();
			
			for (BaseComponent component : components)
				builder.append(component.toPlainText());

			sender.sendMessage(builder.toString());
		}
	}
	
	/**
	 * This method formats a long on a string using so users can read it.
	 * 
	 * @param date - The time to format.
	 * @param format - The format.
	 * @return A formatted string to display the date.
	 */
	public static String toFormatDate(long date, String format) {
		return new SimpleDateFormat(format).format(date);
	}
	
	/**
	 * This method checks if the parameter start it between the min and max value.<br>
	 * Each number is casted to a double to have the best precision.
	 * 
	 * @param start - Element to check if it's between.
	 * @param min - The lowest value.
	 * @param max - The highest value.
	 * @return true if it's between min and max.<br>
	 * false if it's not between.
	 */
	public static boolean isBetween(Number start, Number min, Number max) {
		double startDouble = start.doubleValue();
		return startDouble > min.doubleValue() && startDouble < max.doubleValue();
	}
	
	/**
	 * This method rounds a given double using the class {@link DecimalFormat}.
	 * 
	 * @param round - The format to round this double
	 * @param toRound - The double to round.
	 * @return A string with the double rounded.
	 */
	public static String round(String round, double toRound) {
		NumberFormat nf = new DecimalFormat(round);
		
		nf.setRoundingMode(RoundingMode.HALF_UP);
		return nf.format(toRound).replace(',', '.');
	}
	
	/**
	 * This method sorts a map by it's value using the {@link Stream} api.
	 * @param map - Map to sort
	 * @return A map sorted by value.
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortMapByValue(Map<K, V> map) {
	    return map.entrySet().parallelStream().sorted(Entry.comparingByValue(Collections.reverseOrder())).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}
	
	/**
	 * This method returns the last player who attacked the victim.
	 * 
	 * @param event - Event triggered to get the last attacker.
	 * @return The player who last damaged the victim.<br>
	 * <tt>null</tt> if the last damager wasn't a player.
	 */
	@SuppressWarnings("deprecation")
	public static Player getLastAttacker(EntityDamageByEntityEvent event) {
		Player attacker = null;
		Entity damager = event.getDamager();
			
		if (damager instanceof Player)
			attacker = (Player) damager;
		else if (damager instanceof Projectile) {
			ProjectileSource shooter = ((Projectile) damager).getShooter();
				
			if (shooter instanceof Player) attacker = (Player) shooter;
		}
		return attacker;
	}
	
	/**
	 * This method returns the nice name of an ItemStack.
	 * 
	 * @param stack - ItemStack to get the name.
	 * @return A better looking item stack name.
	 */
	 public static String getItemName(ItemStack stack) {
		return CraftItemStack.asNMSCopy(stack).getName();
	 }
	 
	 /**
	  * This method returns a list with all the replaced tags specified by the param replacement. Example:
	  * <p>
	  * 
	  * <pre>
	  * {@code List<String> list = new ArrayList();
	  * 
	  * replacePlaceholder(list, "%tag1%", "Replacement1", "%tag2%", "Replacement2");}
	  * </pre>
	  * 
	  * @param list - The list to where the tags are going to be replaced.
	  * @param replacement - Words to replace
	  * @return The list with all the replaced tags.
	  */
	 public static List<String> replacePlaceholder(List<String> list, Object... replacement) {
		 List<String> newList = new ArrayList<>();
		
		 for (String str : list) {
			 for (int i = 0; i < replacement.length; i++) {
				 str = str.replace(String.valueOf(replacement[i]), String.valueOf(replacement[i+1]));
				 i++;
			 }
			 
			 newList.add(str);
		 }
		 return newList;
	 }

	 /**
	  * This method merges 2 arrays.
	  * 
	  * @throws IllegalArgumentException if the arrays have different types.
	  * @throws ArrayStoreException if the arrays have different types.
	  * @param array1 - Array to merge
	  * @param array2 - Array to merge
	  * @return The combination of the 2 given arrays.
	  */
	 @SuppressWarnings("unchecked")
	 public static <T> T[] mergeArrays(final T[] array1, final T... array2) {
		 if (array1 == null)	return clone(array2);
		 else if(array2 == null)	return clone(array1);
	        
		 final Class<?> type1 = array1.getClass().getComponentType();
		 final T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
	        
		 System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		 try {
			 System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		 } catch (final ArrayStoreException ase) {
			 final Class<?> type2 = array2.getClass().getComponentType();
	            
			 if (!type1.isAssignableFrom(type2))
				 throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of "+ type1.getName(), ase);

			 throw ase;
		 }
		 return joinedArray;
	 }
	 
	 /**
	  * This method serializes the actual location into a String that you can store in a config file.
	  * 
	  * @param location - The location to serialize.
	  * @param details - Should we even serialize the yaw, pitch ?
	  * @return The location serialized into a String.
	  */
	 public static String serializeLocation(Location location, boolean details) {
		 if (location == null) return "";
		 LocationSerializator serial = new LocationSerializator(location);
		 
		 if (details)
			 serial.serializeWithDetails();
		 else
			 serial.serialize();
			
		 return serial.getSerialized();
	 }
	 
	 public static String serializeItem(ItemStack item, boolean details) {
		 if (item == null) return "";
		 ItemSerializator serial = new ItemSerializator(item);
		 
		 if (details)
			 serial.serializeWithDetails();
		 else
			 serial.serialize();
			
		 return serial.getSerialized();
	 }
		
	 /**
	  * This method deserializes the actual String into a Location.
	  * 
	  * @param string - The String to deserialize.
	  * @param details - Should we deserialize the String with the yaw, pitch ?
	  * @return The location deserialized.
	  */
	 public static Location deserializeLocation(String string, boolean details) {
		 LocationSerializator serial = new LocationSerializator(string);
		 
		 if (details)
			 serial.deserializeWithDetails();
		 else
			 serial.deserialize();
			
		 return serial.getDeserialized();
	 }
	 
	 public static ItemStack deserializeItem(String string, boolean details) {
		 ItemSerializator serial = new ItemSerializator(string);
		 
		 if (details)
			 serial.deserializeWithDetails();
		 else
			 serial.deserialize();
			
		 return serial.getDeserialized();
	 }
	 
	 public static String serializeInventory(Inventory inventory) {
		 if (inventory == null) return "";
		 StringBuilder builder = new StringBuilder();
			
		 builder.append(inventory.getSize()).append(";");
			
		 for (int i = 0; i < inventory.getSize(); i++) {
			 ItemStack item = inventory.getItem(i);
				
			 if (item != null) {
				 ItemSerializator serializeItem = new ItemSerializator(item);
				 serializeItem.serializeWithDetails();
				 builder.append(i).append("#").append(serializeItem.getSerialized()).append(";");
			 }
		 }
		 return builder.toString();
	 }
		
	 public static String serializeItems(Inventory inventory) {
		 if (inventory == null) return "";
		 StringBuilder builder = new StringBuilder();
			
		 for (int i = 0; i < inventory.getSize(); i++) {
			 ItemStack item = inventory.getItem(i);
				
			 if (item != null) {
				 ItemSerializator serializeItem = new ItemSerializator(item);
				 serializeItem.serializeWithDetails();
				 builder.append(i).append("#").append(serializeItem.getSerialized()).append(";");
			 }
		 }
		 return builder.toString();
	 }
		
	 public static String serializeItems(Map<Integer, ItemStack> items) {
		 if (items == null) return "";
		 StringBuilder builder = new StringBuilder();
			
		 for (Entry<Integer, ItemStack> entry : items.entrySet()) {
			 ItemStack item = entry.getValue();
				
			 if (item != null) {
				 ItemSerializator serializeItem = new ItemSerializator(item);
				 serializeItem.serializeWithDetails();
				 builder.append(entry.getKey()).append("#").append(serializeItem.getSerialized()).append(";");
			 }
		 }
		 return builder.toString();
	 }
		
	 @SuppressWarnings("deprecation")
	 public static Inventory deserializeInventory(String serialization) {
		 if (serialization == null) return null;
			
		 String[] invInfo = serialization.split(";");
		 Inventory deserialize = Bukkit.createInventory(null, Integer.valueOf(invInfo[0]));
			
		 for (int i = 1; i < invInfo.length; i++) {
			 String[] items = invInfo[i].split("#");
			 int position = Integer.valueOf(items[0]);
				
			 if (position >= deserialize.getSize())
				 continue;
				
			 ItemStack istack = null;
			 ItemMeta meta = null;
			 List<String> lore = null;
			 boolean created = false;
				
			 String[] itemInfo = items[1].split("!");
				
			 for (String info : itemInfo) {
				 String[] attribute = info.split("@");
					
				 switch(attribute[0]) {
				 case "t":
					 istack = new ItemStack(Material.getMaterial(Integer.valueOf(attribute[1])));
					 meta = istack.getItemMeta();
					 lore = new ArrayList<>();
					 created = true;
					 break;
				 case "d":
					 if(created) istack.setDurability(Short.valueOf(attribute[1]));
					 break;
				 case "a":
					 if(created) istack.setAmount(Integer.valueOf(attribute[1]));
					 break;
				 case "e":
					 if(created) meta.addEnchant(Enchantment.getById(Integer.valueOf(attribute[1])), Integer.valueOf(attribute[2]), false);
					 break;
				 case "n":
					 meta.setDisplayName(attribute[1].replace("&", "§"));
					 break;
				 case "l":
					 lore.add(attribute[1].replace("&", "§"));
				 default:
					 break;
				 }
			 }
			 meta.setLore(lore);
			 istack.setItemMeta(meta);
			 deserialize.setItem(position, istack);
		 }
		 return deserialize;
	 }
		
	 @SuppressWarnings("deprecation")
	 public static Map<Integer, ItemStack> deserializeItems(String serialization) {
		 if (serialization == null) return null;
			
		 String[] invInfo = serialization.split(";");
		 Map<Integer, ItemStack> deserialize = new HashMap<>();
			
		 for (int i = 0; i < invInfo.length; i++) {
			 String[] items = invInfo[i].split("#");
				
			 ItemStack istack = null;
			 ItemMeta meta = null;
			 List<String> lore = null;
			 boolean created = false;
				
			 String[] itemInfo = items[1].split("!");
				
			 for (String info : itemInfo) {
				 String[] attribute = info.split("@");
					
				 switch (attribute[0]) {
				 case "t":
					 istack = new ItemStack(Material.getMaterial(Integer.valueOf(attribute[1])));
					 meta = istack.getItemMeta();
					 lore = new ArrayList<>();
					 created = true;
					 break;
				 case "d":
					 if (created) istack.setDurability(Short.valueOf(attribute[1]));
					 break;
				 case "a":
					 if(created) istack.setAmount(Integer.valueOf(attribute[1]));
					 break;
				 case "e":
					 if (created) meta.addEnchant(Enchantment.getById(Integer.valueOf(attribute[1])), Integer.valueOf(attribute[2]), false);
					 break;
				 case "n":
					 meta.setDisplayName(attribute[1].replace("&", "§"));
					 break;
				 case "l":
					 lore.add(attribute[1].replace("&", "§"));
				 default:
					 break;
				 }
			 }
				
			 meta.setLore(lore);
			 istack.setItemMeta(meta);
			 deserialize.put(Integer.valueOf(items[0]), istack);
		 }
		 return deserialize;
	 }
		
	 public static void clearInventory(Player player) {
		 PlayerInventory inventory = player.getInventory();
			
		 inventory.clear();
		 inventory.setArmorContents(null);
	 }
		
	 public static String serializePotionEffects(Collection<PotionEffect> effects) {
		 String serialization = "";
				
		 for (PotionEffect effect : effects)
			 serialization +="!e@"+effect.getType().getName()+"!a@"+effect.getAmplifier()+"!d@"+effect.getDuration()+";";
			
		 return serialization;
	 }
		
	 public static Collection<PotionEffect> deserializePotionEffects(String serialization) {
		 Collection<PotionEffect> effects = new HashSet<>();
			
		 if (serialization == null || !serialization.contains("!")) return effects;
			
		 String[] allEffect = serialization.split(";"); 
		 for(String effect : allEffect) {
			 String effectName = "";
			 int duration = 0, amplifier = 0;
			 String[] cutted = effect.split("!");
				
			 for (String str : cutted) {
				 String[] details = str.split("@");
				 switch(details[0]) {
				 case "e":
					 effectName = details[1];
					 break;
				 case "a":
					 amplifier = Integer.valueOf(details[1]);
					 break;
				 case "d":
					 duration = Integer.valueOf(details[1]);
					 break;

				 default:
					 break;
				 }
			 }
			 
			 effects.add(new PotionEffect(PotionEffectType.getByName(effectName), duration, amplifier));
		 }
			
		 return effects;
	 }
	 
	 public static boolean hasGotLastDamageByPlayer(Player player) {
		 return (player.getLastDamageCause() != null 
				 && player.getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK 
				 && player.getLastDamageCause() instanceof EntityDamageByEntityEvent 
				 && ((EntityDamageByEntityEvent) player.getLastDamageCause()).getDamager() instanceof Player);	
	 }
	 
	 public static <E extends Comparable<E>> Stream<Entry<String, E>> getTop(Map<String, E> scores, int amount) {
		 return scores.entrySet().parallelStream().sorted(Comparator.comparing(Entry::getValue, Comparator.reverseOrder())).limit(amount);
	 }
	 
	 public static List<String> setEffectsAsInInventory(Collection<PotionEffect> effects, String timeColor, String effectColor) {
		 List<String> lore = new ArrayList<>();
			
		 for (PotionEffect effect : effects) {
			 String effectname = ConfigurationService.EFFECTS_NICE_NAME.get(effect.getType());
			 String effectpower = toRomanNumber(effect.getAmplifier() + 1);
				
			 if (effect.getDuration() > 9999) 
				 lore.add(effectColor + effectname + ' ' + effectpower + timeColor + " **:**");
			 else 
				 lore.add(effectColor + effectname + ' ' + effectpower + ' ' + timeColor + IntegerTime.setHMSFormat(effect.getDuration() / 20));
		 }
		 return lore;
	 }

	public static PotionEffect getPotionEffectByType(Collection<PotionEffect> effects, PotionEffectType type) {
		for (PotionEffect effect : effects) {
			if (effect.getType().equals(type))
				return effect;
		}
		return null;
	}
	
	public static void removeOneItem(Player player) {
		if (player.getItemInHand().getAmount() == 1)
			player.setItemInHand(null);
		else
			player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
	}

	public static void clearEffects(Player player) {
		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
	}

    public static String toRomanNumber(int number) {
        int l = ConfigurationService.NUMBER_ROMAN_VALUES.floorKey(number);
        
        if (number == l)
            return ConfigurationService.NUMBER_ROMAN_VALUES.get(number);
        
        return ConfigurationService.NUMBER_ROMAN_VALUES.get(l) + toRomanNumber(number - l);
    }
    
    @SuppressWarnings("deprecation")
	public static boolean isSimilar(ItemStack stack1, ItemStack stack2) {
        if (stack1 == null || stack2 == null)
            return false;

        if (stack1 == stack2)
            return true;

        return stack1.getTypeId() == stack2.getTypeId() && stack1.hasItemMeta() == stack2.hasItemMeta() && (stack1.hasItemMeta() ? Bukkit.getItemFactory().equals(stack1.getItemMeta(), stack2.getItemMeta()) : true);
    }
    
    public static LivingEntity getEntityTargeting(Player player, int blocks) {
    	Location observerPos = player.getEyeLocation();
        Vector3D observerDir = new Vector3D(observerPos.getDirection());

        Vector3D observerStart = new Vector3D(observerPos);
        Vector3D observerEnd = observerStart.add(observerDir.multiply(blocks));

        LivingEntity hit = null;
        
        // Get nearby entities
        for (LivingEntity target : player.getWorld().getLivingEntities()) {
            // Bounding box of the given player
            Vector3D targetPos = new Vector3D(target.getLocation());
            Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
            Vector3D maximum = targetPos.add(0.5, 1.67, 0.5);

            if (target != player && hasIntersection(observerStart, observerEnd, minimum, maximum)) {
                if (hit == null || 
                        hit.getLocation().distanceSquared(observerPos) > 
                        target.getLocation().distanceSquared(observerPos)) {

                    hit = target;
                }
            }
        }
        return hit;
    }
    
    private static boolean hasIntersection(Vector3D p1, Vector3D p2, Vector3D min, Vector3D max) {
        final double epsilon = 0.0001f;
 
        Vector3D d = p2.subtract(p1).multiply(0.5);
        Vector3D e = max.subtract(min).multiply(0.5);
        Vector3D c = p1.add(d).subtract(min.add(max).multiply(0.5));
        Vector3D ad = d.abs();
 
        if (Math.abs(c.x) > e.x + ad.x)
            return false;
        if (Math.abs(c.y) > e.y + ad.y)
            return false;
        if (Math.abs(c.z) > e.z + ad.z)
            return false;
 
        if (Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + epsilon)
            return false;
        if (Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + epsilon)
            return false;
        if (Math.abs(d.x * c.y - d.y * c.x) > e.x * ad.y + e.y * ad.x + epsilon)
            return false;
 
        return true;
    }
    
	public static List<APIPlayer> getPlayersInModMode() {
		return APIPlayer.getAPIPlayers().values().stream().filter(apiPlayer -> apiPlayer.isInModMode()).collect(Collectors.toCollection(() -> new ArrayList<>(10)));
	}
	
    private static <T> T mergeUnsupported(T valueA, T valueB) {
        throw new UnsupportedOperationException("This Collector does not support merging.");
    }

    private static <A> A combineUnsupported(A accumulatorA, A accumulatorB) {
        throw new UnsupportedOperationException("This Collector does not support parallel streams.");
    }
	
    public static <T, K, V> Collector<T, ?, HashMap<K, V>> toSizedMap(
            Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper, int size) {
        return toSequentialMap(
                () -> Maps.newHashMapWithExpectedSize(size),
                keyMapper, valueMapper, Collector.Characteristics.UNORDERED
        );
    }
    
    public static <T, K, V, M extends Map<K, V>> Collector<T, ?, M> toSequentialMap(
            Supplier<M> mapSupplier, Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends V> valueMapper, Collector.Characteristics... characteristics) {
        return Collector.of(
                mapSupplier,
                (map, element) -> map.merge(
                        keyMapper.apply(element), valueMapper.apply(element), Methods::mergeUnsupported
                ),
                Methods::combineUnsupported,
                characteristics
        );
    }

	public static boolean isDebuff(ThrownPotion tp) {
		for (PotionEffect effect : tp.getEffects()) {
			if (ConfigurationService.DEBUFF_EFFECTS.contains(effect.getType())) 
				return true;
		}
		return false;
	}
	
	public static int getRandomWithExclusion(SplittableRandom rnd, int start, int end, int... exclude) {
	    int random = start + rnd.nextInt(end - start + 1 - exclude.length);
	    
	    for (int ex : exclude) {
	    	if (random < ex)
	            break;
	        
	    	random++;
	    }
	    return random;
	}
	
	public static int getRandomWithExclusion(int start, int end, int... exclude) {
	    int random = start + ThreadLocalRandom.current().nextInt(end - start + 1 - exclude.length);
	    
	    for (int ex : exclude) {
	    	if (random < ex)
	            break;
	        
	    	random++;
	    }
	    return random;
	}
	
	public static void boost(LivingEntity entity, double boostMultiplierX, double boostMultiplierY, double boostMultiplierZ) {
		double pitch = ((entity.getLocation().getPitch() + 90) * Math.PI) / 180;
		double yaw = ((entity.getLocation().getYaw() + 90) * Math.PI) / 180;
		
		entity.setVelocity(new Vector(Math.sin(pitch) * Math.cos(yaw) + boostMultiplierX, Math.cos(pitch) + boostMultiplierY, Math.sin(pitch) * Math.sin(yaw) + boostMultiplierZ));
	}
	
	public static void boost(LivingEntity entity, double boostMultiplier) {
		double pitch = ((entity.getLocation().getPitch() + 90) * Math.PI) / 180;
		double yaw = ((entity.getLocation().getYaw() + 90) * Math.PI) / 180;
		
		entity.setVelocity(new Vector(Math.sin(pitch) * Math.cos(yaw), Math.cos(pitch), Math.sin(pitch) * Math.sin(yaw)).multiply(boostMultiplier));
	}
	
	public static int menuSizeFromInteger(int size) {
		if (size <= 9)
			return 9;
		else if (size <= 18)
			return 18;
		else if (size <= 27)
			return 27;
		else if (size <= 36)
			return 36;
		else if (size <= 45)
			return 45;
		else return 54;
	}

	public static long getIdleTime(Player target) {
		long idleTime = ((CraftPlayer) target).getHandle().x();
		return (idleTime > 0L) ? (System.currentTimeMillis() - idleTime) : 0L;
	}
	
    public static Integer getKills(UUID uuid) {
        if (OfflineAPIPlayer.isLoaded(uuid))
            return OfflineAPIPlayer.getOfflineAPIPlayers().get(uuid).getKills();
        else
        	return kills.get(uuid);
    }
    
    public static void initKills(OfflineAPIPlayer offline) {
    	kills.put(offline.getUUID(), offline.getKills());
    }
    
    public static void initKillsOnStart() {
    	OnimaMongo.get(OnimaCollection.PLAYERS).find().iterator().forEachRemaining(document -> kills.put(UUID.fromString(document.getString("uuid")), ((Number) document.get("kills")).intValue()));
    }
    
    public static void removeKills(OfflineAPIPlayer offline) {
    	kills.remove(offline.getUUID());
    }
    
    public static String getName(OfflineAPIPlayer offline, boolean disguise) {
    	if (!offline.isOnline())
    		return offline.getName();
    	else
    		return ((APIPlayer) offline).getDisplayName(disguise);
    }
    
    public static String getName(CommandSender sender, boolean disguise) {
    	if (sender instanceof Player)
    		return APIPlayer.getPlayer((Player) sender).getDisplayName(disguise);
    	else
    		return sender.getName();
    }
    
    public static String getName(CommandSender sender) {
    	return getName(sender, false);
    }
    
    public static String getName(OfflineAPIPlayer offline) {
    	return getName(offline, false);
    }
    
    public static String getNameFromArg(OfflineAPIPlayer offline, String arg) {
    	if (offline.isOnline()) {
    		String name = getName(offline);
    		
    		if (name.equalsIgnoreCase(arg))
    			return name;
    	}
    	
    	return offline.getName();
    }
    
    public static String getNameFromArg(OfflinePlayer sender, String arg) {
    	if (sender.isOnline()) {
    		String name = getName((Player) sender);
    		
    		if (name.equalsIgnoreCase(arg))
    			return name;
    	}
    	
    	return sender.getName();
    }
    
    public static String getNameFromArg(CommandSender sender, String arg) {
    	String name = getName(sender);
    	
    	if (arg.equalsIgnoreCase(name))
    		return name;
    	else
    		return sender.getName();
    }
    
    public static String getRealName(OfflinePlayer offline) {
    	if (offline.isOnline())
    		return APIPlayer.getPlayer((Player) offline).getName();
    	else
    		return offline.getName();
    }
    
    public static String getRealName(CommandSender sender) {
    	if (sender instanceof Player)
    		return APIPlayer.getPlayer((Player) sender).getName();
    	else
    		return sender.getName();
    }
    
//	public static String getItemName(ItemStack item) {
//		net.minecraft.server.v1_7_R4.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
//		
//		return nmsItem.getItem().a(nmsItem);
//	}
	 
}
