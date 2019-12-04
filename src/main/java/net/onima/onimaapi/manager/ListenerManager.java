package net.onima.onimaapi.manager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.listener.BookDisenchantListener;
import net.onima.onimaapi.listener.ConnectionLogListener;
import net.onima.onimaapi.listener.CrateListener;
import net.onima.onimaapi.listener.DeathListener;
import net.onima.onimaapi.listener.DeathSignListener;
import net.onima.onimaapi.listener.DisguiseListener;
import net.onima.onimaapi.listener.ElevatorSignListener;
import net.onima.onimaapi.listener.EntityLimitListener;
import net.onima.onimaapi.listener.ExpMultiplierListener;
import net.onima.onimaapi.listener.FastTileEntityListener;
import net.onima.onimaapi.listener.FreezeListener;
import net.onima.onimaapi.listener.GlobalOptionsListener;
import net.onima.onimaapi.listener.HorseProtectListener;
import net.onima.onimaapi.listener.InvseeListener;
import net.onima.onimaapi.listener.LimitersListener;
import net.onima.onimaapi.listener.MenuListener;
import net.onima.onimaapi.listener.MobMergeListener;
import net.onima.onimaapi.listener.ModListener;
import net.onima.onimaapi.listener.MountainListener;
import net.onima.onimaapi.listener.OreListener;
import net.onima.onimaapi.listener.ProtectionListener;
import net.onima.onimaapi.listener.PunishmentListener;
import net.onima.onimaapi.listener.RankReceivedListener;
import net.onima.onimaapi.listener.ShopSignListener;
import net.onima.onimaapi.listener.SignColorsListener;
import net.onima.onimaapi.listener.SpecialInventoryListener;
import net.onima.onimaapi.listener.StatItemsListener;
import net.onima.onimaapi.listener.WandListener;
import net.onima.onimaapi.listener.WorldBorderListener;
import net.onima.onimaapi.listener.fixes.BlockHitFixListener;
import net.onima.onimaapi.listener.fixes.FixesListener;

/**
 * This class handles all the bukkit's listeners.
 */
public class ListenerManager {
	
	private OnimaAPI plugin;
	private PluginManager pm;
	private WorldBorderListener worldBorderListener;
	
	public ListenerManager(OnimaAPI plugin) {
		this.plugin = plugin;
		this.pm = Bukkit.getPluginManager();
	}
	
	public void registerListener() {
		pm.registerEvents(new ConnectionLogListener(), plugin);
		pm.registerEvents(new WandListener(), plugin);
		pm.registerEvents(worldBorderListener = new WorldBorderListener(), plugin);
		pm.registerEvents(new MenuListener(), plugin);
		pm.registerEvents(new ModListener(), plugin);
		pm.registerEvents(new StatItemsListener(), plugin);
		pm.registerEvents(new OreListener(), plugin);
		pm.registerEvents(new BookDisenchantListener(), plugin);
		pm.registerEvents(new GlobalOptionsListener(), plugin);
		pm.registerEvents(new LimitersListener(), plugin);
		pm.registerEvents(new MountainListener(), plugin);
		pm.registerEvents(new ProtectionListener(), plugin);
		pm.registerEvents(new FastTileEntityListener(), plugin);
		pm.registerEvents(new ExpMultiplierListener(), plugin);
		pm.registerEvents(new MobMergeListener(), plugin);
		pm.registerEvents(new EntityLimitListener(), plugin);
		pm.registerEvents(new CrateListener(), plugin);
		pm.registerEvents(new ShopSignListener(), plugin);
		pm.registerEvents(new ElevatorSignListener(), plugin);
		pm.registerEvents(new DeathSignListener(), plugin);
		pm.registerEvents(new FreezeListener(), plugin);
		pm.registerEvents(new RankReceivedListener(), plugin);
		pm.registerEvents(new SpecialInventoryListener(), plugin);
		pm.registerEvents(new DeathListener(), plugin);
		pm.registerEvents(new InvseeListener(), plugin);
		pm.registerEvents(new HorseProtectListener(), plugin);
		pm.registerEvents(new PunishmentListener(), plugin);
		pm.registerEvents(new FixesListener(), plugin);
		pm.registerEvents(new BlockHitFixListener(), plugin);
		pm.registerEvents(new SignColorsListener(), plugin);
		pm.registerEvents(new DisguiseListener(), plugin);
	}

	public WorldBorderListener getWorldBorderListener() {
		return worldBorderListener;
	}

}
