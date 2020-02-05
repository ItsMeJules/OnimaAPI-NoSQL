package net.onima.onimaapi.manager;

import net.onima.onimaapi.utils.Config;

public class ConfigManager {
	
	private static Config signSerialConfig, stuffsSerialConfig, regionSerialConfig, crateSerialConfig, gameSerialConfig, mountainSerialConfig,
	reportSerialConfig;
	
	public static void loadConfigs() {
		signSerialConfig = new Config("SUPPRIME PAS PTIT PD", "sign-serial.yml", true, true);
		stuffsSerialConfig = new Config("SUPPRIME PAS PTIT PD", "stuffs-serial.yml", true, true);
		regionSerialConfig = new Config("SUPPRIME PAS PTIT PD", "region-serial.yml", true, true);
		crateSerialConfig = new Config("SUPPRIME PAS PTIT PD", "crate-serial.yml", true, true);
		gameSerialConfig = new Config("SUPPRIME PAS PTIT PD", "game-serial.yml", true, true);
		mountainSerialConfig = new Config("SUPPRIME PAS PTIT PD", "mountains-serial.yml", true, true);
		reportSerialConfig = new Config("SUPPRIME PAS PTIT PD", "reports-serial.yml", true, true);
		
		gameSerialConfig.saveConfig();
		crateSerialConfig.saveConfig();
		regionSerialConfig.saveConfig();
		signSerialConfig.saveConfig();
		stuffsSerialConfig.saveConfig();
		mountainSerialConfig.saveConfig();
		reportSerialConfig.saveConfig();
	}

	public static Config getSignSerialConfig() {
		return signSerialConfig;
	}

	public static Config getStuffsSerialConfig() {
		return stuffsSerialConfig;
	}

	public static Config getRegionSerialConfig() {
		return regionSerialConfig;
	}

	public static Config getCrateSerialConfig() {
		return crateSerialConfig;
	}
	
	public static Config getGameSerialConfig() {
		return gameSerialConfig;
	}
	
	public static Config getMountainSerialConfig() {
		return mountainSerialConfig;
	}
	
	public static Config getReportSerialConfig() {
		return reportSerialConfig;
	}
	
}
