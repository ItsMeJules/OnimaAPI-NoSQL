package net.onima.onimaapi.crates.utils;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.crates.PhysicalCrate;
import net.onima.onimaapi.crates.RoomCrate;
import net.onima.onimaapi.crates.SupplyCrate;
import net.onima.onimaapi.crates.VirtualCrate;
import net.onima.onimaapi.crates.booster.KeyBooster;
import net.onima.onimaapi.crates.booster.NoBooster;
import net.onima.onimaapi.crates.prizes.CommandPrize;
import net.onima.onimaapi.crates.prizes.ItemPrize;
import net.onima.onimaapi.crates.prizes.Prize;
import net.onima.onimaapi.crates.prizes.PrizeCalculator;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.DisplayButton;
import net.onima.onimaapi.manager.ConfigManager;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.saver.FileSaver;
import net.onima.onimaapi.utils.CasualFormatDate;
import net.onima.onimaapi.utils.Config;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.OSound;
import net.onima.onimaapi.zone.Cuboid;

public abstract class Crate implements FileSaver {

	public static final double MAX_ITEMS_WEIGHT;
	public static final KeyBooster NO_BOOSTER;
	public static final OSound WIN_SOUND;
	
	protected static List<Crate> crates;
	protected static Config crateConfig;
	protected static FileConfiguration crateFile;
	protected static boolean refreshed;
	
	
	static {
		MAX_ITEMS_WEIGHT = 100D;
		NO_BOOSTER = new NoBooster();
		WIN_SOUND = new OSound(Sound.LEVEL_UP, 1F, 1F);
		
		crates = new ArrayList<>();
		crateConfig = ConfigManager.getCrateSerialConfig();
		crateFile = crateConfig.getConfig();
	}
	
	protected String name, displayName;
	protected List<Prize> prizes;
	protected int prizeAmount;
	protected CrateType type;
	
	{
		prizes = new ArrayList<>();
	}
	
	public Crate(String name, CrateType type, int prizeAmount) {
		this.name = name;
		this.type = type;
		this.prizeAmount = prizeAmount;
		save();
	}
	
	public void open(APIPlayer player, KeyBooster booster) {
		player.setCrateOpening(this);
	}
	
	public void close(APIPlayer player) {
		player.setCrateOpening(null);
	}
	
	public List<Prize> getPrizes(KeyBooster booster) {
		List<Prize> won = new ArrayList<>(prizeAmount);
		List<PrizeCalculator> calculator = new ArrayList<>(prizes.size());
		
		double weight = 0;

		if (booster == null)
			booster = NO_BOOSTER;
		
		for (Prize prize : prizes) {
			weight += prize.getChance();
			calculator.add(new PrizeCalculator(weight, prize));
		}
		
		int iteration = prizeAmount;

		while (iteration != 0) {
			won.add(booster.apply(calculator, iteration));
			
			iteration--;
		}
		
		return won;
	}
	
	public abstract void cancel(APIPlayer player);
	public abstract void sendShow(CommandSender sender);
	
	public void preview(APIPlayer apiPlayer) {
		new PacketMenu(null, "§7Preview §e" + displayName, Methods.menuSizeFromInteger(prizes.size()), true) {
			
			@Override
			public void registerItems() {
				for (Prize prize : prizes)
					buttons.put(buttons.size(), new DisplayButton(prize.getDisplayItem()));
			}

		}.open(apiPlayer);
		
		apiPlayer.sendMessage("§dVous §7êtes entrain de regarder le contenu de la crate " + displayName);
	}
	
	public double getTotalWeight() {
		double weight = 0;
		
		for (Prize prize : prizes)
			weight += prize.getChance();
		
		return weight;
	}
	
	public String getName() {
		return name;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public List<Prize> getPrizes() {
		return prizes;
	}
	
	public int getPrizeAmount() {
		return prizeAmount;
	}
	
	public void setPrizeAmount(int prizeAmount) {
		this.prizeAmount = prizeAmount;
	}
	
	public CrateType getType() {
		return type;
	}

	@Override
	public boolean isSaved() {
		return crates.contains(this) && OnimaAPI.getShutdownSavers().contains(this);
	}
	
	@Override
	public void remove() {
		crates.remove(this);
		OnimaAPI.getShutdownSavers().remove(this);
		
		if (crates.size() == 0)
			crateConfig.remove("crates", false);
	}
	
	@Override
	public void save() {
		crates.add(this);
		OnimaAPI.getShutdownSavers().add(this);
	}
	
	@Override
	public void refreshFile() {
		List<String> crateNames = crates.stream().map(Crate::getName).collect(Collectors.toList());
		
		for (CrateType type : CrateType.values()) {
			ConfigurationSection section = crateFile.getConfigurationSection("crates." + type.name());
			
			if (section != null) {
				for (String name : section.getKeys(false)) {
					if (!crateNames.contains(name))
						crateConfig.remove("crates." + type.name() + '.' + name, false);
				}
			}
		}
		
		refreshed = true;
	}
	
	@Override
	public void serialize() {
		String path = "crates." + type.name() + '.' + name + '.';
		
		if (!refreshed)
			refreshFile();
		
		crateFile.set(path + "name", name);
		crateFile.set(path + "display-name", displayName == null ? null : displayName.replace("§", "&"));
		
		if (!prizes.isEmpty()) {
			List<String> items = new ArrayList<>();
			
			for (Prize prize : prizes)
				items.add(prize.asSerializableString());
			
			crateFile.set(path + "prizes", items);
		}
		
		crateFile.set(path + "prize-amount", prizeAmount);
	}
	
	public static String constructMultiPrize(List<Prize> prizes) {
		StringBuilder builder = new StringBuilder();
		Iterator<Prize> iterator = prizes.iterator();
		
		while (iterator.hasNext()) {
			Prize prize = iterator.next();
			ItemStack item = prize.getDisplayItem();
			
			builder.append("§r" + (item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : Methods.getItemName(item)));
			
			if (item.getAmount() > 1)
				builder.append(" §6x" + item.getAmount() + "§r");
			
			if (iterator.hasNext())
				builder.append("§r, ");
		}
		
		builder.append("§r");
		
		return builder.toString();
	}
	
	public static Crate getByName(String name) {
		for (Crate crate : crates) {
			if (crate.name.equalsIgnoreCase(name) 
					|| (crate.displayName != null && name.equalsIgnoreCase(crate.displayName))
					|| (crate.displayName != null && ChatColor.stripColor(crate.displayName).equalsIgnoreCase(name)))
				return crate;
		}
		
		return null;
	}
	
	public static List<Crate> getCrates() {
		return crates;
	}
	
	public static void deserialize() {
		ConfigurationSection crateSection = crateFile.getConfigurationSection("crates");
		int crates = 0;
		
		if (crateSection != null) {
			for (String typeName : crateSection.getKeys(false)) {
				CrateType type = CrateType.fromName(typeName);
				ConfigurationSection typeSection = crateSection.getConfigurationSection(typeName);
				
				if (typeSection == null)
					continue;
				
				for (String name : typeSection.getKeys(false)) {
					Integer prizeAmount = Methods.toInteger(typeSection.getString(name + ".prize-amount"));
					Crate crate = null;		
					
					switch (type) {
					case PHYSICAL:
						Block block = null;
						
						if (typeSection.getString(name + ".location") != null)
							block = Methods.deserializeLocation(typeSection.getString(name + ".location"), false).getBlock();
						
							crate = new PhysicalCrate(block, name, prizeAmount);
						break;
					case ROOM:
						Location tpLoc = null, loc = null;
						Cuboid cuboid = null;
						
						if (typeSection.getString(name + ".tp-location") != null)
							tpLoc = Methods.deserializeLocation(typeSection.getString(name + ".tp-location"), true);
						
						if (typeSection.getString(name + ".location") != null)
							loc = Methods.deserializeLocation(typeSection.getString(name + ".location"), true);
						
						if (typeSection.getString(name + ".cuboid-loc-min") != null) {
							cuboid = new Cuboid(Methods.deserializeLocation(typeSection.getString(name + ".cuboid-loc-min"), false),
									Methods.deserializeLocation(typeSection.getString(name + ".cuboid-loc-max"), false), false);
						}
						
						RoomCrate roomCrate = new RoomCrate(loc, name, prizeAmount);
						
						roomCrate.setTeleportLocation(tpLoc);
						roomCrate.setRoom(cuboid);
						
						crate = roomCrate;
						break;
					case SUPPLY:
						Long resetTime = typeSection.getLong(name + ".reset-time-cycle");
						Long nextStart = typeSection.getLong(name + ".next-start");
						
						SupplyCrate supply = new SupplyCrate(name, prizeAmount);
						
						if (resetTime != null && nextStart != null) {
							
							if (nextStart != -1) {
								while (nextStart <= System.currentTimeMillis())
									nextStart += resetTime;
								
								supply.setTemporal(ZonedDateTime.ofInstant(Instant.ofEpochMilli(nextStart), OnimaAPI.TIME_ZONE));
								OnimaAPI.sendConsoleMessage("§fProchain drop de la crate " + supply.name + ' ' + new CasualFormatDate("d u z hi").toNormalDate(nextStart), ConfigurationService.ONIMAAPI_PREFIX);
							}
							
							supply.scheduleEvery(resetTime);
						}
						
						supply.setSchedulerEnabled(typeSection.getBoolean(name + ".scheduler-enabled"));
						
						crate = supply;
						break;
					case VIRTUAL:
						crate = new VirtualCrate(name, prizeAmount);
						break;
					default:
						continue;
					}
					
					crate.displayName = Methods.colors(typeSection.getString(name + ".display-name"));
					List<Prize> prizes = new ArrayList<>();
					
					for (String line : typeSection.getStringList(name + ".prizes")) {
						String[] parts = line.split(";");
						
						prizes.add(parts.length == 5 ? CommandPrize.fromString(parts) : ItemPrize.fromString(parts));
					}
					
					crate.prizes = prizes;
					crates++;
				}
			}
		}
		
		OnimaAPI.sendConsoleMessage("§aNous avons chargé " + crates + " crate" + (crates > 1 ? "s" : ""), ConfigurationService.ONIMAAPI_PREFIX);
	}
	
}
