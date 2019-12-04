package net.onima.onimaapi.fasttiles;

import org.bukkit.Location;
import org.bukkit.block.Furnace;

import net.onima.onimaapi.utils.ConfigurationService;

public class FastFurnace implements FastTile {
	
    private final Furnace furnace;

    public FastFurnace(Furnace furnace) {
        this.furnace = furnace;
    }

    @Override
    public Location getLocation() {
        return furnace.getLocation();
    }

    @Override
    public boolean action() {
        if (furnace.getInventory().getItem(0) != null) {
            if (furnace.getCookTime() > 0 || furnace.getBurnTime() > 0) {
                furnace.setCookTime((short) (furnace.getCookTime() + ConfigurationService.FURNACE_MULTIPLIER));
                furnace.setBurnTime((short) (furnace.getBurnTime() - ConfigurationService.FURNACE_MULTIPLIER));
            }
        } else if (furnace.getInventory().getViewers().isEmpty())
        	return true;
        
        return false;
    }

    public Furnace getFurnace() {
		return furnace;
	}
    
}
