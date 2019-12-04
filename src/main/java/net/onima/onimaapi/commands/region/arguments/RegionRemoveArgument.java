package net.onima.onimaapi.commands.region.arguments;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.event.region.RegionChangeEvent;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;
import net.onima.onimaapi.zone.type.Region;

public class RegionRemoveArgument extends BasicCommandArgument {
	
	public RegionRemoveArgument() {
		super("remove", OnimaPerm.ONIMAAPI_REGION_REMOVE_ARGUMENT, new String[] {"delete"});
		usage = new JSONMessage("§7/region " + name + " <name>", "§d§oPermet de supprimer une région.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 2, true))
			return false;
		
		Region region = null;
		
		if ((region = Region.getByName(args[1])) == null) {
			sender.sendMessage("§cLa région " + args[1] + " n'existe pas !");
			return false;
		}
		
		RegionChangeEvent event = new RegionChangeEvent(region, sender instanceof Player ? APIPlayer.getPlayer((Player) sender) : null, RegionChangeEvent.RegionChangementCause.DELETED);
		Bukkit.getPluginManager().callEvent(event);
		
		if (event.isCancelled())
			return true;
		
		OnimaAPI.broadcast("§d§o" + Methods.getRealName(sender) + " §7a supprimé la région §d§o" + region.getDisplayName(sender) + "§7.", OnimaPerm.ONIMAAPI_REGION_REMOVE_ARGUMENT);
		region.remove();
		return true;
	}

	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length != 2 && !OnimaPerm.ONIMAAPI_REGION_REMOVE_ARGUMENT.has(sender))
			return Collections.emptyList();
		
		return Region.getRegions().stream().map(Region::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
	}

}
