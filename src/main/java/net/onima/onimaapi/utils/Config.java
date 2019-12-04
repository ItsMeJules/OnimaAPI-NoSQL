package net.onima.onimaapi.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.minecraft.util.org.apache.commons.io.FileUtils;
import net.onima.onimaapi.OnimaAPI;

public class Config {

	private File file;
	private FileConfiguration config;
	private String path, fileName;
	
	private static final OnimaAPI plugin;
	public static final String DEFAULT_PATH;
	
	/**
	 * List of the saved configs.
	 */
	private static List<Config> configs;
	
	static { 
		plugin = OnimaAPI.getInstance();
		DEFAULT_PATH = plugin.getDataFolder().toString() + "/";
		configs = new ArrayList<>();
	}

	/**
	 * This constructor calls this one {@link #Config(String, String, boolean, boolean)}.<br>
	 * The changing thing is the path of where this config will be created. Here the default plugin path.
	 * 
	 * @param fileName - The file name
	 * @param create - Creating the config
	 * @param save - Adding the object to the list.
	 */
	public Config(String fileName, boolean create, boolean save) {
		this("", fileName, create, save);
	}
	
	/**
	 * If create is set to true the method {@link #create(boolean, boolean)} will be called with the second argument with the
	 * save value.
	 * 
	 * @param path - The path to save the file into.
	 * @param fileName - The file name
	 * @param create - Creating the config
	 * @param save - Adding the object to the {@link #configs}.
	 */
	public Config(String path, String fileName, boolean create, boolean save) {
		this.path = DEFAULT_PATH + path;
		this.fileName = fileName;
		if (create)
			create(true, save);
	}
	
	/**
	 * This method automatically creates and load the file as a YAML.
	 * 
	 * @param file - The file to create the YAML from.
	 */
	public Config(File file) {
		path = file.getPath();
		fileName = file.getName();
		this.file = file;
		
		config = YamlConfiguration.loadConfiguration(file);
		configs.add(this);
	}
	
	/**
	 * This method creates the file.
	 * 
	 * @param yaml - Make this file a YAML file.
	 * @param save - Adding the object to the {@link #configs}.
	 */
	public void create(boolean yaml, boolean save) {
		file = new File(path, fileName);
		if (save)
			configs.add(this);
		
		if (yaml)
			config = YamlConfiguration.loadConfiguration(file);
	}
	
	/**
	 * This method copies a file in the plugin ressource path to another given location.<br>
	 * If the parameter outPutFile is empty, the output file will have the resource file name.
	 * 
	 * @param resourcePath - The config at the base of the plugin.
	 * @param overwrite - Replacing the file if there's already one created.
	 * @param outPutFile - The name of the output file
	 * @param saveOutPutFile - Saving the output file.
	 */
    public void setDefaultContent(String resourcePath, boolean overwrite, String outPutFile, boolean saveOutPutFile) {
        if (resourcePath == null || resourcePath.equals("")) throw new IllegalArgumentException("ResourcePath cannot be null or empty");

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = plugin.getResource(resourcePath);

        if (in == null) throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + file);

        file = new File(path, resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(path, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

        if (!outDir.exists()) outDir.mkdirs();

        try {
            if (!file.exists())
            	write(in, file);
            else if (overwrite) {
            	if (saveOutPutFile)
            		FileUtils.copyFile(file, new File(path, outPutFile));
            	write(in, file);
            }
        } catch(IOException ex) {
        	plugin.getLogger().log(Level.SEVERE, "Could not save " + file.getName() + " to " + file, ex);
        }
    }
    
    /**
     * This method writes the data picked in the InputStream.
     * 
     * @param in - The InputStream where you read the data from.
     * @param file - The file where you wish to write the data in.
     * @throws FileNotFoundException
     */
    private void write(InputStream in, File file) throws FileNotFoundException {
        try {
        	OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            
            int len;
            while ((len = in.read(buf)) > 0) 
                out.write(buf, 0, len);
            
            out.close();
            in.close();
        } catch (IOException e) {
        	plugin.getLogger().log(Level.SEVERE, "Could not save " + file.getName() + " to " + file, e);
		}
    }
	
    /**
     * This method reloads the yaml config.
     */
	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	/**
	 * Saves the config.
	 */
	public void saveConfig() {
		try {
			config.save(file);
		} catch (IOException e) {
			plugin.getLogger().log(Level.SEVERE, "Impossible to save "+file.getName(), e);
		}
	}
	
	/**
	 * This method returns the FileConfiguration from this class.
	 * 
	 * @return The FileConfiguration from this class.
	 */
	public FileConfiguration getConfig() {
		return config;
	}
	
	/**
	 * This method checks if a file exists.
	 * 
	 * @return true if the file exists.<br>
	 * false if the file doesn't exist.
	 */
	public boolean exists() {
		return file.exists();
	}
	
	/**
	 * This method returns the config's name.
	 * 
	 * @return The config name.
	 */
	public String getName() {
		return fileName;
	}
	
	/**
	 * This method returns the config's file.
	 * 
	 * @return The config file.
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * This methods checks if the file is created.
	 * 
	 * @return true if the file is != <tt>null</tt><br>.
	 * false if the file == <tt>null</tt>.
	 */
	public boolean isFileCreated() {
		return file != null;
	}
	
	/**
	 * This methods checks if YAML is created.
	 * 
	 * @return true if the YAML != <tt>null</tt>.<br>
	 * false if the YAML == <tt>null</tt>.
	 */
	public boolean isYamlCreated() {
		return config != null;
	}
	
	/**
	 * This methods removes a value at the given path and saves the modifications if the user asked for it.
	 * 
	 * @param key - Path to the value to remove.
	 * @param save - Saving the modification.
	 */
	public void remove(String key, boolean save) {
		config.set(key, null);
		if (save) {
			saveConfig();
			reloadConfig();
		}
	}

	/**
	 * This method gets the Config from a given File.
	 * 
	 * @param file - File of the config.
	 * @return The matched file or a new one if nothing was found.
	 */
	public static Config fromFile(File file) {
		for (Config config : configs) {
			if (config.getFile().equals(file))
				return config;
		}
		return new Config(file);
	}
	
	/**
	 * 
	 * @param name - Name of the config.
	 * @return The found Config.<br>
	 * <tt>null</tt> if no Config was found.
	 */
	public static Config fromName(String name) {
		for (Config config : configs) {
			if (config.getName().equalsIgnoreCase(name))
				return config;
		}
		return null;
	}

	/**
	 * {@link #configs}
	 * 
	 * @return A list with some registered Config.
	 */
	public static List<Config> getConfigs() {
		return configs;
	}
}
