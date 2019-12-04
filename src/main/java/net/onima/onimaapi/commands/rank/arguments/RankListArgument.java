package net.onima.onimaapi.commands.rank.arguments;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.rank.RankType;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class RankListArgument extends BasicCommandArgument {
	
	public RankListArgument() {
		super("list", OnimaPerm.RANK_COMMAND_LIST);
		
		usage = new JSONMessage("§7/rank " + name + " (-p)", "§d§oAffiche la liste des ranks \n§d§o(-p permet d'afficher les rangs comme dans le chat).");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage(ConfigurationService.STAIGHT_LINE);
		rankList(sender, args[args.length - 1].equalsIgnoreCase("-p"));
		sender.sendMessage(ConfigurationService.STAIGHT_LINE);
		return true;
	}
	
	private void rankList(CommandSender sender, boolean rankPrefix) {
		sender.sendMessage("§f§oListe des ranks :");
		ComponentBuilder builder = new ComponentBuilder("");
		
		for (RankType rank : RankType.values()) {
			if (rank == RankType.CONSOLE) continue;
			
			builder.append(rankPrefix ? rank.getPrefix() : rank.getName());
			builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(rank.getDescription()).create()));
			builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/menu ranks"));
			if (rank != RankType.BOT)
				builder.append(", ", FormatRetention.NONE);
		}
		
		Methods.sendJSON(sender, builder.create());
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		return Collections.emptyList();
	}

}
