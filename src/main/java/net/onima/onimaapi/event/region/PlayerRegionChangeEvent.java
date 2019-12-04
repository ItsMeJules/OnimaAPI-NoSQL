package net.onima.onimaapi.event.region;

import org.bukkit.Location;

import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.zone.type.Region;

public class PlayerRegionChangeEvent extends RegionChangeEvent {

	private Region newRegion;
	private Location fromLocation, toLocation;

	public PlayerRegionChangeEvent(APIPlayer apiPlayer, Location fromLocation, Location toLocation, Region region, Region newRegion, RegionChangeCause cause) {
		super(region, apiPlayer, cause);
		this.newRegion = newRegion;
		this.fromLocation = fromLocation;
		this.toLocation = toLocation;
	}
	
	public Region getNewRegion() {
		return newRegion;
	}
	
	public Location getFromLocation() {
		return fromLocation;
	}

	public Location getToLocation() {
		return toLocation;
	}

	public static enum PlayerRegionChangementCause implements RegionChangeCause {
		TELEPORT,
		MOVE;
	}
	
}
