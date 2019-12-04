package net.onima.onimaapi.commands.crate.arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.crates.SupplyCrate;
import net.onima.onimaapi.crates.prizes.CommandPrize;
import net.onima.onimaapi.crates.prizes.PrizeRarity;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.gui.menu.AnvilInputMenu;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class CrateAddCommandArgument extends BasicCommandArgument {

	public CrateAddCommandArgument() {
		super("addcommand", OnimaPerm.ONIMAAPI_CRATE_ADDCOMMAND_ARGUMENT);
		
		usage = new JSONMessage("§7/crate " + name + " <crate> <chance> <rarity>", "§d§oAjoute une commande dans la crate.");
		playerOnly = true;
	}

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 4, true))
			return false;
	
		final Crate crate = Crate.getByName(args[1]);
		
		if (crate == null) {
			sender.sendMessage("§cLa crate " + args[1] + " n'existe pas !");
			return false;
		}
		
		if (crate instanceof SupplyCrate) {
			sender.sendMessage("§cVous ne pouvez pas ajouter de commandes dans un drop !");
			return false;
		}
		
		Double chance = Methods.toDouble(args[2]);
		
		if (chance == null) {
			sender.sendMessage("§c" + args[2] + " n'est pas un nombre !");
			return false;
		}
		
		final PrizeRarity rarity = PrizeRarity.fromName(args[3]);
		
		if (rarity == null) {
			sender.sendMessage("§cLe rarité de prix " + args[3] + " n'existe pas !");
			return false;
		}

		Player player = (Player) sender;
		final ItemStack hand = player.getItemInHand();
		
		if (hand == null) {
			sender.sendMessage("§cVous devez avoir un item dans la main !");
			return false;
		}
		
		new AnvilInputMenu(player.getUniqueId(), "lol", "noahpd", "Entez votre commande ici.") {
			
			@Override
			public void onEvent(AnvilClickEvent event) {
				if (event.getSlot() == AnvilSlot.OUTPUT) {
					sender.sendMessage("§dVous §7avez ajouté une commande dans la crate " + crate.getName() + "§7.");
					crate.getPrizes().add(new CommandPrize(chance, hand, event.getInput(), rarity));
					event.setWillClose(true);
					event.setWillDestroy(true);
				}
			}
			
		}.open(APIPlayer.getPlayer(player));
		
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2)
			return Crate.getCrates().stream().map(Crate::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
		else if (args.length == 4) {
			List<String> completions = new ArrayList<>();
			
			for (PrizeRarity rarity : PrizeRarity.values()) {
				if (StringUtil.startsWithIgnoreCase(rarity.name(), args[3]))
					completions.add(rarity.name());
			}
			
			return completions;
		} else
			return Collections.emptyList();
	}
	
}
