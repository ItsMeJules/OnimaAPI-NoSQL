package net.onima.onimaapi.gui.menu;

import java.util.List;

import org.bukkit.scheduler.BukkitTask;

import net.onima.onimaapi.crates.prizes.Prize;
import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.players.APIPlayer;

public abstract class CrateOpeningMenu extends PacketMenu {
	
	protected boolean rolling, dropped;
	protected List<Prize> prizes;
	protected BukkitTask task;
	
	public CrateOpeningMenu(String crateName, List<Prize> prizes, int size) {
		super("opening_menu_" + crateName, "§bOuverture...", size, false);
		
		this.prizes = prizes;
	}
	
	public abstract void closeCrate(APIPlayer player);
	public abstract void renderInventory(APIPlayer player, List<Prize> prizes);
	public abstract void renderPrizes(List<Prize> prizes);
	public abstract void startRoll(APIPlayer player);
	
	public boolean isRolling() {
		return rolling;
	}
	
	public boolean arePrizesDropped() {
		return dropped;
	}
	
	public List<Prize> getPrizes() {
		return prizes;
	}
	
	public BukkitTask getTask() {
		return task;
	}

}
