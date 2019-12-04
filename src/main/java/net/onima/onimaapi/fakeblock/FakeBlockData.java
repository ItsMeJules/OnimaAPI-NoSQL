package net.onima.onimaapi.fakeblock;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

/**
 * This class is most importantly to have an easier way on creating fake blocks (especially when you can dye them).
 */
public class FakeBlockData extends MaterialData {
	
	/**
	 * Blocks displayed for claim creation (Glowstone, Cyan Stained glass)
	 */
	public static final FakeBlockData[] CREATE_CLAIM_BLOCKS_DATA = {new FakeBlockData(Material.GLOWSTONE), new FakeBlockData(Material.STAINED_GLASS, DyeColor.CYAN)};
	
	/**
	 * Blocks displayed for claim map (Snow block, Sand stone, Furnace, Netherrack...)
	 */
	public static final FakeBlockData[] CLAIM_MAP_BLOCKS_DATA = {new FakeBlockData(Material.SNOW_BLOCK), new FakeBlockData(Material.SANDSTONE), 
			new FakeBlockData(Material.FURNACE), new FakeBlockData(Material.NETHERRACK), new FakeBlockData(Material.NETHER_BRICK), 
			new FakeBlockData(Material.DIAMOND_ORE), new FakeBlockData(Material.COAL_ORE), new FakeBlockData(Material.IRON_ORE), 
			new FakeBlockData(Material.GOLD_ORE), new FakeBlockData(Material.LAPIS_ORE), new FakeBlockData(Material.REDSTONE_ORE), 
			new FakeBlockData(Material.GLOWSTONE), new FakeBlockData(Material.LAPIS_BLOCK), new FakeBlockData(Material.GOLD_BLOCK),
			new FakeBlockData(Material.DIAMOND_BLOCK), new FakeBlockData(Material.COAL_BLOCK), new FakeBlockData(Material.IRON_BLOCK),
			new FakeBlockData(Material.LAPIS_BLOCK), new FakeBlockData(Material.REDSTONE_BLOCK), new FakeBlockData(Material.EMERALD_BLOCK),
			new FakeBlockData(Material.EMERALD_ORE), new FakeBlockData(Material.BOOKSHELF), new FakeBlockData(Material.CLAY),
			new FakeBlockData(Material.CLAY_BRICK), new FakeBlockData(Material.BOOKSHELF), new FakeBlockData(Material.HAY_BLOCK)};
	
	
	/**
	 * This constructor instantiate a fake block data which is better for fake block uses.
	 * It's mostly used for wool/stained glass...
	 * 
	 * @param type - Material to set.
	 * @param color - DyeColor to set.
	 */
	@SuppressWarnings("deprecation")
	public FakeBlockData(Material type, DyeColor color) {
		super(type, color.getData());
	}
	
	/**
	 * This constructor instantiate a fake block data which is better for fake block uses.
	 * 
	 * @param type - Material to set.
	 */
	public FakeBlockData(Material type) {
		super(type);
	}
	
	@Override
	public Material getItemType() {
		return super.getItemType();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public byte getData() {
		return super.getData();
	}
	
}
