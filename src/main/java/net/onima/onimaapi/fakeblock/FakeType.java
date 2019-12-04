package net.onima.onimaapi.fakeblock;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;

public enum FakeType {
	
	/**
	 * Represents a barrier ie: when an area has the flag combat-tag-deny-entry.
	 */
	COMBAT_REGION_BORDER(new FakeBlockData(Material.STAINED_GLASS, DyeColor.RED)),
	
	/**
	 * Represents a barrier ie: when you have your pvp timer and try to enter in a region with the flag pvp-timer-denny-entry.
	 */
	PVP_TIMER_REGION_BORDER(new FakeBlockData(Material.STAINED_GLASS, DyeColor.BLACK)),
	
	/**
	 * Represents a barrier ie: when a region's access rank is higher than yours.
	 */
	RANK_TOO_LOW_BORDER(new FakeBlockData(Material.STAINED_GLASS, DyeColor.SILVER)),
	
	/**
	 * Represents a claim pillar ie: when your /f map is on.
	 */
	CLAIM_MAP(new FakeBlockData(Material.STAINED_GLASS, DyeColor.YELLOW)),
	
	/**
	 * Represents a claim pillar ie: when selecting a block with the claiming wand.
	 */
	CREATE_CLAIM(null);
	
	private FakeBlockData data;

	private FakeType(FakeBlockData data) {
		this.data = data;
	}
	
	/**
	 * 
	 * @return The fake block data for this fake block type.<br>
	 * <tt>null</tt> if FakeType = {@link #CREATE_CLAIM}.
	 */
	public FakeBlockData getData() {
		return data;
	}
	
	/**
	 * This method is mostly for {@link #CLAIM_MAP} and {@link #CREATE_CLAIM}. It turns all the locations given
	 * into the FakeBlockData of the FakeType.
	 * 
	 * @param locations - Location to set the fake type.
	 * @param count - What block to use for CLAIM_MAP (0 = Glowstone, 1 = Cyan Stained Glass).
	 * @return A list of fake block created.
	 */
	public List<FakeBlock> toBlocks(List<Location> locations, int count) {
		List<FakeBlock> blocks = new ArrayList<>();
		
		locs: for (Location location : locations) {
			if (location.getBlock().getType() != Material.AIR) continue;
			int y = location.getBlockY();
			
			switch (this) {
			case CLAIM_MAP:
				if (y % 5 == 0 || y == 0) {
					blocks.add(new FakeBlock(this, FakeBlockData.CLAIM_MAP_BLOCKS_DATA[count], location));
					continue locs;
				}
				blocks.add(new FakeBlock(this, data, location));
				break;
			case CREATE_CLAIM:
				if (y % 4 == 0 || y == 0) {
					blocks.add(new FakeBlock(this, FakeBlockData.CREATE_CLAIM_BLOCKS_DATA[0], location));
					continue locs;
				}
				blocks.add(new FakeBlock(this, FakeBlockData.CREATE_CLAIM_BLOCKS_DATA[1], location));
				break;
			default:
				blocks.add(new FakeBlock(this, data, location));
				break;
			}
		}
		
		return blocks;
	}

}
