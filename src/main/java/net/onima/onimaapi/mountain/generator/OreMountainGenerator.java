package net.onima.onimaapi.mountain.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;

import com.google.common.base.Preconditions;

import net.onima.onimaapi.mountain.OreMountain;
import net.onima.onimaapi.mountain.utils.Mountain;
import net.onima.onimaapi.mountain.utils.MountainGenerator;
import net.onima.onimaapi.utils.OEffect;

public class OreMountainGenerator implements MountainGenerator {

	@Override
	public List<Location> setBlocks(Mountain mountain) {
		return setBlocks(mountain, Collections.emptyList());
	}

	@Override
	public List<Location> setBlocks(Mountain mountain, List<OEffect> effects) {
		Preconditions.checkArgument(mountain instanceof OreMountain, "La montagne spécifiée pour ce générateur doit être de type ORE");
		
		List<Location> locations = new ArrayList<>();
		
		if (!effects.isEmpty()) {
			for (Entry<Location, Material> entry : ((OreMountain) mountain).getBlocksToSet().entrySet()) {
				Location location = entry.getKey();
					
				locations.add(location);
				location.getBlock().setType(entry.getValue());
				effects.stream().forEach(oEffect -> oEffect.play(location));
			}
		} else {
			for (Entry<Location, Material> entry : ((OreMountain) mountain).getBlocksToSet().entrySet()) {
				Location location = entry.getKey();
					
				locations.add(location);
				location.getBlock().setType(entry.getValue());
			}
		}
		
		return locations;
	}

}
