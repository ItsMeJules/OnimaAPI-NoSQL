package net.onima.onimaapi.commands.crate.arguments;

import java.util.Collections;
import java.util.Iterator;
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
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.JSONMessage;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.commands.BasicCommandArgument;

public class CrateFireworkArgument extends BasicCommandArgument {
	
	public CrateFireworkArgument() {
		super("firework", OnimaPerm.ONIMAAPI_CRATE_FIREWORK_ARGUMENT);
		
		usage = new JSONMessage("§7/crate " + name + " <crate>", "§d§oMet un feu d'artifice au prix.");
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
		CrateFireworkMenu menu = new CrateFireworkMenu(player, crate);
		
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
	
	public class CrateFireworkMenu extends PacketMenu {

		private Player player;
		private Crate crate;
		
		public CrateFireworkMenu(Player player, Crate crate) {
			super("mdr", "Mettre un feu d'artifice", Methods.menuSizeFromInteger(crate.getPrizes().size()), false);
			
			this.player = player;
			this.crate = crate;
		}

		@Override
		public void registerItems() {
			Iterator<Prize> iterator = crate.getPrizes().iterator();
			
			while (iterator.hasNext()) {
				Prize prize = iterator.next();
				BetterItem item = new BetterItem(prize.getDisplayItem(), prize.getDisplayItem().getItemMeta().getDisplayName(), prize.getDisplayItem().getItemMeta().getLore());
				
				item.addLore("§2Feu d'artifice : " + (prize.getEffect() != null ? "§atrue" : "§cfalse"));
				
				CallbackButton<Player> button = new CallbackButton<Player>(item, player);
				
				button.setCallBack((player) -> {
					boolean effect = prize.getEffect() != null;
					
					prize.setEffect(!effect);
					button.getButtonItem(null).setLastInLore("§2Feu d'artifice : " + (!effect ? "§atrue" : "§cfalse"));
					player.sendMessage("§7Vous avez " + (!effect ? "§aajouté" : "§csupprimé") + " §7un firework !");
					
					updateLocalized(player, button.getEvent().getSlot());
					
					button.getEvent().setCancelled(true);
					return true;
				});
				
				buttons.put(buttons.size(), button);
			}
		}

	}

}
