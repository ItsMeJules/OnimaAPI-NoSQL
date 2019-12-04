package net.onima.onimaapi.commands;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.google.common.collect.Lists;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.mod.ModItem;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;

public class ModCommand implements CommandExecutor, TabExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.MOD_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}
		
		Player player = (Player) sender;
		APIPlayer apiPlayer = APIPlayer.getPlayer(player);	
		
		if (args.length < 1) {
			if (!apiPlayer.isInModMode()) {
				apiPlayer.setModMode(true);
				player.sendMessage(ModItem.MOD_PREFIX + " §fVous êtes §amaintenant §fen mod mode !");
			} else {
				apiPlayer.setModMode(false);
				player.sendMessage(ModItem.MOD_PREFIX + " §fVous n'êtes §cplus §fen mod mode !");
			}
		} else if (args[0].equalsIgnoreCase("settings"))
			apiPlayer.openMenu(apiPlayer.getMenu("mod_options"));
		
		
		
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player && args.length == 1 && StringUtil.startsWithIgnoreCase("settings", args[0])
				&& OnimaPerm.MOD_COMMAND.has(sender))
			return Lists.newArrayList("settings");
		else
			return Collections.emptyList();
	}

}
