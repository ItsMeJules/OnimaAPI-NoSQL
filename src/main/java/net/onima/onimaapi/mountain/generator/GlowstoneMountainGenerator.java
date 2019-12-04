package net.onima.onimaapi.mountain.generator;

import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;

import com.google.common.base.Preconditions;

import net.onima.onimaapi.mountain.GlowstoneMountain;
import net.onima.onimaapi.mountain.utils.Mountain;
import net.onima.onimaapi.mountain.utils.MountainGenerator;
import net.onima.onimaapi.utils.OEffect;

public class GlowstoneMountainGenerator implements MountainGenerator {

	@Override
	public List<Location> setBlocks(Mountain mountain) {
		return setBlocks(mountain, Collections.emptyList());
	}

	@Override
	public List<Location> setBlocks(Mountain mountain, List<OEffect> effects) {
		Preconditions.checkArgument(mountain instanceof GlowstoneMountain, "La montagne spécifiée pour ce générateur doit être de type GLOWSTONE");
		
		List<Location> locations = ((GlowstoneMountain) mountain).getBlocksToSet();
		
		if (!effects.isEmpty()) {
			for (Location location : locations) {
				location.getBlock().setType(Material.GLOWSTONE);
				effects.stream().forEach(oEffect -> oEffect.play(location));
			}
		} else {
			for (Location location : locations)
				location.getBlock().setType(Material.GLOWSTONE);
		}
		
		return locations;
	}

}
