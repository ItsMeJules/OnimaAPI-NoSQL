package net.onima.onimaapi.commands.essentials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.StringUtil;

import net.minecraft.util.com.google.common.base.Enums;
import net.minecraft.util.com.google.common.base.Optional;
import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.rank.OnimaPerm;

public class SkullCommand implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!OnimaPerm.ONIMAAPI_SKULL_COMMAND.has(sender)) {
			sender.sendMessage(OnimaAPI.UNKNOWN_COMMAND);
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSeulement les joueurs peuvent utiliser cette commande !");
			return false;
		}
		
        Optional<SkullType> skullType = (args.length > 0) ? Enums.getIfPresent(SkullType.class, args[0]) : Optional.absent();
        ItemStack stack;
        
        if (skullType.isPresent())
            stack = new ItemStack(Material.SKULL_ITEM, 1, (short) skullType.get().ordinal());
        else {
            stack = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            String ownerName = args.length > 0 ? args[0] : sender.getName();
            SkullMeta meta = (SkullMeta) stack.getItemMeta();
            
            meta.setOwner(ownerName);
            stack.setItemMeta((ItemMeta) meta);
        }
        
        ((Player) sender).getInventory().addItem(new ItemStack[]{stack});
        return true;
	}
	
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 1 || !(sender instanceof Player))
            return Collections.emptyList();
        
        List<String> completions = new ArrayList<>();
        
        for (SkullType type : SkullType.values()) {
        	if (StringUtil.startsWithIgnoreCase(type.name(), args[0]))
        		completions.add(type.name());
        }
        	
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
        	if (StringUtil.startsWithIgnoreCase(sender.getName(), args[0]))
        		completions.add(player.getName());
        }
        
        return completions;
    }

}
