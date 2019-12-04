package net.onima.onimaapi.commands.crate.arguments;

import java.util.Iterator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.crates.utils.CrateType;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class CrateListArgument extends BasicCommandArgument {

	public CrateListArgument() {
		super("list", OnimaPerm.ONIMAAPI_CRATE_LIST_ARGUMENT);
		
		usage = new JSONMessage("§7/crate " + name + " (type)", "§d§oAffiche la liste des crates.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 1, true))
			return false;
		
		CrateType type = args.length > 1 ? CrateType.fromName(args[1]) : null;
		
		sender.sendMessage("§eCrates (" + Crate.getCrates().size() + ')');
		ComponentBuilder builder = new ComponentBuilder("");
		Iterator<Crate> iterator = Crate.getCrates().iterator();
		
		while (iterator.hasNext()) {
			Crate crate = iterator.next();
			
			if (type != null && type != crate.getType())
				continue;
			
			String name = crate.getDisplayName() == null ? crate.getName() : crate.getDisplayName();
			
			builder.append(name);
			builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eAfficher les informatiions de la crate §6" + name).create()));
			builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/crate info " + crate.getName()));
			
			if (iterator.hasNext())
				builder.append(", ", FormatRetention.NONE);
		}
		
		Methods.sendJSON(sender, builder.create());
		return true;
	}

}
