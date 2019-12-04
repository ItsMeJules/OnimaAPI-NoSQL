package net.onima.onimaapi.commands.crate.arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.crates.SupplyCrate;
import net.onima.onimaapi.crates.VirtualCrate;
import net.onima.onimaapi.crates.booster.KeyBooster;
import net.onima.onimaapi.crates.booster.NoBooster;
import net.onima.onimaapi.crates.booster.PrizeRarityBooster;
import net.onima.onimaapi.crates.openers.Key;
import net.onima.onimaapi.crates.openers.PhysicalKey;
import net.onima.onimaapi.crates.openers.VirtualKey;
import net.onima.onimaapi.crates.prizes.PrizeRarity;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class CrateKeyArgument extends BasicCommandArgument {
	
	public CrateKeyArgument() {
		super("key", OnimaPerm.ONIMAAPI_CRATE_KEY_ARGUMENT, new String[] {"givekey"});
		
		usage = new JSONMessage("§7/crate " + name + " <crate> <prize-rarity:multiplicator> (player)", "§d§oGive une clef permettant d'ouvrir une crate.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 3, true))
			return false;
	
		Crate crate = null;
		
		if ((crate = Crate.getByName(args[1])) == null) {
			sender.sendMessage("§cLa crate " + args[1] + " n'existe pas !");
			return false;
		}
		
		if (crate instanceof SupplyCrate) {
			sender.sendMessage("§cLes supply crates n'ont pas besoin de clefs !");
			return false;
		}
		
		KeyBooster keyBooster;
		
		if (args[2].equalsIgnoreCase("no"))
			keyBooster = new NoBooster();
		else {
			String[] booster = args[2].split(":");
			PrizeRarity rarity = PrizeRarity.fromName(booster[0]);
			
			if (rarity == null) {
				sender.sendMessage("§cLe rarité de prix " + booster[0] + " n'existe pas !");
				return false;
			}
			
			Double multp = Methods.toDouble(booster[1]);
			
			if (multp == null) {
				sender.sendMessage("§c" + booster[1] + " n'est pas un nombre !");
				return false;
			}
			
			keyBooster = new PrizeRarityBooster(rarity, multp);
		}
		
		Player target = null;
		
		if (args.length > 3) {
			target = Bukkit.getPlayer(args[3]);
			
			if (target == null) {
				sender.sendMessage("§cLe joueur spécifié n'existe pas !");
				return false;
			}
		} else if (sender instanceof Player)
			target = (Player) sender;
		else {
			sender.sendMessage("§cSeulement un joueur peut recevoir une clef !");
			return false;
		}
		
		Key key;
	
		if (!(crate instanceof VirtualCrate))
			key = new PhysicalKey(crate.getDisplayName() == null ? crate.getName() : crate.getDisplayName(), keyBooster);
		else
			key = new VirtualKey(crate.getDisplayName() == null ? crate.getName() : crate.getDisplayName(), keyBooster);
			
		
		sender.sendMessage("§dVous §7avez donné une clef §e" + (key instanceof PhysicalKey ? "physique" : "virtuelle") + " §7à §d" + Methods.getName(target, true));
		key.give(APIPlayer.getPlayer(target), true);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 2)
			return Collections.emptyList();
		
		if (args.length == 2)
			return Crate.getCrates().stream().filter(crate -> !(crate instanceof SupplyCrate)).map(Crate::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
		
		List<String> completions = new ArrayList<>();
		
		if (args.length == 3) {
			if (StringUtil.startsWithIgnoreCase("no", args[2]))
				completions.add("no");
			
			for (PrizeRarity rarity : PrizeRarity.values()) {
				if (StringUtil.startsWithIgnoreCase(rarity.name(), args[2]))
					completions.add(rarity.name() + ":2");
			}
		} else if (args.length == 4)
			return Bukkit.getOnlinePlayers().parallelStream().map(Player::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[3])).collect(Collectors.toList());
			
		return completions;
	}

}
