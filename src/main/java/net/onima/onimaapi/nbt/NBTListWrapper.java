package net.onima.onimaapi.nbt;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_7_R4.NBTTagList;

public class NBTListWrapper {

	private List<NBTCompoundWrapper> list;
	private NBTTagList tagList;

	{
		list = new ArrayList<>();
	}
	
	public NBTListWrapper(NBTTagList tagList) {
		this.tagList = tagList;
	}
	
	public NBTListWrapper() {
		tagList = new NBTTagList();
	}
	
	public void add(NBTCompoundWrapper wrapper) {
		list.add(wrapper);
		tagList.add(wrapper.asNBTCompound());
	}
	
	public List<NBTCompoundWrapper> getList() {
		return list;
	}
	
	public NBTTagList asNBTList() {
		return tagList;
	}
	
}
