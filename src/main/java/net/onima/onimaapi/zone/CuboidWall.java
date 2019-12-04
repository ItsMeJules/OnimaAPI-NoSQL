package net.onima.onimaapi.zone;

import org.bukkit.util.Vector;

public class CuboidWall {

	private Vector minimum, maximum, corner1, corner2;

	public CuboidWall(Vector minimum, Vector maximum, Vector corner1, Vector corner2) {
		this.minimum = minimum;
		this.maximum = maximum;
		this.corner1 = corner1;
		this.corner2 = corner2;
	}
	
	public boolean contains(Vector vector) {
		if (vector == null)
			return false;
		
		return vector.getBlockX() >= minimum.getBlockX() && vector.getBlockX() <= maximum.getBlockX()
				&& vector.getBlockY() >= minimum.getBlockY() && vector.getBlockY() <= maximum.getBlockY()
				&& vector.getBlockZ() >= minimum.getBlockZ() && vector.getBlockZ() <= maximum.getBlockZ();
	}

	public Vector getMinimum() {
		return minimum;
	}

	public Vector getMaximum() {
		return maximum;
	}

	public Vector getCorner1() {
		return corner1;
	}

	public Vector getCorner2() {
		return corner2;
	}
	
}
