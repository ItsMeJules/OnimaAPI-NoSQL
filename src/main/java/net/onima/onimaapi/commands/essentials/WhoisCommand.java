package net.onima.onimaapi.commands.essentials;

import java.util.Map;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableMap;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.time.Time.LongTime;

public class WhoisCommand implements CommandExecutor { //TODO mettre un max d'informations

	private JSONMessage usage = new JSONMessage("§7/whois <player>", "§d§oAffiche des informations sur le joueur.");
	private Map<Integer, String> versions = ImmutableMap.of(4, "1.7.2 -> 1.7.5", 5, "1.7.6 -> 1.7.10", 47, "1.8 -> 1.8.8");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_WHOIS_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (args.length < 1) {
			usage.send(sender, "§7Utilisation : ");
			return false;
		}
		
		APIPlayer apiPlayer = APIPlayer.getPlayer(args[0]);
		
		if (apiPlayer != null) {
			Player target = apiPlayer.toPlayer();
			Location location = target.getLocation();
			World world = location.getWorld();
			
			sender.sendMessage("§7" + ConfigurationService.STAIGHT_LINE);
			sender.sendMessage(" §a[" + apiPlayer.getColoredName(true) + "§a]");
            sender.sendMessage("§e  Vie : §6" + apiPlayer.getHealth() + '/' + apiPlayer.getMaxHealth());
            sender.sendMessage("§e  Faim : §6" + target.getFoodLevel() + '/' + 20 + " (" + target.getSaturation() + " saturation)");
            sender.sendMessage("§e  Expérience : §6" + apiPlayer.getExperienceManager().getCurrentExp());
            sender.sendMessage("§e  Location : §6" + world.getName() + " §7" + '[' + WordUtils.capitalizeFully(world.getEnvironment().name().replace('_', ' ')) + "] §6" + '(' + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ')');
            sender.sendMessage("§e  Vanish : §6" + apiPlayer.isVanished() + " (rank=" + apiPlayer.getRank().getRankType().getName() + "§6)");
            sender.sendMessage("§e  Opérateur : §6" + target.isOp());
            sender.sendMessage("§e  Game Mode : §6" + WordUtils.capitalizeFully(target.getGameMode().name().replace('_', ' ')));
            sender.sendMessage("§e  Disguise : §6" + (apiPlayer.getDisguiseManager().isDisguised() ? "true §7(§e" + apiPlayer.getDisguiseManager().getName() + "§7)" : "false"));
            sender.sendMessage("§e  Inactivité : §6" + LongTime.setYMDWHMSFormat(Methods.getIdleTime(target)));
            sender.sendMessage("§e  Temps de jeu : §6" + LongTime.setYMDWHMSFormat(apiPlayer.getPlayTime().getPlayTime()));
            sender.sendMessage("§e  Adresse IP : §6" + target.getAddress().getHostString());
            int version = ((CraftPlayer) apiPlayer.toPlayer()).getHandle().playerConnection.networkManager.getVersion();
            sender.sendMessage("§e  Version du client : §6" + version + " §7[" + versions.get(version) + "]");
            sender.sendMessage("§7" + ConfigurationService.STAIGHT_LINE);
		} else {
			OfflineAPIPlayer.getPlayer(args[0], offline -> {
				if (offline == null) {
					sender.sendMessage("§c" + args[0] + " n'existe pas !");
					return;
				}
				
				sender.sendMessage("§7" + ConfigurationService.STAIGHT_LINE);
				sender.sendMessage(" §a[" + offline.getColoredName(false) + "§a]");
				sender.sendMessage("§e  Vanish : §6" + offline.isVanished() + " (rank=" + offline.getRank().getRankType().getName() + "§6)");
				sender.sendMessage("§e  Opérateur : §6" + offline.getOfflinePlayer().isOp());
				sender.sendMessage("§e  Temps de jeu : §6" + LongTime.setYMDWHMSFormat(offline.getPlayTime().getPlayTime()));
				sender.sendMessage("§e  Adresse IP : §6" + offline.getIP());
				sender.sendMessage("§7" + ConfigurationService.STAIGHT_LINE);
			});
		}
		
		return true;
	}

}
