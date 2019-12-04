package net.onima.onimaapi.mountain;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

import net.onima.onimaapi.mountain.generator.GlowstoneMountainGenerator;
import net.onima.onimaapi.mountain.utils.Mountain;
import net.onima.onimaapi.mountain.utils.MountainGenerator;
import net.onima.onimaapi.mountain.utils.MountainType;
import net.onima.onimaapi.utils.Methods;

public class GlowstoneMountain extends Mountain {
	
	private List<Location> toSet;
	
	{
		generator = new GlowstoneMountainGenerator();
		type = MountainType.GLOWSTONE;
		toSet = new ArrayList<>();
	}

	public GlowstoneMountain(String name, Location location1, Location location2, String creator) {
		super(name + "_GLOWSTONE_" + mountains.size(), name, location1, location2, creator);
	}
	
	public GlowstoneMountain(String name, String creator) {
		super(name + "_GLOWSTONE_" + mountains.size(), name, creator);
	}
	
	public GlowstoneMountain() {
		super();
	}
	
	@Override
	public String getTimeAnnouncement() {
		return "§6La glowstone montagne §e" + name + " §6va reset dans %time%.";
	}
	
	@Override
	public void generate() {
		generate(generator);
	}
	
	@Override
	public void generate(MountainGenerator generator) {
		if (generator == null)
			generator = super.generator;
		
		List<LivingEntity> entities = cuboid.getLivingEntities();
		
		if (!entities.isEmpty()) {
			Location location = cuboid.getWorld().getHighestBlockAt(cuboid.getMinimumLocation().add(-1, 0, -1)).getLocation();
			entities.forEach(entity -> entity.teleport(location));
		}
		
		generated = generator.setBlocks(this);
		generatedAnnouncement("§6La glowstone montagne en %X% %Y% %Z% §e" + name + " §6a été reset !");
	}
	
	@Override
	public double getPercentageLeft() {
		return generated == null ? 0.0 : (int) ((double) generated.parallelStream().filter(location -> location.getBlock().getType() == Material.AIR).count() / (double) generated.size() * 100);
	}
	
	@Override
	public boolean canBuild(Block block) {
		return toSet.contains(block.getLocation());
	}
	
	@Override
	public void registerBlocks() {
		cuboid.iterator().forEachRemaining(block -> {
			if (block.getType() == Material.GLOWSTONE)
				toSet.add(block.getLocation());
		});
	}
	
	@Override
	public boolean areBlocksRegistered() {
		return toSet.isEmpty();
	}
	
	@Override
	public void serialize() {
		super.serialize();
		
		List<String> blocksStr = new ArrayList<>();
		
		for (Location location : toSet)
			blocksStr.add(Methods.serializeLocation(location, false));
		
		config.set("mountains." + type.name() + '.' + name + ".blocks", blocksStr);
	}
	
	
	public List<Location> getBlocksToSet() {
		return toSet;
	}

}
