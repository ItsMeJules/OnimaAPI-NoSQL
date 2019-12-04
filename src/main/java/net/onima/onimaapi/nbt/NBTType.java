package net.onima.onimaapi.nbt;

import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_7_R4.NBTBase;
import net.minecraft.server.v1_7_R4.NBTTagByte;
import net.minecraft.server.v1_7_R4.NBTTagByteArray;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.NBTTagDouble;
import net.minecraft.server.v1_7_R4.NBTTagFloat;
import net.minecraft.server.v1_7_R4.NBTTagInt;
import net.minecraft.server.v1_7_R4.NBTTagIntArray;
import net.minecraft.server.v1_7_R4.NBTTagList;
import net.minecraft.server.v1_7_R4.NBTTagLong;
import net.minecraft.server.v1_7_R4.NBTTagShort;
import net.minecraft.server.v1_7_R4.NBTTagString;

public enum NBTType {

//	0:"END", 1:"BYTE", 2:"SHORT", 3:"INT", 4:"LONG", 5:"FLOAT", 6:"DOUBLE", 7:"BYTE[]", 8:"STRING", 9:"LIST", 10:"COMPOUND", 11:"INT[]"
	
	END((byte) 0, null, Void.class),
	BYTE((byte) 1, NBTTagByte.class, Byte.class, byte.class),
	SHORT((byte) 2, NBTTagShort.class, Short.class, short.class),
	INT((byte) 3, NBTTagInt.class, Integer.class, int.class),
	LONG((byte) 4, NBTTagLong.class, Long.class, long.class),
	FLOAT((byte) 5, NBTTagFloat.class, Float.class, float.class),
	DOUBLE((byte) 6, NBTTagDouble.class, Double.class, double.class),
	BYTE_ARRAY((byte) 7, NBTTagByteArray.class, Byte[].class, byte[].class),
	STRING((byte) 8, NBTTagString.class, String.class),
	LIST((byte) 9, NBTTagList.class, List.class),
	COMPOUND((byte) 10, NBTTagCompound.class, Map.class),
	INT_ARRAY((byte) 11, NBTTagIntArray.class, Integer[].class, int[].class);
	
	private byte type;
	private Class<? extends NBTBase> nbtClass;
	private Class<?>[] clazz;
	
	NBTType(byte type, Class<? extends NBTBase> nbtClass, Class<?>... clazz) {
		this.type = type;
		this.clazz = clazz;
	}
	
	public NBTBase asNBT(Object value) {
		switch (this) {
		case LIST:
			if (value instanceof NBTListWrapper)
				return ((NBTListWrapper) value).asNBTList();
			else return null;
		case COMPOUND:
			if (value instanceof NBTCompoundWrapper)
				return ((NBTCompoundWrapper) value).asNBTCompound();
			else return null;
		case BYTE:
			return new NBTTagByte((byte) value);
		case BYTE_ARRAY:
			return new NBTTagByteArray((byte[]) value);
		case DOUBLE:
			return new NBTTagDouble((double) value);
		case FLOAT:
			return new NBTTagFloat((float) value);
		case INT:
			return new NBTTagInt((int) value);
		case INT_ARRAY:
			return new NBTTagIntArray((int[]) value);
		case LONG:
			return new NBTTagLong((long) value);
		case SHORT:
			return new NBTTagShort((short) value);
		case STRING:
			return new NBTTagString((String) value);
		default:
			return null;
		}
	}
	
	public Object asNormal(NBTBase base) {
		switch (this) {
		case LIST:
			return new NBTListWrapper((NBTTagList) base);
		case COMPOUND:
			return new NBTCompoundWrapper((NBTTagCompound) base);
		case BYTE:
			return ((NBTTagByte) base).f();
		case BYTE_ARRAY:
			return ((NBTTagByteArray) base).c();
		case DOUBLE:
			return ((NBTTagDouble) base).g();
		case FLOAT:
			return ((NBTTagFloat) base).h();
		case INT:
			return ((NBTTagInt) base).d();
		case INT_ARRAY:
			return ((NBTTagIntArray) base).c();
		case LONG:
			return ((NBTTagLong) base).c();
		case SHORT:
			return ((NBTTagShort) base).e();
		case STRING:
			return ((NBTTagString) base).a_();
		default:
			return null;
		}
	}

	public boolean isAssignable(Class<?> clazz) {
		for (Class<?> cl : this.clazz) {
			if (cl.isAssignableFrom(clazz))
				return true;
		}
		
		return false;
	}
	
	public byte getType() {
		return type;
	}
	
	public Class<? extends NBTBase> getNBTClass() {
		return nbtClass;
	}
	
	public Class<?>[] getClazz() {
		return clazz;
	}
	
	public static NBTType fromByte(byte type) {
		for (NBTType listType : values()) {
			if (listType.type == type)
				return listType;
		}
		
		return null;
	}
	
	public static NBTType fromNBTClass(Class<? extends NBTBase> nbtClass) {
		for (NBTType listType : values()) {
			if (listType.getNBTClass().equals(nbtClass))
				return listType;
		}
		
		return null;
	}
	
	public static NBTType fromClass(Class<?> clazz) {
		for (NBTType listType : values()) {
			if (listType.isAssignable(clazz))
				return listType;
		}
		
		return null;
	}
	
}
