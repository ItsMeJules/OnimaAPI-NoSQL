package net.onima.onimaapi.commands.warp.arguments;

import java.util.Iterator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.Warp;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class WarpListArgument extends BasicCommandArgument {

	public WarpListArgument() {
		super("list", OnimaPerm.ONIMAAPI_LIST_REMOVE_ARGUMENT);
		
		usage = new JSONMessage("§7/warp " + name, "§d§oPermet d'afficher la liste des warps.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage("§9Warps (" + Warp.getWarps().size() + ')');
		ComponentBuilder builder = new ComponentBuilder("");
		Iterator<Warp> iterator = Warp.getWarps().iterator();
		
		while (iterator.hasNext()) {
			Warp warp = iterator.next();
			
			builder.append(warp.getName());
			builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eSe téléporter au warp §6" + warp.getName()).create()));
			builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp " + warp.getName()));
			
			if (iterator.hasNext())
				builder.append(", ", FormatRetention.NONE);
		}
		
		Methods.sendJSON(sender, builder.create());
		return true;
	}

}
