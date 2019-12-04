package net.onima.onimaapi.commands;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;

public class LagCommand implements CommandExecutor {
	
	private Runtime runtime;
	
	{
		runtime = Runtime.getRuntime();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) { //TODO sort de la merde à changer en suivant ça : https://stackoverflow.com/questions/17374743/how-can-i-get-the-memory-that-my-java-program-uses-via-javas-runtime-api
		if (!OnimaPerm.LAG_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		double tps = Math.round(Bukkit.spigot().getTPS()[0]);
		double lag = Math.round((1.0D - tps / 20.0D) * 100.0D);
		
		sender.sendMessage(ConfigurationService.STAIGHT_LINE);
		sender.sendMessage("§aTPS du serveur : §" + (tps > 18 ? 'a' : (tps > 15 ? 'e' : 'c')) + tps + "§a.");
		sender.sendMessage("§aLag du serveur : §e" + lag + "§a.");
		
		if (OnimaPerm.LAG_COMMAND_MEMORY.has(sender)) {
			sender.sendMessage("§aProcesseurs disponibles : §e" + runtime.availableProcessors() + "§a.");
			sender.sendMessage("§aMémoire Libre - Mémoire Utilisée/Mémoire Max : §e" + (runtime.totalMemory() / 1048576L) + "MB - " + (runtime.maxMemory() / 1048576L) + "/" + (runtime.freeMemory() / 1048576L) + "MB");
		
			for (World world : Bukkit.getWorlds()) {
				int tileEntities = 0;
				
				for (Chunk chunk : world.getLoadedChunks())
					tileEntities += chunk.getTileEntities().length;

				sender.sendMessage("§aMonde : §e" + world.getName() + "§a, " + world.getLoadedChunks().length + " §achunks chargés, entités " + world.getEntities().size() + "§a, tile entités " + tileEntities + "§a.");
			}
		}
		return true;
	}

}
