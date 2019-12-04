package net.onima.onimaapi.commands.cooldown.arguments;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.cooldown.utils.Cooldown;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;
import net.onima.onimaapi.utils.time.Time.LongTime;
import net.onima.onimaapi.utils.time.TimeUtils;

public class CooldownAddArgument extends BasicCommandArgument {

	public CooldownAddArgument() {
		super("add", OnimaPerm.ONIMAAPI_COOLDOWN_ADD_ARGUMENT);
		usage = new JSONMessage("§7/cooldown " + name + " <player> <cooldown> (time)", "§d§oPermet d'ajouter un cooldown.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 3, true))
			return false;
		
		OfflineAPIPlayer.getPlayer(args[1], offline -> {
			if (offline == null) {
				sender.sendMessage("§cLe joueur " + args[1] + " ne s'est jamais connecté sur le serveur !");
				return;
			}
			
			Cooldown cooldown = Cooldown.getCooldown(args[2]);
			
			if (cooldown == null) {
				sender.sendMessage("§cLe cooldown " + args[2] + " n'existe pas !");
				return;
			}
			
			long time;
			
			if (args.length > 3) {
				time = TimeUtils.timeToMillis(args[3]);
				
				if (time == -1) {
					sender.sendMessage("§cLa valeur " + args[3] + " n'est pas un nombre !");
					return;
				} else if (time == -2) {
					sender.sendMessage("§cMauvais format pour : " + args[3] + " il faut écrire les deux premières lettres du temps. Exemple : §o/koth setcaptime §c15mi pour 15 minutes.");
					return; 
				}
			} else
				time = cooldown.getDuration();
			
			sender.sendMessage("§dVous §7avez ajouté le cooldown §d" + cooldown.getName() + " §7à §d" + Methods.getName(offline, true) + " §7pour §d" + LongTime.setYMDWHMSFormat(time) + "§7.");
			
			if (offline.isOnline())
				((APIPlayer) offline).sendMessage("§d" + Methods.getRealName(sender) + " §7vous a ajouté le cooldown §d" + cooldown.getName() + "§7.");
			
			offline.startCooldown(cooldown, time);
		});
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_COOLDOWN_ADD_ARGUMENT.has(sender))
			return Collections.emptyList();
		
		if (args.length == 2)
			return super.onTabComplete(sender, cmd, label, args);
		else if (args.length == 3)
			return Cooldown.getCooldowns().stream().map(Cooldown::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[2])).collect(Collectors.toList());
		else
			return Collections.emptyList();
	}
}
