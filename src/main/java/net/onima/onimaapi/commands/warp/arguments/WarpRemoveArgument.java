package net.onima.onimaapi.commands.warp.arguments;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Warp;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class WarpRemoveArgument extends BasicCommandArgument {

	public WarpRemoveArgument() {
		super("remove", OnimaPerm.ONIMAAPI_WARP_REMOVE_ARGUMENT);
		
		usage = new JSONMessage("§7/warp " + name + " <name>", "§d§oPermet de supprimer un warp.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 2, true))
			return false;
		
		Warp warp;
		
		if ((warp = Warp.getByName(args[1])) == null) {
			sender.sendMessage("§cLe warp " + args[1] + " n'existe pas.");
			return false;
		}
		
		warp.remove();
		sender.sendMessage("§dVous §7avez supprimé le warp §d" + warp.getName() + "§7.");
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 2 || !OnimaPerm.ONIMAAPI_WARP_REMOVE_ARGUMENT.has(sender))
			return Collections.emptyList();

		return Warp.getWarps().stream().map(Warp::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
	}

}
