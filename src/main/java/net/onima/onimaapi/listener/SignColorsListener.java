package net.onima.onimaapi.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import net.onima.onimaapi.rank.OnimaPerm;
import net.onima.onimaapi.utils.ConfigurationService;

public class SignColorsListener implements Listener {
	
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onSignCreate(SignChangeEvent event) {
        Player player = event.getPlayer();
        
        if (player != null && player.hasPermission("base.sign.colour")) {
            String[] lines = event.getLines();
            String first = lines[0];
            
            if (!OnimaPerm.CREATE_SIGN_SHOP.has(player) && (first.equalsIgnoreCase(ChatColor.stripColor(ConfigurationService.SHOP_SIGN_BUY_LINE))
            		|| first.equalsIgnoreCase(ChatColor.stripColor(ConfigurationService.SHOP_SIGN_SELL_LINE)))) {
                player.sendMessage("§cVous ne pouvez pas créer ce genre de panneau.");
                event.setCancelled(true);
            }
            
            for (int i = 0; i < lines.length; ++i)
                event.setLine(i, ChatColor.translateAlternateColorCodes('&', lines[i]));
        }
    }

}
