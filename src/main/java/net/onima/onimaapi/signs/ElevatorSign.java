package net.onima.onimaapi.signs;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;

import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;

public class ElevatorSign extends HCFSign { 
	//TODO Some players can use it (line 1 [Elevator]; line 2 Officers | Members | Leaders; line 3 UP | DOWN) <- might be shit
	//TODO If a location is obstructed find a new one and inform the player (Usual location obstructed trying to find a new one; New location found | No new location found try shit)
	
	private static boolean refreshed;
	
	private Location teleportLocation;
	private boolean up, readyToUse, locFixed;
	private ElevatorSign twin;
	
	public ElevatorSign(Sign sign, boolean up) {
		super(sign);
		this.up = up;
	}
	
	public Location getTeleportLocation() {
		return teleportLocation;
	}

	public void setTeleportLocation(Location teleportLocation) {
		this.teleportLocation = teleportLocation;
	}

	public boolean isUp() {
		return up;
	}
	
	public boolean isDown() {
		return !up;
	}
	
	public boolean isReadyToUse() {
		return readyToUse;
	}
	
	public void setReadyToUse(boolean readyToUse) {
		this.readyToUse = readyToUse;
	}
	
	public boolean isLocFixed() {
		return locFixed;
	}
	
	public ElevatorSign getTwin() {
		return twin;
	}
	
	public Location initLocationToTeleport() {
		Location found = sign.getLocation().clone();
		int toAdd = up ? 1 : -1;
		
		while (twin == null) {
			Block nextBlock = found.add(0, toAdd, 0).getBlock();
			
			if (found.getBlockY() == 255 || found.getBlockY() == 0) return null;
			if (nextBlock.getState() instanceof Sign) {
				HCFSign hcfSign = HCFSign.getSign((Sign) nextBlock.getState());
				
				if (hcfSign instanceof ElevatorSign) {
					if (up != ((ElevatorSign) hcfSign).up) {
						ElevatorSign elevator = (ElevatorSign) hcfSign;
						twin = elevator;
						elevator.twin = this;
					}
				}
			}
		}
		
		Location loc = twin.fixLoc(sign.getLocation());
		
		if (!twin.teleportLocaionValid(loc)) {
			twin.locFixed = false;
			return null;
		}
		
		twin.setTeleportLocation(loc.add(0.5, 0, 0.5));
		
		List<String> lines = Methods.replacePlaceholder(ConfigurationService.ELEVATOR_SIGN_LINES, "%up%", twin.up ? ConfigurationService.ELEVATOR_SIGN_UP : ConfigurationService.ELEVATOR_SIGN_DOWN, "%usage%", ConfigurationService.ELEVATOR_SIGN_CAN_USE);
		for (int i = 0; i < lines.size(); i++)
			twin.sign.setLine(i, lines.get(i));
		
		twin.sign.update();
		readyToUse = true;
		twin.readyToUse = true;
		
		return teleportLocation = fixLoc(found.add(0.5, 0, 0.5));
	}
	
	public boolean teleportLocaionValid(Location location) {
		return location.clone().add(0, -1, 0).getBlock().getType().isSolid() && (locFixed ? !location.getBlock().getType().isSolid() : location.getBlock().getType().toString().contains("SIGN") && !location.clone().add(0, 1, 0).getBlock().getType().isSolid());
	}

	private Location fixLoc(Location location) {
		if (location.clone().add(0, -1, 0).getBlock().getType().isSolid())
			return location;
		else {
			locFixed = true;
			return location.add(0, -1, 0);
		}
	}
	
	@Override
	public void serialize() {
		String path = "elevator-signs."+Methods.serializeLocation(sign.getLocation(), false)+".";
		
		if (!refreshed)
			refreshFile();
		
		config.set(path+"teleport-location", Methods.serializeLocation(teleportLocation, false));
		config.set(path+"up", up);
		config.set(path+"twin-loc", twin == null ? "" : twin.teleportLocation == null ? "" : Methods.serializeLocation(twin.teleportLocation, false));
	}

	@Override
	public void refreshFile() {
		ConfigurationSection section = config.getConfigurationSection("elevator-signs");
		
		if (section != null) {
			List<Location> elevatorLoc = signs.stream().filter(sign -> sign instanceof ElevatorSign).map(hcfSign -> hcfSign.getSign().getLocation()).collect(Collectors.toList());
			
			for (String stringLoc : section.getKeys(false)) {
				if (!elevatorLoc.contains(Methods.deserializeLocation(stringLoc, false)))
					signSerialConfig.remove("elevator-signs."+stringLoc, false);
			}
		}
		
		refreshed = true;
	}
	
}
