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
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Directional;

import net.onima.onimaapi.mountain.generator.TreasureMountainGenerator;
import net.onima.onimaapi.mountain.utils.Mountain;
import net.onima.onimaapi.mountain.utils.MountainGenerator;
import net.onima.onimaapi.mountain.utils.MountainType;
import net.onima.onimaapi.utils.Methods;

public class TreasureMountain extends Mountain {
	
	private Map<Location, TreasureBlock> blocks;
	
	{
		generator = new TreasureMountainGenerator();
		type = MountainType.TREASURE;
		blocks = new HashMap<>();
	}
	
	public TreasureMountain(String name, Location location1, Location location2, String creator) {
		super(name + "_TREASURE_" + mountains.size(), name, location1, location2, creator);
	}
	
	public TreasureMountain(String name, String creator) {
		super(name + "_TREASURE_" + mountains.size(), name, creator);
	}
	
	public TreasureMountain() {
		super();
	}
	
	@Override
	public String getTimeAnnouncement() {
		return "§5La montagne de trésor §d"+ name + " §5va reset dans %time%.";
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
		generatedAnnouncement("§5La montagne de trésor en %X% %Y% %Z% §d" + name + " §5a été reset !");
	}

	@Override
	public double getPercentageLeft() {
		return generated == null ? 0.0 : (int) ((double) generated.parallelStream().filter(location -> location.getBlock().getType() == Material.AIR).count() / (double) generated.size() * 100);
	}
	
	@Override
	public boolean canBuild(Block block) {
		return blocks.containsKey(block.getLocation());
	}
	
	@Override
	public boolean areBlocksRegistered() {
		return blocks.isEmpty();
	}

	@Override
	public void registerBlocks() {
		Iterator<Block> iterator = cuboid.iterator();
		blocks.clear();
		
		while (iterator.hasNext()) {
			Block block = iterator.next();
			BlockState state = block.getState();
			TreasureBlock treasureBlock = null;
			
			if (state instanceof InventoryHolder) {
				InventoryHolder holder = (InventoryHolder) state;
				
				if (holder.getInventory() instanceof DoubleChestInventory) {
					DoubleChest doubleChest = ((DoubleChestInventory) holder.getInventory()).getHolder();
					Location loc1 = ((Chest) doubleChest.getLeftSide()).getLocation();
					Location loc2 = ((Chest) doubleChest.getRightSide()).getLocation();
					
					if (blocks.containsKey(loc1) || blocks.containsKey(loc2)) continue;
					
					treasureBlock = new TreasureBlock(loc1, loc2, state.getType(), this);
				} else
					treasureBlock = new TreasureBlock(block.getLocation(), state.getType(), this);
				
				treasureBlock.setItems(holder.getInventory());
				blocks.put(block.getLocation(), treasureBlock);
			}
			
			if (state.getData() instanceof Directional && treasureBlock != null)
				treasureBlock.setFace(((Directional) state.getData()).getFacing());
		}
	}
	
	@Override
	public void serialize() {
		super.serialize();
		
		String path = "mountains."+type.name()+'.'+name+'.';
		
		List<String> blocksList = new ArrayList<>();
		List<String> doubleChestList = new ArrayList<>();
		
		for (Entry<Location, TreasureBlock> entry : blocks.entrySet()) {
			TreasureBlock block = entry.getValue();
			
			if (block.isDoubleChest())
				doubleChestList.add(Methods.serializeLocation(block.getLocation1(), false) + '_' + Methods.serializeLocation(block.getLocation2(), false) + '_' + block.material.name() + '_' + block.face.name() + '_' + Methods.serializeItems(block.items));
			else
				blocksList.add(Methods.serializeLocation(entry.getKey(), false) + '_' + block.material.name() + '_' + (block.face != null ? block.face.name() : "") + (block.items != null ? '_' + Methods.serializeItems(block.items) : ""));
		}
		
		config.set(path + "blocks", blocksList);
		config.set(path + "double-chest", doubleChestList);
	}
	
	public TreasureBlock getTreasureBlock(Location location) {
		BlockState state = location.getBlock().getState();
		
		if (state instanceof InventoryHolder) {
			InventoryHolder holder = (InventoryHolder) state;
			
			if (holder.getInventory() instanceof DoubleChestInventory) {
				DoubleChest doubleChest = ((DoubleChestInventory) holder.getInventory()).getHolder();
				Location loc1 = ((Chest) doubleChest.getLeftSide()).getLocation();
				location = blocks.containsKey(loc1) ? loc1 : ((Chest) doubleChest.getRightSide()).getLocation();
			}
		}
		
		return blocks.get(location);
	}
	
	public Map<Location, TreasureBlock> getBlocks() {
		return blocks;
	}
	
	public static class TreasureBlock {
		
		private TreasureMountain mountain;
		private Location location1, location2;
		private Material material;
		private BlockFace face;
		private Map<Integer, ItemStack> items;
		
		public TreasureBlock(Location location1, Material material, TreasureMountain mountain) {
			this(location1, null, material, mountain);
		}
		
		public TreasureBlock(Location location1, Location location2, Material material, TreasureMountain mountain) {
			this.location1 = location1;
			this.location2 = location2;
			this.material = material;
			this.mountain = mountain;
		}
		
		public TreasureMountain getMountain() {
			return mountain;
		}

		public Location getLocation() {
			return location1 == null ? location2 : location1;
		}
		
		public Location getLocation1() {
			return location1;
		}
		
		public Location getLocation2() {
			return location2;
		}
		
		public boolean isDoubleChest() {
			return location1 != null && location2 != null;
		}

		public Material getMaterial() {
			return material;
		}

		public BlockFace getFace() {
			return face;
		}

		public void setFace(BlockFace face) {
			this.face = face;
		}
		
		public Map<Integer, ItemStack> getItems() {
			return items;
		}
		
		public void setItems(Map<Integer, ItemStack> items) {
			this.items = items;
		}
		
		public void setItems(Inventory inventory) {
			if (items == null) items = new HashMap<>();
			
			for (int i = 0; i < inventory.getSize(); i++) {
				ItemStack item = inventory.getItem(i);
				
				if (item == null || item.getType() == Material.AIR) continue;
				
				items.put(i, item);
			}
		}
		
	}

}
