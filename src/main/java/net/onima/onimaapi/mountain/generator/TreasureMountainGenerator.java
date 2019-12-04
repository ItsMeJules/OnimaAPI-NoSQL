package net.onima.onimaapi.mountain.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;

import com.google.common.base.Preconditions;

import net.onima.onimaapi.mountain.TreasureMountain;
import net.onima.onimaapi.mountain.TreasureMountain.TreasureBlock;
import net.onima.onimaapi.mountain.utils.Mountain;
import net.onima.onimaapi.mountain.utils.MountainGenerator;
import net.onima.onimaapi.utils.OEffect;

public class TreasureMountainGenerator implements MountainGenerator { //TODO Optimize

	@Override
	public List<Location> setBlocks(Mountain mountain) {
		return setBlocks(mountain, Collections.emptyList());
	}

	
	@Override
	public List<Location> setBlocks(Mountain mountain, List<OEffect> effects) {
		Preconditions.checkArgument(mountain instanceof TreasureMountain, "La montagne spécifiée pour ce générateur doit être de type TREASURE");
		
		List<Location> locations = new ArrayList<>();
		
		if (!effects.isEmpty()) {
			for (Entry<Location, TreasureBlock> entry : ((TreasureMountain) mountain).getBlocks().entrySet()) {
				Location location = entry.getKey();
				TreasureBlock block = entry.getValue();
				
				initBlock(location, block, locations);
				effects.stream().forEach(gEffect -> gEffect.play(location));
				
				locations.add(location);
			}
		} else {
			for (Entry<Location, TreasureBlock> entry : ((TreasureMountain) mountain).getBlocks().entrySet()) {
				Location location = entry.getKey();
				TreasureBlock block = entry.getValue();
				
				initBlock(location, block, locations);
			}
		}
		return locations;
	}


	private void initBlock(Location location, TreasureBlock block, List<Location> locations) {
		Block locBlock = location.getBlock();

		if (block.isDoubleChest()) {
			block.getLocation1().getBlock().setType(block.getMaterial());
			block.getLocation2().getBlock().setType(block.getMaterial());
			locations.add(block.getLocation1());
			locations.add(block.getLocation2());
		} else {
			locations.add(block.getLocation());
			locBlock.setType(block.getMaterial());
		}
		
		Inventory blockInventory = ((InventoryHolder) locBlock.getState()).getInventory();
			
		for (Entry<Integer, ItemStack> entry : block.getItems().entrySet())
			blockInventory.setItem(entry.getKey(), entry.getValue());
		
		if (block.getFace() != null) {
			BlockState state = locBlock.getState();
			Directional dir = (Directional) locBlock.getState().getData();
			
			dir.setFacingDirection(block.getFace());
			state.setData((MaterialData) dir);
			state.update(true);
		}
	}
	
	
}
