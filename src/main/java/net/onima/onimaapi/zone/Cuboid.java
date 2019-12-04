package net.onima.onimaapi.zone;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.google.common.base.Preconditions;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.zone.struct.CuboidFace;

public class Cuboid implements Cloneable, Iterable<Block> {
	
	private World world;
	private int minX, minY, minZ;
	private int maxX, maxY, maxZ;
	
	//Updatable values
	private int xLength, yLength, zLength, area, volume;
	private Vector minimum, center, maximum;
	private Vector[] corners;
//	private CuboidWall[] walls;
	private List<Location> cornersPillars;
	
	public Cuboid(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this.world = world;
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		update();
	}
	
	public Cuboid(Location first, Location second, boolean update) {
		Preconditions.checkNotNull(first, "Location 1 cannot be null");
		Preconditions.checkNotNull(second, "Location 2 cannot be null");
		Preconditions.checkArgument(first.getWorld().equals(second.getWorld()), "Locations must be on the same world");
		
		world = first.getWorld();
		minX = Math.min(first.getBlockX(), second.getBlockX());
		minY = Math.min(first.getBlockY(), second.getBlockY());
		minZ = Math.min(first.getBlockZ(), second.getBlockZ());
		maxX = Math.max(first.getBlockX(), second.getBlockX());
		maxY = Math.max(first.getBlockY(), second.getBlockY());
		maxZ = Math.max(first.getBlockZ(), second.getBlockZ());
		
		if (update)
			update();
	}
	
	private void update() {
		xLength = maxX - minX + 1;
		yLength = maxY - minY + 1;
		zLength = maxZ - minZ + 1;
		area = xLength * zLength;
		volume = xLength * zLength * yLength;
		minimum = new Vector(minX, minY, minZ);
		center = new Vector(minX + xLength / 2.0, minY + yLength / 2.0, minZ + zLength / 2.0);
		maximum = new Vector(maxX, maxY, maxZ);
		
		Bukkit.getScheduler().runTaskAsynchronously(OnimaAPI.getInstance(), () -> {
			corners = new Vector[8];
			corners[0] = new Vector(minX, minY, minZ);
			corners[1] = new Vector(minX, minY, maxZ);
			corners[2] = new Vector(minX, maxY, minZ);
			corners[3] = new Vector(minX, maxY, maxZ);
			corners[4] = new Vector(maxX, minY, minZ);
			corners[5] = new Vector(maxX, minY, maxZ);
			corners[6] = new Vector(maxX, maxY, minZ);
			corners[7] = new Vector(maxX, maxY, maxZ);
			
//			walls = new CuboidWall[4];
//			walls[0] = new CuboidWall(corners[0], corners[3], corners[1], corners[2]);
//			walls[1] = new CuboidWall(corners[0], corners[6], corners[2], corners[4]);
//			walls[2] = new CuboidWall(corners[4], corners[7], corners[6], corners[5]);
//			walls[3] = new CuboidWall(corners[1], corners[7], corners[3], corners[5]);
			
			cornersPillars = new ArrayList<>(4 * yLength);
			
			for (int y = minY; y <= maxY; y++) {
				cornersPillars.add(new Location(world, minX, y, minZ));
				cornersPillars.add(new Location(world, minX, y, maxZ));
				cornersPillars.add(new Location(world, maxX, y, minZ));
				cornersPillars.add(new Location(world, maxX, y, maxZ));
			}
			
//			if (!walls) return;
//			
//			//(l - 1 + L - 1) * h * 2 = amount of blocks of the walls
//			this.walls = new ArrayList<>((xLength + zLength - 2) * yLength * 2);
//			
//			for (int x = minX; x <= maxX; ++x) {
//				for (int y = minY; y < maxY; y++) {
//					this.walls.add(new Vector(x, y, minZ));
//					this.walls.add(new Vector(x, y, maxZ));
//				}
//			}
//			
//			for (int z = minZ; z <= maxZ; ++z) {
//				for (int y = minY; y < maxY; y++) {
//					this.walls.add(new Vector(minX, y, z));
//					this.walls.add(new Vector(maxX, y, z));
//				}
//			}
		});
	}
	
    public Cuboid expand(CuboidFace face, int toExpand) {
    	switch (face) {
		case NORTH:
			minX -= toExpand;
			break;
		case SOUTH:
			maxX += toExpand;
			break;
		case EAST:
			minY -= toExpand;
			break;
		case WEST:
			maxZ += toExpand;
			break;
		case DOWN:
			minY -= toExpand;
			break;
		case UP:
			maxY += toExpand;
			break;
		case HORIZONTAL:
			minX -= toExpand;
			maxX += toExpand;
			minZ -= toExpand;
			maxZ += toExpand;
			break;
		case VERTICAL:
			minY -= toExpand;
			maxY += toExpand;
			break;
		default:
			break;
		}
    	update();
    	return this;
    }
    
    public Cuboid expand(CuboidFace face) {
    	return expand(face, 1);
    }
    
    public Cuboid expandVertical() {
    	minY = 0;
    	maxY = 256;
    	update();
    	return this;
    }
    
	public boolean contains(Cuboid cuboid) {
		if (!cuboid.world.equals(world)) return false;
		
		return cuboid.maxX >= minX && cuboid.minX <= maxX && cuboid.maxY >= minY && cuboid.minY <= maxY && cuboid.maxZ >= minZ && cuboid.minZ <= maxZ;
	}

	public boolean contains(Entity entity) {
		return contains(entity.getLocation());
	}

	public boolean contains(int x, int z) {
		return x >= minX && x <= maxX && z >= minZ && z <= maxZ;
	}
	
	public boolean contains(World world, int x, int z) {
		return this.world.getUID().equals(world.getUID()) && x >= minX && x <= maxX && z >= minZ && z <= maxZ;
	}
	
	public boolean contains(int x, int y, int z) {
		return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
	}
	
	public boolean contains(Vector vector) {
		if (vector == null)
			return false;
	
		return contains(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
	}

	public boolean contains(Location location) {
		if (location == null || world == null)
			return false;

		World world = location.getWorld();

		return world != null && this.world.getUID().equals(world.getUID()) && contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}
	
	public boolean wallContains(Location location) {
//		if (location == null || world == null)
//			return false;
//
//		World world = location.getWorld();
//
//		if (world != null && this.world.getUID().equals(world.getUID())) {
//			for (CuboidWall wall : walls) {
//				if (wall.contains(location.toVector()))
//					return true;
//			}
//		}
		int x = location.getBlockX(), z = location.getBlockZ();
		
		return x == minimum.getBlockX() || x == maximum.getBlockX() ||  z == minimum.getBlockZ() || z == maximum.getBlockZ();
	}
	
	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<>();

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (contains(player))
				players.add(player);
		}

		return players;
	}
	
	public List<LivingEntity> getLivingEntities() {
		return world.getEntities()
				.parallelStream()
				.filter(entity -> entity instanceof LivingEntity)
				.filter(entity -> contains(entity))
				.map(entity -> (LivingEntity) entity)
				.collect(Collectors.toList());
	}
    
	public World getWorld() {
		return world;
	}
	
	public int getMinX() {
		return minX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMinZ() {
		return minZ;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getMaxZ() {
		return maxZ;
	}
	
	public int getXLength() {
		return xLength;
	}
	
	public int getYLength() {
		return yLength;
	}
	
	public int getZLength() {
		return zLength;
	}
	
	public int getArea() {
		return area;
	}
	
	public int getVolume() {
		return volume;
	}
	
	public Vector getMinimum() {
		return minimum;
	}
	
	public Vector getCenter() {
		return center;
	}
	
	public Vector getMaximum() {
		return maximum;
	}
	
//	public CuboidWall[] getWalls() {
//		return walls;
//	}
	
	public Vector[] getCorners() {
		return corners;
	}
	
	public List<Location> getCornersPillars() {
		return cornersPillars;
	}
	
	public Location getMinimumLocation() {
		return minimum.toLocation(world);
	}
	
	public Location getCenterLocation() {
		return center.toLocation(world);
	}
	
	public Location getMaximumLocation() {
		return maximum.toLocation(world);
	}

	@Override
	public Iterator<Block> iterator() {
		return new CuboidIterator(world, minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	public Cuboid clone() {
		return new Cuboid(world, minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	public class CuboidIterator implements Iterator<Block> {
		private World world;
		private int baseX, baseY, baseZ;
		private int x, y, z;
		private int sizeX, sizeY, sizeZ;

		public CuboidIterator(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
			this.world = world;
			baseX = x1;
			baseY = y1;
			baseZ = z1;
			sizeX = Math.abs(x2 - x1) + 1;
			sizeY = Math.abs(y2 - y1) + 1;
			sizeZ = Math.abs(z2 - z1) + 1;
			x = y = z = 0;
		}

		public boolean hasNext() {
			return x < sizeX && y < sizeY && z < sizeZ;
		}

		public Block next() {
			Block b = world.getBlockAt(baseX + x, baseY + y, baseZ + z);
			if (++x >= sizeX) {
				x = 0;
				if (++y >= sizeY) {
					y = 0;
					++z;
				}
			}
			return b;
		}

		public void remove() {
			// nop
		}
	}
	
}
