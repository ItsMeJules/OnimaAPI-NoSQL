package net.onima.onimaapi.signs;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.manager.ConfigManager;
import net.onima.onimaapi.saver.FileSaver;
import net.onima.onimaapi.utils.Config;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;

public abstract class HCFSign implements FileSaver {
	
	protected static List<HCFSign> signs;
	protected static OnimaAPI onimaAPI;
	protected static Config signSerialConfig;
	protected static FileConfiguration config;
	
	protected Sign sign;
	protected Block attachedBlock;
	
	static {
		signs = new ArrayList<>();
		onimaAPI = OnimaAPI.getInstance();
		signSerialConfig = ConfigManager.getSignSerialConfig();
		config = signSerialConfig.getConfig();
	}
	
	public HCFSign(Sign sign) {
		this.sign = sign;
		
		Block block = sign.getBlock();
		attachedBlock = block.getRelative(((org.bukkit.material.Sign) block.getState().getData()).getAttachedFace());
		
		save();
	}
	
	public Sign getSign() {
		return sign;
	}

	@Override
	public void save() {
		signs.add(this);
		OnimaAPI.getShutdownSavers().add(this);
	}

	@Override
	public void remove() {
		signs.remove(this);
		OnimaAPI.getShutdownSavers().remove(this);
	}

	@Override
	public boolean isSaved() {
		return signs.contains(this) && OnimaAPI.getShutdownSavers().contains(this);
	}
	
	public Block getAttachedBlock() {
		return attachedBlock;
	}

	public static boolean isSign(Block block) {
		return block.getType().toString().contains("SIGN");
	}
	
	public static List<HCFSign> getHCFSigns() {
		return signs;
	}
	
	public static HCFSign getSign(Sign sign) {
		for (HCFSign hcfSign : signs) {
			if (hcfSign.sign.equals(sign)) 
				return hcfSign;
		}
		return null;
	}
	
	public static HCFSign getSign(Location location) {
		for (HCFSign hcfSign : signs) {
			if (Methods.locationEquals(location, hcfSign.sign.getLocation())) 
				return hcfSign;
		}
		return null;
	}
	
	public static boolean removeSign(Sign sign) {
		return signs.removeIf(hcfSign -> hcfSign.sign.equals(sign));
	}
	
	public static void deserialize() {
		ConfigurationSection shopSection = config.getConfigurationSection("shop-signs"), elevatorSection = config.getConfigurationSection("elevator-signs");
		ConfigurationSection subclaimSection = config.getConfigurationSection("subclaim-signs");
		int signs = 0, signsShop = 0, elevatorSigns = 0, subclaimSigns = 0;
		
		if (shopSection != null) {
			for (String stringLoc : shopSection.getKeys(false)) {
				String path = "shop-signs."+stringLoc+".";
				
				Block block = Methods.deserializeLocation(stringLoc, false).getBlock();
				
				if (checkSign(block, path))
					continue;
			
				SignShop sign = new SignShop((Sign) block.getState(), config.getBoolean(path+"buy-sign"));
				sign.setStackSize(config.getInt(path+"stack-size"));
				sign.setMaterial(config.getInt(path+"material-id"), config.getInt(path+"material-damage"));
				sign.setPrice(config.getDouble(path+"price"));
				signsShop++;
			}
		}
		
		if (elevatorSection != null) {
			for (String stringLoc : elevatorSection.getKeys(false)) {
				String path = "elevator-signs."+stringLoc+".";
				Block block = Methods.deserializeLocation(stringLoc, false).getBlock();
				
				if (checkSign(block, path))
					continue;
				
				ElevatorSign sign = new ElevatorSign((Sign) block.getState(), config.getBoolean(path+"up"));
				sign.setTeleportLocation(config.getString(path+"teleport-location").isEmpty() ? null : Methods.deserializeLocation(config.getString(path+"teleport-location"), false));
				sign.initLocationToTeleport();
				elevatorSigns++;
			}
		}
		
		if (subclaimSection != null) {
			for (String stringLoc : subclaimSection.getKeys(false)) {
				String path = "subclaim-signs."+stringLoc+".";
				Block block = Methods.deserializeLocation(stringLoc, false).getBlock();
				
				if (checkSign(block, path))
					continue;
			
				String[] locations = config.getString(path + "chest-locations").split("µ");
				SubclaimSign sign = new SubclaimSign((Sign) block.getState(), Methods.deserializeLocation(locations[0], false));
				
				if (locations.length > 1)
					sign.setLocation(Methods.deserializeLocation(locations[1], false), 1);
				
				sign.setOwners(config.getStringList(path + "owners").stream().map(UUID::fromString).collect(Collectors.toCollection(() -> new ArrayList<>(3))));
				subclaimSigns++;
			}
		}
			
		OnimaAPI.sendConsoleMessage("§aNous avons chargé "+signs+" sign"+(signs > 1 ? "s" : "")+" (Elevator Signs: "+elevatorSigns+")"+" (Signs Shop: "+signsShop+")"+" (Subclaim Signs: "+subclaimSigns+")", ConfigurationService.ONIMAAPI_PREFIX);
	}
	
	private static boolean checkSign(Block block, String path) {
		if (!(block.getState() instanceof Sign)) {
			signSerialConfig.remove(StringUtils.left(path, path.length() - 1), false);
			return true;
		} 
		return false;
	}
	
	public static Block getAttachedBlock(Block block) {
		return block.getRelative(((org.bukkit.material.Sign) block.getState().getData()).getAttachedFace());
	}
	
}
