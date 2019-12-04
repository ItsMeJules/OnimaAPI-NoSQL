package net.onima.onimaapi.crates.prizes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.Methods;

public class CommandPrize extends Prize {

	private String command;
//	private Map<String, String> toReplace;
//	
//	{
//		toReplace = new HashMap<>();
//	}
	
	public CommandPrize(double chance, ItemStack displayItem, String command, PrizeRarity rarity) {
		super(chance, displayItem, rarity);
		
		this.command = command.replace("/", "");
	}
	
	@Override
	public boolean give(Player player) {
//		String command = replaceArgs(this.command, player);
		
		if (command.contains("%player%"))
			command = command.replace("%player%", player.getName());
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
		return true;
	}
	
//	public String replaceArgs(String command, Player player) {
//		String newCommand = command;
//		
//		for (Entry<String, String> entry : toReplace.entrySet())
//			newCommand = newCommand.replace(entry.getKey(), entry.getValue());
//		
//		return newCommand;
//	}

//	public Map<String, String> getArgsToReplace() {
//		return toReplace;
//	}
	
	@Override
	public String asSerializableString() {
		BetterItem item = new BetterItem(displayItem, displayItem.getItemMeta().getDisplayName(), displayItem.getItemMeta().getLore());
		
		item.removeLore(item.getLore().size() - 1);
		
		return super.asSerializableString() + ';' + Methods.serializeItem(item.toItemStack(), true) + ';' + command;
	}
	
	public static CommandPrize fromString(String[] parts) {
		Double chance = Methods.toDouble(parts[0]);
		PrizeRarity rarity = PrizeRarity.fromName(parts[1]);
		boolean effect = Boolean.valueOf(parts[2]);
		
		CommandPrize prize = new CommandPrize(chance, Methods.deserializeItem(parts[3], true), parts[4], rarity);
		
		prize.setEffect(effect);
		
		return prize;
	}
	
}
