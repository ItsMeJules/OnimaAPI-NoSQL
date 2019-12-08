package net.onima.onimaapi.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import net.minecraft.util.com.google.common.collect.Maps;
import net.onima.onimaapi.rank.RankType;
import net.onima.onimaapi.utils.time.Time;

/**
 * This class contains all the variables needed.
 */
public class ConfigurationService {
	
	/**
	 * Prefix of OnimaAPI plugin: <i>§e[§dOnimaAPI§e]</i>.
	 */
	public static final String ONIMAAPI_PREFIX = "§e[§dOnimaAPI§e]§r";
	
	/**
	 * Prefix of OnimaBoard plugin: <i>§e[§dOnimaBoard§e]</i>.
	 */
	public static final String ONIMABOARD_PREFIX = "§e[§dOnimaBoard§e]§r";
	
	/**
	 * Prefix of OnimaFaction plugin: <i>§e[§dOnimaFaction§e]</i>.
	 */
	public static final String ONIMAFACTION_PREFIX = "§e[§dOnimaFaction§e]§r";
	
	/**
	 * Prefix of OnimaGames plugin: <i>§e[§dOnimaGames§e]</i>.
	 */
	public static final String ONIMAGAMES_PREFIX = "§e[§dOnimaGames§e]§r";
	
	/**
	 * Prefix of OnimaDB plugin: <i>§e[§dOnimaDB§e]</i>.
	 */
	public static final String ONIMADB_PREFIX = "§e[§dOnimaDB§e]§r";
	
	/**
	 * Straight line: <i>§m-------------------------</i>
	 */
	public static final String STAIGHT_LINE = "§m-------------------------";
	
	/**
	 * Format date: <i>dd/MM/yyyy - HH:mm:ss</i>
	 */
	public static final String DATE_FORMAT_HOURS = "dd/MM/yyyy - HH:mm:ss";
	
	/**
	 * Format date: <i>dd/MM/yyyy</i>
	 */
	public static final String DATE_FORMAT_NO_HOURS = "dd/MM/yyyy";
	
	/**
	 * The Wilderness name: <i>§aNature</i>
	 */
	public static final String WILDERNESS_NAME = "§aNature";
	
	/**
	 * The WarZone name: <i>§cOctogone</i>
	 */
	public static final String WARZONE_NAME = "§cOctogone";
	
	/**
	 * The SafeZone name: <i>§6Spawn</i>
	 */
	public static final String SAFEZONE_NAME = "§6Spawn";
	
	/**
	 * The minimum DTR: <i>-5F</i>
	 */
	public static final float MIN_DTR = -5F;
	
	/**
	 * The maximum DTR: <i>5.0F</i>
	 */
	public static final float MAX_DTR = 5F;
	
	/**
	 * The DTR of a solo faction: <i>1.10F</i>
	 */
	public static final float SOLO_DTR = 1.01F;
	
	/**
	 * The DTR a player adds when joining a faction: <i>0.51F</i>
	 */
	public static final float PLAYER_DTR = 0.51F;
	
	/**
	 * The world names from the environment.<br>
	 * Normal: <i>Overworld</i><br>
	 * Nether: <i>Nether</i><br>
	 * THE_END: <i>End</i><br>
	 */
	public static final Map<Environment, String> ENVIRONMENT_NAME = ImmutableMap.of(Environment.NORMAL, "Overworld", Environment.NETHER, "Nether", Environment.THE_END, "End");
	
	/**
	 * The symbol for money: <i>$</i>
	 */
	public static final char MONEY_SYMBOL = '$';
	
	/**
	 * The amount of max claims for a faction: <i>16</i>
	 */
	public static final int MAX_CLAIMS = 16;
	
	/**
	 * The max width for a claim: <i>32</i>
	 */
	public static final int CLAIM_MAX_WIDTH = 32;
	
	/**
	 * The max length for a claim: <i>32</i>
	 */
	public static final int CLAIM_MAX_LENGTH = 32;
	
	/**
	 * The min length for a claim: <i>5</i>
	 */
	public static final int CLAIM_MIN_LENGTH = 5;
	
	/**
	 * The min width for a claim: <i>5</i>
	 */
	public static final int CLAIM_MIN_WIDTH = 5;
	
	/**
	 * The buffer radius for a claim: <i>4</i>
	 */
	public static final int CLAIM_BUFFER_RADIUS = 4;
	
	/**
	 * Can you claim beside a road ? : <i>false</i>
	 */
	public static final boolean CLAIM_ALLOWED_BESIDE_ROAD = false;
	
	/**
	 * The minimal height where a claim will go: <i>0</i>
	 */
	public static final int CLAIM_MIN_HEIGHT = 0;
	
	/**
	 * The maximal height where a claim will go: <i>256</i>
	 */
	public static final int CLAIM_MAX_HEIGHT = 256;
	
	/**
	 * The claimable worlds: <i>world</i>
	 */
	public static final List<String> CLAIMABLE_WORLD = ImmutableList.of("world");
	
	/**
	 * The stuck radius to move in: <i>5</i>
	 */
	public static final int STUCK_RADIUS = 5;

	/**
	 * The stuck search radius: <i>25</i>
	 */
	public static final int STUCK_SEARCH_RADIUS = 25;

	/**
	 * The crowbar name: <i>§6Crowbar</i>
	 */
	public static final String CROWBAR_NAME = "§6Crowbar";
	
	/**
	 * The crowbar lore: <br>
	 * &nbsp; <i>- §bSpawners : %spawner%</i><br>
	 * &nbsp; <i>- §bEnder Portals : %portals%</i>
	 */
	public static final List<String> CROWBAR_LORE = ImmutableList.of("§e§m-------------", "§cSpawners §f: §e%spawners%", "§aEnder Portals §f: §e%portals%");
	
	/**
	 * The crowbar gived by line: <i>§eCrowbar give par :</i>
	 */
	public static final String CROWBAR_GIVE_LINE = "§7§oCrowbar give par :";

	/**
	 * The stat pickaxe name: <i>§ePioche StatTrak™</i>
	 */
	public static final String STAT_PICKAXE_NAME = "§ePioche StatTrak™";

	/**
	 * The stat pickaxe lore:<br>
	 * &nbsp; - <i>§f§m--------§7[§6Minerais minés§7]§f§m--------</i><br>
	 * &nbsp; - <i>§bDiamant : §f%diamond%</i><br>
	 * &nbsp; - <i>§aEmeraude : §f%emerald%</i><br>
	 * &nbsp; - <i>§eOr : §f%gold%</i><br>
	 * &nbsp; - <i>§4Redstone : §f%redstone%</i><br>
	 * &nbsp; - <i>§9Lapis : §f%lapis%</i><br>
	 * &nbsp; - <i>§7Fer : §f%iron%</i><br>
	 * &nbsp; - <i>§8Charbon : §f%coal%</i><br>
	 */
	public static final List<String> STAT_PICKAXE_LORE = ImmutableList.of("§f§m--------§7[§6Minerais minés§7]§f§m--------",
			"§bDiamant : §f%diamond%", "§aEmeraude : §f%emerald%", "§eOr : §f%gold%", "§4Redstone : §f%redstone%",
			"§9Lapis : §f%lapis%", "§7Fer : §f%iron%", "§8Charbon : §f%coal%", "§fQuartz : §f%quartz%"); 
	
	/**
	 * The stat sword first line: <i>§eJoueurs tués par cette épée :</i>
	 */
	public static final String STAT_SWORD_FIRST_LINE = "§eJoueurs tués par cette épée :";
	
	/**
	 * The stat sword killed line: <i>§e%killer% §7a tué §e%victim% §5» §7%date%</i>
	 */
	public static final String STAT_SWORD_KILL_LINE = "§e%killer% §fa tué §e%victim% §f» §7%date%";

	/**
	 * The stat armor first line: <i>§eJoueurs morts avec cette armure :</i>
	 */
	public static final String STAT_ARMOR_FIRST_LINE = "§eJoueurs morts avec cette armure :";
	
	/**
	 * The stat killed by a player line: <i>§e%victim% §7a été tué par §e%killer% §5» §7%date%</i>
	 */
	public static final String STAT_ARMOR_KILLED_LINE = "§e%victim% §fa été tué par §e%killer% §f» §7%date%";
	
	/**
	 * The stat armor dead line: <i>§e%victim% est mort §5» §7%date%</i>
	 */
	public static final String STAT_ARMOR_DEATH_LINE = "§e%victim% §fest mort §f» §7%date%";
	
	/**
	 * The wand name: <i>§6Selection Wand</i>
	 */
	public static final String WAND_NAME = "§6Selection Wand";

	/**
	 * The wand lore:<br>
	 * &nbsp; - <i>§7Clique gauche pour sélectionner la première location.</i><br>
	 * &nbsp; - <i>§7Clique droit pour sélectionner la seconde location.</i><br>
	 */
	public static final List<String> WAND_LORE = ImmutableList.of("§7Clique gauche pour sélectionner la première location.", "§7Clique droit pour sélectionner la seconde location.");

	/**
	 * The logger health: <i>40.0D</i>
	 */
	public static final double LOGGER_HEALTH = 40.0D;

	/**
	 * The logger remove time in seconds: <i>30</i>
	 */
	public static final long LOGGER_REMOVE_TIME = 30;

	/**
	 * The death sign name: <i>§5Death-Sign</i>
	 */
	public static final String DEATH_SIGN_NAME = "§5Death-Sign";
	
	/**
	 * The death sign lore:<br>
	 * &nbsp; - <i>§4%killer%</i><br>
	 * &nbsp; - <i>§ea tué</i><br>
	 * &nbsp; - <i>§4%victim%</i><br>
	 * &nbsp; - <i>§r%date%</i><br>
	 */
	public static final List<String> DEATH_SIGN_LINES = ImmutableList.of("§4%killer%", "§ea tué", "§4%victim%", "§r%date%");
	
	/**
	 * The elevator sign lore:
	 * &nbsp; - <i>§e[Ascenseur]</i><br>
	 * &nbsp; - <i>%up%</i><br>
	 * &nbsp; - <i>%usage%</i><br>
	 */
	public static final List<String> ELEVATOR_SIGN_LINES = ImmutableList.of("§e[Ascenseur]", "%up%", "%usage%");
	
	/**
	 * The elevator sign up: <i>§aUP</i>
	 */
	public static final String ELEVATOR_SIGN_UP = "§aHAUT";
	
	/**
	 * The elevator sign down: <i>§cBAS</i>
	 */
	public static final String ELEVATOR_SIGN_DOWN = "§cBAS";
	
	/**
	 * The elevator sign cant use: <i>§cInutilisable</i>
	 */
	public static final String ELEVATOR_SIGN_CANT_USE = "§cInutilisable";
	
	/**
	 * The elevator sign can use: <i>§aUtilisable</i>
	 */
	public static final String ELEVATOR_SIGN_CAN_USE = "§aUtilisable";
	
	/**
	 * The shop sign first buy line: <i>§a[Acheter]</i>
	 */
	public static final String SHOP_SIGN_BUY_LINE = "§a[Acheter]";
	
	/**
	 * The shop sign first sell line: <i>§c[Vendre]</i>
	 */
	public static final String SHOP_SIGN_SELL_LINE = "§c[Vendre]";
	
	/**
	 * The shop sign sell lines:<br>
	 * &nbsp; - <i>§aVENDU§0 %amount%</i><br>
	 * &nbsp; - <i>pour §a%price% + {@link #MONEY_SYMBOL}</i><br>
	 * &nbsp; - <i>Balance :</i><br>
	 * &nbsp; - <i>§a%balance% + {@link #MONEY_SYMBOL}</i><br>
	 */
	public static final List<String> SHOP_SIGN_SELL_LINES = ImmutableList.of("§aVENDU§0 %amount%", "pour §a%price%" + MONEY_SYMBOL, "Balance :", "§a%balance%" + MONEY_SYMBOL);
	
	/**
	 * The shop buy sign lines:<br>
	 * &nbsp; - <i>§9ACHETE§0 %amount%</i><br>
	 * &nbsp; - <i>pour §9%price% + {@link #MONEY_SYMBOL}</i><br>
	 * &nbsp; - <i>Balance :</i><br>
	 * &nbsp; - <i>§a%balance% + {@link #MONEY_SYMBOL}</i><br>
	 */
	public static final List<String> SHOP_SIGN_BUY_LINES = ImmutableList.of("§9ACHETE§0 %amount%", "pour §9%price%" + MONEY_SYMBOL, "Balance :", "§9 %balance%" + MONEY_SYMBOL);
	
	/**
	 * The shop sign item missing lines:<br>
	 * &nbsp; - <i>§cVous n'avez</i><br>
	 * &nbsp; - <i>§caucun</i><br>
	 * &nbsp; - <i>%id%</i><br>
	 * &nbsp; - <i>§csur vous !</i><br>
	 */
	public static final List<String> SHOP_SIGN_ITEM_MISSING_LINES = ImmutableList.of("§cVous n'avez", "§caucun", "%id%", "§csur vous !");
	
	/**
	 * The shop sign balance banned lines:<br>
	 * &nbsp; - <i>§cVotre balance</i><br>
	 * &nbsp; - <i>§cest bannie</i><br>
	 * &nbsp; - <i>§cvous ne pouvez</i><br>
	 * &nbsp; - <i>§cpas faire ça !</i><br>
	 */
	public static final List<String> SHOP_SIGN_BALANCE_BANNED_LINES = ImmutableList.of("§cVotre balance", "§cest bannie", "§cvous ne pouvez", "§cpas faire ça !");
	
	/**
	 * The shop sign not enough money lines:<br>
	 * &nbsp; - <i>§cVous n'avez</i><br>
	 * &nbsp; - <i>§cpas assez</i><br>
	 * &nbsp; - <i>§cd'argent</i><br>
	 * &nbsp; - <i>§c%balance% + {@link #MONEY_SYMBOL} + manquant</i><br>
	 */
	public static final List<String> SHOP_SIGN_NOT_ENOUGH_MONEY_LINES = ImmutableList.of("§cVous n'avez", "§cpas assez", "§cd'argent", "§c%balance%" + MONEY_SYMBOL + " manquant");

	/**
	 * The first subclaim sign line: <i>§a[Coffre privé]</i>
	 */
	public static final String SUBCLAIM_SIGN_LINE = "§a[Coffre privé]";
	
	/**
	 * The enderpeaerl cooldown name on the item: <i>§dCooldown enderpearl : §e</i>
	 */
	public static final String ENDERPEARL_COOLDOWN_ITEM_NAME = "§dCooldown enderpearl : §e";

	/**
	 * The keys name: <i>§dClef §7(§e%crate%§7)</i>
	 */
	public static final String KEY_NAME = "§dClef §7(§e%crate%§7)";

	/**
	 * The keys lore:<br>
	 * &nbsp; - <i>§eClef §dde crate &7obtenue le §e%date% §7par §d§o%player%</i><br>
	 * &nbsp; - <i>§6Allez au spawn pour récupérer votre récompense.</i>
	 */
	public static final List<String> KEY_LORE = ImmutableList.of(/*"§eClef §dde crate §7obtenue le §e%date% §7par §d§o%player%", */"§6Allez au spawn pour récupérer votre récompense.");

	/**
	 * The keys booster in the lore: <i>§eBooster x§c%booster%</i>
	 */
	public static final String KEY_BOOSTER = "§eBooster %booster%";
	
	/**
	 * The DTC server list lines:<br>
	 * &nbsp; - <i>§eDTC en cours : %name% %points%</i>
	 */
	public static final List<String> DTC_SERVER_LIST_LINE = ImmutableList.of("§eDTC en cours : %name% %points%");
	
	/**
	 * The Koth server list lines:<br>
	 * &nbsp; - <i>§eKoth en cours : %name% %points%</i>
	 */
	public static final List<String> KOTH_SERVER_LIST_LINE = ImmutableList.of("§9Koth en cours : %name% %time%");
	
	/**
	 * The Dragon server list lines:<br>
	 * &nbsp; - <i>§bDragon en cours : %health%%</i>
	 */
	public static final List<String> DRAGON_SERVER_LIST_LINE = ImmutableList.of("§bDragon en cours : %health%%");
	
	/**
	 * The worlds allowed to spawn an enderdragon: <i>world_the_end</i>
	 */
	public static final List<String> ENDERDRAGON_EVENT_WORLDS = ImmutableList.of("world_the_end");

	/**
	 * The Conquest server list lines:<br>
	 * &nbsp; - <i>§3Conquest en cours : %%name% %points-to-win%</i>
	 */
	public static final List<String> CONQUEST_SERVER_LIST_LINE = ImmutableList.of("§3Conquest en cours : %name% %points-to-win%");

	/**
	 * The Citadel server list lines:<br>
	 * &nbsp; - <i>§5Citadel en cours : %name% %time%</i>
	 */
	public static final List<String> CITADEL_SERVER_LIST_LINE = ImmutableList.of("§5Citadel en cours : %name% %time%");

	/**
	 * The amount of points to lose when a faction member dies on a conquest event: <i>20</i>
	 */
	public static final short CONQUEST_POINTS_DEATH = 20;

	/**
	 * The sound sent when the dtc is broken : <i>new OSound(Sound.ENDERDRAGON_HIT, 2F, 10F)</i>
	 */
	public static final OSound DTC_BREAK_SOUND = new OSound(Sound.ENDERDRAGON_HIT, 2F, 10F);

	/**
	 * The koth knock announce delay to send one : <i>25 * Time.SECOND</i>
	 */
	public static final long KOTH_KNOCK_ANNOUNCE_DELAY = 25 * Time.SECOND;

	/**
	 * The citadel knock announce delay to send one : <i>50 * Time.SECOND</i>
	 */
	public static final long CITADEL_KNOCK_ANNOUNCE_DELAY = 50 * Time.SECOND;

	/**
	 * The no faction character: <i>§c*§r</i>
	 */
	public static final String NO_FACTION_CHARACTER = "§6[§d*§6]§r";
	
	/**
	 * All the debuff effects.
	 */
	public static final Set<PotionEffectType> DEBUFF_EFFECTS = Sets.newHashSet(PotionEffectType.BLINDNESS, PotionEffectType.CONFUSION, PotionEffectType.HARM, PotionEffectType.HUNGER, PotionEffectType.POISON, PotionEffectType.SLOW, PotionEffectType.SLOW_DIGGING, PotionEffectType.WEAKNESS, PotionEffectType.WITHER);

	/**
	 * All the effects with their minecraft name as a value.
	 */
	public static final Map<PotionEffectType, String> EFFECTS_NICE_NAME = ImmutableMap.<PotionEffectType, String>builder().put(PotionEffectType.WITHER, "Wither").put(PotionEffectType.WEAKNESS, "Weakness").put(PotionEffectType.WATER_BREATHING, "Water Breathing").put(PotionEffectType.SPEED, "Speed").put(PotionEffectType.SLOW_DIGGING, "Mining Fatigue").put(PotionEffectType.SLOW, "Slowness").put(PotionEffectType.SATURATION, "Saturation").put(PotionEffectType.REGENERATION, "Regeneration").put(PotionEffectType.POISON, "Poison").put(PotionEffectType.NIGHT_VISION, "Night Vision").put(PotionEffectType.JUMP, "Jump Boost").put(PotionEffectType.INVISIBILITY, "Invisibility").put(PotionEffectType.INCREASE_DAMAGE, "Strength").put(PotionEffectType.HUNGER, "Hunger").put(PotionEffectType.HEALTH_BOOST, "Health Boost").put(PotionEffectType.HEAL, "Instant Health").put(PotionEffectType.HARM, "Instant Damage").put(PotionEffectType.FIRE_RESISTANCE, "Fire Resistance").put(PotionEffectType.FAST_DIGGING, "Haste").put(PotionEffectType.DAMAGE_RESISTANCE, "Resistance").put(PotionEffectType.CONFUSION, "Nausea").put(PotionEffectType.BLINDNESS, "Blindness").put(PotionEffectType.ABSORPTION, "Absorption").build();
	
	/**
	 * The amount of command displayer per page : <i>8</i>.
	 */
	public static final int HELP_PER_PAGE = 8;

	/**
	 * The wand selection 1 sound: <i>new OSound(Sound.ORB_PICKUP, 1.0F, 0.5F)</i>
	 */
	public static final OSound WAND_SELECT1_SOUND = new OSound(Sound.ORB_PICKUP, 1.0F, 0.5F);
	
	/**
	 * The wand selection 1 sound: <i>new OSound(Sound.ORB_PICKUP, 0.5F, 0.5F)</i>
	 */
	public static final OSound WAND_SELECT2_SOUND = new OSound(Sound.ORB_PICKUP, 0.5F, 0.5F);

	/**
	 * All the faction names blocked.
	 */
	public static final List<String> BLOCKED_FACTION_NAMES = ImmutableList.<String>builder().add("fuck", "eze", "eez", "nigga", "all", "fdp", "connard", "encule", "enculer", "filsdepute", "filsdeput", "negre", "bite", "chibre", "penis", "gay").build();

	/**
	 * The faction name max length : <i>16</i>
	 */
	public static final int FACTION_NAME_MAX_LENGTH = 16;
	
	/**
	 * The faction name min length : <i>3</i>
	 */
	public static final int FACTION_NAME_MIN_LENGTH = 3;

	/**
	 * The amount of members in the faction <i>20</i>.
	 */
	public static final int FACTION_MAX_MEMBERS = 20;

	/**
	 * The claiming wand name: <i>§cBaguette de claim</i>
	 */
	public static final String CLAIMING_WAND_NAME = "§cBaguette de claim";

	/**
	 * The claiming wand lore:
	 * &nbsp; - <i>§aClick gauche et click droit sur un block pour sélectionner les locations.</i>
	 * &nbsp; - <i>§aClick droit dans l'air pour annuler la sélection.</i>
	 * &nbsp; - <i>§eShift click gauche pour valider les sélections et claim.</i>
	 */
	public static final List<String> CLAIMING_WAND_LORE = ImmutableList.of("§aClick gauche et click droit sur un block pour sélectionner les locations.", "§aClick droit dans l'air pour annuler la sélection.", "§eShift click gauche pour valider les sélections et claim.");

	/**
	 * The amount of maximum allies: <i>0</i>
	 */
	public static final int FACTION_MAX_ALLY = 0;

	/**
	 * Dtr multiplier when all members are outside the claims: <i>2F</i>
	 */
	public static final float DTR_TO_MULTIPLIER_OUTSIDE_CLAIMS = 2F;
	
	/**
	 * Dtr added per updates: <i>0.1F</i>
	 */
	public static final float DTR_TO_ADD_PER_UPDATE = 0.1F;

	/**
	 * The time to take to update the DTR (seconds): <i>60</i>
	 */
	public static final int DTR_UPDATE_TIME = 60;
	
	/**
	 * The console uuid: <i>29f26148-4d55-4b4b-8e07-900fda686a67</i>
	 */
	public static final UUID CONSOLE_UUID = UUID.fromString("29f26148-4d55-4b4b-8e07-900fda686a67");
	
	/**
	 * The radius of the different spawns :
	 * &nbsp; - <i>Normal : 128</i>
	 * &nbsp; - <i>Nether : 64</i>
	 */
	public static final Map<Environment, Integer> SPAWN_RADIUS_ENV = ImmutableMap.<Environment, Integer>builder().put(Environment.NORMAL, 64).put(Environment.NETHER, 65).build();
	
	/**
	 * The length of the roads :
	 * &nbsp; - <i>Normal : 4000</i>
	 * &nbsp; - <i>Nether : 4000</i>
	 */
	public static final Map<Environment, Integer> ROAD_LENGTH_ENV = ImmutableMap.<Environment, Integer>builder().put(Environment.NORMAL, 5056).put(Environment.NETHER, 3878).build();

	/**
	 * The length of the roads :
	 * &nbsp; - <i>Normal : 20</i>
	 * &nbsp; - <i>Nether : 7</i>
	 */
	public static final Map<Environment, Integer> ROAD_HALF_WIDTH = ImmutableMap.<Environment, Integer>builder().put(Environment.NORMAL, 20).put(Environment.NETHER, 7).build();

	/**
	 * Interactables blocks in spawn.
	 */
	public static final List<Material> SPAWN_INTERACTABLES_BLOCKS = ImmutableList.of(Material.WORKBENCH);
	
	/**
	 * Interactables blocks in an other territory than yours (except spawn).
	 */
	public static final List<Material> NOT_CLAIM_INTERACTABLES_BLOCKS = ImmutableList.of(Material.WORKBENCH);

	public static final double BARD_BARDING_DISTANCE = 20;
	
	public static final TreeMap<Integer, String> NUMBER_ROMAN_VALUES = new TreeMap<Integer, String>(Maps.newHashMap(ImmutableMap.<Integer, String>builder().put(1000, "M").put(900, "CM").put(500, "D").put(400, "CD").put(100, "C").put(90, "XC").put(50, "L").put(40, "XL").put(10, "X").put(9, "IX").put(5, "V").put(4, "IV").put(1, "I").build()));

	public static final String SCOREBOARD_BAR = "§7§m--*------------------*------";

	public static final RankType DEFAULT_RANK = RankType.DEFAULT;

	public static final double DEFAULT_BALANCE = 250D;

	public static final long FACTION_RENAME_TIME = 15 * Time.SECOND;

	public static final String SQL_ERROR_LOADING_DATA = "§f§m-----------------------------"
			+ "\n§fIl y a eu une erreur lors du chargement de"
			+ "\n§fvos données, réessayez dans un instant."
			+ "\n§f§m-----------------------------";

	public static final int DISTANCE_BARRIER_HORIZONTAL = 10;
	
	public static final int DISTANCE_BARRIER_ABOVE = 4;
	
	public static final int DISTANCE_BARRIER_BELOW = 3;

	public static final OSound PRIVATE_MESSAGE_SOUND = new OSound(Sound.ORB_PICKUP, 1, 1);

	public static final short BREWING_MULTIPLIER = 3;

	public static final short FURNACE_MULTIPLIER = 3;

	public static final double EXP_MULTIPLIER_LOOTING_PER_LEVEL = 1.5D;

	public static final double EXP_MULTIPLIER_FORTUNE_PER_LEVEL = 1.5D;

	public static final double EXP_MULTIPLIER_GENERAL = 2.0D;

	public static final double EXP_MULTIPLIER_FISHING = 2.0D;

	public static final double EXP_MULTIPLIER_LUCK_PER_LEVEL = 2.0D;

	public static final double EXP_MULTIPLIER_SMELTING = 2.0D;

	public static final List<EntityType> NOT_STACKABLE_MOBS = ImmutableList.of(EntityType.CREEPER, EntityType.ENDERMAN, EntityType.HORSE, EntityType.SLIME, EntityType.VILLAGER, EntityType.BAT);
	
	public static final double ENTITY_STACK_RADIUS = 15.0D;

	public static final int ENTITY_MAX_STACK = 150;
	
	public static final int MAX_SPAWNED_CHUNK_ENTITIES = 30;
	
	public static final int MAX_ITEM_CHUNK_ENTITIES = 100;
	
	public static final int MAX_NATURAL_CHUNK_ENTITIES = 15;
	
	public static final int MAX_CHUNK_GENERATED_ENTITIES = 20;

	public static final String CRATE_PRIZE_LORE_LINE = "%rarity% §r- §e§lChance : §c%chance%%";

	public static final String SPAWNER_NAME = "§a%spawner% spawner";

	public static final String DEATHBAN_KICK_MESSAGE = "%death-message%"
			+ "\n§cVous avez été deathban le %death-time%"
			+ "\n§eRevenez dans %time-left%";

	public static final float DTR_LOSS = 1.0F;

	public static final long REGEN_COOLDOWN = Time.HOUR;

	public static final double REWARD_PER_KILL = 250D;

	public static final String DEATHBAN_KICK_CONNECT_MESSAGE = "%death-message%"
			+ "\n§cVous avez été deathban le %death-time%"
			+ "\n§eRevenez dans %time-left%"
			+ "\nOu reconnectez-vous avant 15 secondes pour utiliser une vie";

	public static final String COMBAT_LOGGER_NAME = "%player%";

	public static final double COMBAT_LOGGER_ENEMIES_DISTANCE = 64D;

	public static final String SERVER_NOT_LOADED = "Le serveur est entrain de démarrer.";

	public static final String EOTW_DEATHBANNED = "§cDeathban jusqu'à la fin de la map car"
			+ "\n§ccar vous êtes mort durant l'EOTW."
			+ "\n§fRevenez demain pour le SOTW.";
}