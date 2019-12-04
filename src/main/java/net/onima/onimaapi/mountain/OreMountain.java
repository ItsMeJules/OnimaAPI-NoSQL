package net.onima.onimaapi.mountain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

import net.onima.onimaapi.mountain.generator.OreMountainGenerator;
import net.onima.onimaapi.mountain.utils.Mountain;
import net.onima.onimaapi.mountain.utils.MountainGenerator;
import net.onima.onimaapi.mountain.utils.MountainType;
import net.onima.onimaapi.utils.Methods;

public class OreMountain extends Mountain {
	
	private Map<Location, Material> toSet;
	
	{
		generator = new OreMountainGenerator();
		type = MountainType.ORES;
		toSet = new HashMap<>();
	}
	
	public OreMountain(String name, Location location1, Location location2, String creator) {
		super(name + "_ORES_" + mountains.size(), name, location1, location2, creator);
	}
	
	public OreMountain(String name, String creator) {
		super(name + "_ORES_" + mountains.size(), name, creator);
	}
	
	public OreMountain() {
		super();
	}
	
	@Override
	public String getTimeAnnouncement() {
		return "§9La montagne de minerais §b" + name + " §9va reset dans %time%.";
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
		generatedAnnouncement("§9La montagne de minerais en %X% %Y% %Z% §b" + name + " §9a éré reset !");
	}

	@Override
	public double getPercentageLeft() {
		return generated == null ? 0.0 : (int) ((double) generated.parallelStream().filter(location -> location.getBlock().getType() == Material.AIR).count() / (double) generated.size() * 100);
	}
	
	
	@Override
	public boolean canBuild(Block block) {
		return toSet.containsKey(block.getLocation());
	}
	
	@Override
	public boolean areBlocksRegistered() {
		return toSet.isEmpty();
	}
	
	@Override
	public void registerBlocks() {
		Iterator<Block> iterator = cuboid.iterator();
		toSet.clear();
		
		while (iterator.hasNext()) {
			Block block = iterator.next();
			
			if (block.getType().name().endsWith("_ORE"))
				toSet.put(block.getLocation(), block.getType());
		}
		
		blocksRegistered = true;
	}
	
	@Override
	public void serialize() {
		super.serialize();
		
		List<String> blocksStr = new ArrayList<>();
		
		for (Entry<Location, Material> entry : toSet.entrySet())
			blocksStr.add(Methods.serializeLocation(entry.getKey(), false) + '#' + entry.getValue().name());
		
		config.set("mountains." + type.name() + '.' + name + ".blocks", blocksStr);
	}
	
	public Map<Location, Material> getBlocksToSet() {
		return toSet;
	}

}
