package net.onima.onimaapi.gui.buttons.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import net.onima.onimaapi.players.OfflineAPIPlayer;
import net.onima.onimaapi.rank.RankType;
import net.onima.onimaapi.utils.BetterItem;

public abstract class HeadButton implements Button {
	
	protected OfflineAPIPlayer owner;
	protected RankType rankType;
	protected List<String> lore;
	
	{
		lore = new ArrayList<>();
	}
	
	public HeadButton(OfflineAPIPlayer owner) {
		this.owner = owner;
		
		rankType = owner.getRank().getRankType();
	}
	
	@Override
	public BetterItem getButtonItem(Player player) {
		return new BetterItem(1, owner.getRankName(true), lore, owner.getOfflinePlayer());
	}
	
	protected abstract void lore();

}
