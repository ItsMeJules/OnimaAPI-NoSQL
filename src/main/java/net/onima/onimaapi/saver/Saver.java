package net.onima.onimaapi.saver;

/**
 * This class has the purpose to save objects in the plugin's cache.
 */
public interface Saver {

	/**
	 * This method should be called at the creation of an object.
	 */
	public void save();
	
	/**
	 * This method should be called when you want to remove this object.
	 */
	public void remove();
	
	/**
	 * This method is called to know when an object is saved in the plugin's cache.
	 * 
	 * @return true if the object is stored in a list.<br>
	 * false if the object is not stored.
	 */
	public boolean isSaved();
	
}
