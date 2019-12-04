package net.onima.onimaapi.mountain.utils;

import java.util.List;

import org.bukkit.Location;

import net.onima.onimaapi.utils.OEffect;

public interface MountainGenerator { //TODO RAndom for effects + create asyncTask if locations has a big size
	
	public List<Location> setBlocks(Mountain mountain);
	public List<Location> setBlocks(Mountain mountain, List<OEffect> effects);
	
}
