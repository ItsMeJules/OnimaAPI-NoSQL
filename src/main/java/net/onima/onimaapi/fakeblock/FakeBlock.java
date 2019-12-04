package net.onima.onimaapi.fakeblock;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import net.onima.onimaapi.players.APIPlayer;

/**
 * This class is useful to create some fake blocks (which are only client sided). 
 */
public class FakeBlock {
	
	private FakeType type;
	private Location location;
	private FakeBlockData data;
	
	/**
	 * This constructor makes a new instance with everything you want.
	 * 
	 * @param type - The FakeType to use.
	 * @param data - The data (block and damage)
	 * @param location - The location where this fake block should be set.
	 */
	public FakeBlock(FakeType type, FakeBlockData data, Location location) {
		this.type = type;
		this.location = location;
		this.data = data;
	}
	
	/**
	 * This constructor uses the default data of the given FakeType compared to the<br>
	 * {@link #FakeBlock(FakeType, FakeBlockData, Location)} constructor.
	 * 
	 * @param type - The FakeType to use.
	 * @param location - The location where this fake block should be set.
	 */
	public FakeBlock(FakeType type, Location location) {
		this(type, type.getData(), location);
	}
	
	/**
	 * This method returns the FakeType of this FakeBlock.
	 * 
	 * @return The FakeType.
	 */
	public FakeType getType() {
		return type;
	}

	/**
	 * This method returns the FakeBlock location.
	 * 
	 * @return The fake block location.
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * This method returns the fake block data which is basically the type of block.
	 * 
	 * @return The fake block data
	 */
	public FakeBlockData getData() {
		return data;
	}
	
	/**
	 * This method sends the fake block to the given player.
	 * 
	 * @param player - Player to send the block change.
	 */
	@SuppressWarnings("deprecation")
	public void send(Player player) {
		Block block = location.getBlock();
		
		if (block.getType() == data.getItemType() && block.getData() == data.getData()) return;
		
		player.sendBlockChange(location, data.getItemType(), data.getData());
	}
	
	/**
	 * This method sets back the original block at this FakeBlock location.<br>
	 * Note that the method {@link net.jestiz.onimaapi.player.Profile#sendBlockChange(Location, org.bukkit.Material, byte)}
	 * is called twice because only one call does not work, do not ask me why.
	 * 
	 * @param profile - The profile to set the original block.
	 */
	@SuppressWarnings("deprecation")
	public void reset(Player player) {
		Block block = location.getBlock();
		
		if (block.getType() == data.getItemType() && block.getData() == data.getData()) return;
		
		player.sendBlockChange(location, block.getType(), block.getData());
	}
	
	/**
	 * This method sends all the given fake blocks to the given profile using the parallelStream. So watch out to sync issues.
	 * 
	 * @param profile - Profile to generate the fake blocks
	 * @param fakeBlocks - The set of fake blocks to send. 
	 */
	public static void generate(APIPlayer apiPlayer, List<FakeBlock> fakeBlocks) {
		fakeBlocks.parallelStream().forEach(fakeBlock -> apiPlayer.addFakeBlock(fakeBlock));
	}

}
