package net.onima.onimaapi.zone.type.utils;

import java.util.List;

import net.onima.onimaapi.zone.struct.Flag;

/**
 * This class represent a zone that can have a {@link net.jestiz.onimaapi.zone.struct.Flag}
 */
public interface FlagZone {
	
	/**
	 * This method returns all the flags that this area has.
	 * 
	 * @return All the flags for this area.
	 */
	public List<Flag> getFlags();
	
	/**
	 * This method adds a flag to this area.
	 * 
	 * @param flag - Flag to add.
	 */
	public void addFlag(Flag flag);
	
	/**
	 * This method overwrites every flags to set the new ones.
	 * 
	 * @param flags - Flags to set.
	 */
	public void setFlags(Flag... flags);
	
	/**
	 * This method overwrites every flags to set the new ones. It's mostly for deserialization purposes.
	 * 
	 * @param string - A string of flag.
	 */
	public void setFlags(String string);
	
	/**
	 * This method returns all the current flags as a single string. It's mostly for serialization purposes.
	 * @return The flags as one string.
	 */
	public String flagsToString();
	
	/**
	 * This method returns whether the area has flags or not.
	 * 
	 * @return true if the area has at least one flag.<br>
	 * false if the area has no flag.
	 */
	public boolean hasFlags();
	
	/**
	 * This method returns whether the area has the given flags or not.
	 * 
	 * @param flags - Flags to check.
	 * @return true if the area has the given flags.<br>
	 * false if the area doesn't have the given flags.
	 */
	public boolean hasFlags(Flag... flags);
	
	/**
	 * This method returns whether the area has one of the given flags or not.
	 * 
	 * @param flags - Flags to check.
	 * @return true if the area has at least one of the given flags.<br>
	 * false it the area doesn't have any of the given flags.
	 */
	public boolean hasOneOfThisFlags(Flag... flags);
	
	/**
	 * This method returns whether the area has the given flag or not.
	 * 
	 * @param flag - Flag to check.
	 * @return true if the aera has the given flag.<br>
	 * false if the area doesn't have this flag.
	 */
	public boolean hasFlag(Flag flag);
	
	/**
	 * This method removes the given flag from the area.
	 * 
	 * @param flag - Flag to remove.
	 */
	public void removeFlag(Flag flag);
	
}

