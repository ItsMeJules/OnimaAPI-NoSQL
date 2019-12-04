package net.onima.onimaapi.commands.region.arguments;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.rank.RankType;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;
import net.onima.onimaapi.zone.type.Region;

public class RegionRankArgument extends BasicCommandArgument {

	public RegionRankArgument() {
		super("rank", OnimaPerm.ONIMAAPI_REGION_RANK_ARGUMENT);
		usage = new JSONMessage("§7/region " + name + " <region> <rank>", "§d§oDéfinit le rank minimum pour accéder à cette région.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 3, true))
			return false;
		
		Region region = null;
		
		if ((region = Region.getByName(args[1])) == null) {
			sender.sendMessage("§cLa région " + args[1] + " n'existe pas !");
			return false;
		}
		
		RankType rankType = RankType.fromString(args[2]);
		
		if (rankType == null) {
			sender.sendMessage("§cLe rank " + args[2] + " n'existe pas !");
			return false;
		}
		
		if (rankType == region.getAccessRank()) {
			sender.sendMessage("§cLa région " + region.getName() + " a déjà le rang d'accès définit sur " + rankType.name() + '.');
			return false;
		}
		
		region.setAccessRank(rankType);
		sender.sendMessage("§dVous §7avez définit le rank d'accès à la région §d" + region.getDisplayName(sender) + " §7sur §d" + rankType.name() + "§7.");
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_REGION_RANK_ARGUMENT.has(sender))
			return Collections.emptyList();
		else if (args.length == 2)
			return Region.getRegions().stream().map(Region::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
		else if (args.length == 3)
			return Arrays.stream(RankType.values()).map(RankType::name).filter(name -> StringUtil.startsWithIgnoreCase(name, args[2])).collect(Collectors.toList());
		else return Collections.emptyList();
	}

}
