package net.onima.onimaapi.saver;

/**
 * This class has the purpose to reuse objects by serializing them into the server's files.
 */
public interface FileSaver extends Saver {
	
	/**
	 * This method should be only called when the server shuts down/saves.
	 */
	public void serialize();
	
	/**
	 * This method should be only called when the server shuts down/saves.
	 */
	public void refreshFile();

}
