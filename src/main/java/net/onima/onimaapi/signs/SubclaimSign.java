package net.onima.onimaapi.signs;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;

import net.onima.onimaapi.utils.Methods;

public class SubclaimSign extends HCFSign {
	
	private static boolean refreshed;
	
	private List<UUID> owners;
	private Location[] locations;
	private Chest chest;

	{
		owners = new ArrayList<>();
		locations = new Location[2];
	}
	
	public SubclaimSign(Sign sign, Chest chest) {
		super(sign);
		this.chest = chest;
		locations[0] = chest.getLocation();
	}
	
	public SubclaimSign(Sign sign, Location location) {
		super(sign);
		Block block = location.getBlock();
		
		if (!(block.getState() instanceof Chest))
			throw new IllegalArgumentException("Le block à la location x : " + block.getX() + " y : " + block.getY() + " z : " + block.getZ() + " n'est pas un coffre mais un " + block.getType());
		
		chest = (Chest) location.getBlock().getState();
		locations[0] = location;
	}
	
	public SubclaimSign(Sign sign) {
		super(sign);
	}
	
	public List<UUID> getOwners() {
		return owners;
	}
	
	public void setOwners(List<UUID> owners) {
		this.owners = owners;
	}
	
	public Location[] getLocations() {
		return locations;
	}

	public void setLocations(Location[] locations) {
		this.locations = locations;
	}
	
	public void setLocation(Location location, int index) {
		locations[index] = location;
	}
	
	public Chest getChest() {
		return chest;
	}
	
	public void setChest(Chest chest) {
		this.chest = chest;
		locations[0] = chest.getLocation();
	}
	
	@Override
	public void serialize() {
		String path = "subclaim-signs." + Methods.serializeLocation(sign.getLocation(), false) + ".";
		
		if (!refreshed)
			refreshFile();

		config.set(path + "chest-locations", Methods.serializeLocation(locations[0], false) + 'µ' + Methods.serializeLocation(locations[1], false));
		config.set(path + "owners", owners.stream().map(UUID::toString).collect(Collectors.toCollection(() -> new ArrayList<>(3))));
	}

	@Override
	public void refreshFile() {
		ConfigurationSection section = config.getConfigurationSection("subclaim-signs");
		
		if (section != null) {
			List<Location> subLoc = signs.stream().filter(sign -> sign instanceof SubclaimSign).map(hcfSign -> {
				return hcfSign.getSign().getLocation();
			}).collect(Collectors.toList());
			
			for (String stringLoc : section.getKeys(false)) {
				if (!subLoc.contains(Methods.deserializeLocation(stringLoc, false)))
					signSerialConfig.remove("subclaim-signs." + stringLoc, false);
			}
		}
		
		refreshed = true;
	}
	
	public static SubclaimSign fromChest(Chest chest) {
		return fromLocation(chest.getLocation());
	}
	
	public static SubclaimSign fromLocation(Location location) {
		for (HCFSign sign : getHCFSigns()) {
			if (!(sign instanceof SubclaimSign)) continue;
			
			SubclaimSign subSign = (SubclaimSign) sign;
			
			if (Methods.locationEquals(location, subSign.locations[0]) || Methods.locationEquals(location, subSign.locations[1]) || Methods.locationEquals(location, sign.getSign().getLocation())) 
				return subSign;
		}
		return null;
	}

}
