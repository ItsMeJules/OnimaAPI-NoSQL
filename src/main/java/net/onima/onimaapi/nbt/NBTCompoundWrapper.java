package net.onima.onimaapi.nbt;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.server.v1_7_R4.NBTTagCompound;

public class NBTCompoundWrapper {
	
	private Map<String, Object> map;
	private NBTTagCompound compound;
	
	{
		map = new HashMap<>();
	}
	
	public NBTCompoundWrapper(NBTTagCompound compound) {
		this.compound = compound;
	}

	public NBTCompoundWrapper() {
		compound = new NBTTagCompound();
	}
	
	public void add(String key, NBTType type, Object value) {
		if (!type.isAssignable(value.getClass()))
			throw new IllegalArgumentException();
		
		map.put(key, value);
		compound.set(key, type.asNBT(value));
	}
	
	public void remove(String key) {
		map.remove(key);
		compound.remove(key);
	}
	
	public Map<String, Object> getMapCopy() {
		return Maps.newHashMap(map);
	}
	
	public NBTTagCompound asNBTCompound() {
		return compound;
	}

}
