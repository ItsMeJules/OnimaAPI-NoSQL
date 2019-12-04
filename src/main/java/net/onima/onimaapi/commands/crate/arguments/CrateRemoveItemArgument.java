package net.onima.onimaapi.commands.crate.arguments;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import net.onima.onimaapi.crates.prizes.Prize;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.CallbackButton;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class CrateRemoveItemArgument extends BasicCommandArgument {

	public CrateRemoveItemArgument() {
		super("removeitem", OnimaPerm.ONIMAAPI_CRATE_REMOVEITEM_ARGUMENT);
		
		usage = new JSONMessage("§7/crate " + name + " <crate>", "§d§oSupprime des items d'une crate.");
		playerOnly = true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (checks(sender, args, 2, true))
			return false;
		
		Crate crate = null;
		
		if ((crate = Crate.getByName(args[1])) == null) {
			sender.sendMessage("§cLa crate " + args[1] + " n'existe pas !");
			return false;
		}
		
		Player player = (Player) sender;
		CrateRemoveMenu menu = new CrateRemoveMenu(player, crate);
		
		menu.open(APIPlayer.getPlayer(player));
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2)
			return Crate.getCrates().stream().map(Crate::getName).filter(name -> StringUtil.startsWithIgnoreCase(name, args[1])).collect(Collectors.toList());
		else
			return Collections.emptyList();
	}
	
	public class CrateRemoveMenu extends PacketMenu {

		private Player player;
		private Crate crate;
		
		public CrateRemoveMenu(Player player, Crate crate) {
			super("mdr", "Supprimer des items", Methods.menuSizeFromInteger(crate.getPrizes().size()), false);
			
			this.player = player;
			this.crate = crate;
		}

		@Override
		public void registerItems() {
			for (Prize prize : crate.getPrizes()) {
				CallbackButton<Player> button = new CallbackButton<Player>(prize.getDisplayItem(), player);
				
				button.setCallBack((player) -> {
					crate.getPrizes().remove(prize);
					button.getEvent().setCancelled(true);
					player.sendMessage("§aVous avez supprimé un item de la crate !");
					
					updateItems(player);
					return true;
				});
				
				buttons.put(buttons.size(), button);
			}
		}
	}
	
}
