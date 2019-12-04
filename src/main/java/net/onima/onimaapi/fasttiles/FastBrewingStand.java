package net.onima.onimaapi.fasttiles;

import org.bukkit.Location;
import org.bukkit.block.BrewingStand;

import net.onima.onimaapi.utils.ConfigurationService;

public class FastBrewingStand implements FastTile {
	
	private final BrewingStand brewingStand;

    public FastBrewingStand(BrewingStand brewingStand) {
        this.brewingStand = brewingStand;
    }

    @Override
    public Location getLocation() {
        return brewingStand.getLocation();
    }

    @Override
    public boolean action() {
        if (brewingStand.getInventory().getViewers().isEmpty() && brewingStand.getInventory().getItem(3) == null)
            return true;

        if (brewingStand.getBrewingTime() > 1)
            brewingStand.setBrewingTime(Math.max(1, brewingStand.getBrewingTime() - ConfigurationService.BREWING_MULTIPLIER));
        
        return false;
    }

    public BrewingStand getBrewingStand() {
		return brewingStand;
	}

}
