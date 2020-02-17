package net.onima.onimaapi.commands.reports.admin.arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.caching.UUIDCache;
import net.onima.onimaapi.gui.menu.report.PlayerReceivedReportsMenu;
import net.onima.onimaapi.gui.menu.report.PlayerSentReportsMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class ReportsUserArgument extends BasicCommandArgument {

	public ReportsUserArgument() {
		super("user", OnimaPerm.REPORTS_USER_ARGUMENT);
		
		playerOnly = true;
		usage = new JSONMessage("§7/reports " + name + " <player> <sent | received>", "§dAffiche tous les reports en relation avec ce joueur.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 3, true))
			return false;
	
		APIPlayer apiPlayer = APIPlayer.getPlayer((Player) sender);
		UUID uuid = UUIDCache.getUUID(args[1]);
		
		if (uuid == null) {
			sender.sendMessage("§cLe joueur " + args[1] + " n'existe pas !");
			return false;
		}
		
		if (args[2].equalsIgnoreCase("sent") || args[2].equalsIgnoreCase("received")) {
			boolean sent = args[2].equalsIgnoreCase("sent") ? true : false;
			
			OfflineAPIPlayer.getPlayer(uuid, offline -> apiPlayer.openMenu(sent ? new PlayerSentReportsMenu(null, offline) : new PlayerReceivedReportsMenu(null, offline)));
			return true;
		} else {
			sender.sendMessage("§cVeuillez préciser quels reports afficher.");
			return false;
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || !OnimaPerm.REPORTS_USER_ARGUMENT.has(sender))
			return Collections.emptyList();
		
		if (args.length == 2)
			return super.onTabComplete(sender, cmd, label, args);
		else if (args.length == 3) {
			List<String> completions = new ArrayList<>();
			
			if (StringUtil.startsWithIgnoreCase("sent", args[2]))
				completions.add("sent");
			
			if (StringUtil.startsWithIgnoreCase("received", args[2]))
				completions.add("received");
			
			return completions;
		} else
			return Collections.emptyList();
	}
	
}
