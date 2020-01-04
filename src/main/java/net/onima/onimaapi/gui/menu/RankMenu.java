package net.onima.onimaapi.gui.menu;

import java.util.Map.Entry;

import org.bukkit.Material;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.PacketStaticMenu;
import net.onima.onimaapi.gui.buttons.DisplayButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.rank.RankType;
import net.onima.onimaapi.utils.BetterItem;

public class RankMenu extends PacketMenu implements PacketStaticMenu {

	public RankMenu() {
		super("ranks", "§6Ranks du serveur.", MAX_SIZE - 9, true);
	}

	@Override
	public void registerItems() {//TODO ajouter les descriptions
		BetterItem defaultRank = new BetterItem(Material.WOOL, 1, 0, RankType.DEFAULT.getName());
		BetterItem ninjaRank = new BetterItem(Material.WOOL, 1, 8, RankType.NINJA.getName());
		BetterItem roninRank = new BetterItem(Material.WOOL, 1, 9, RankType.RONIN.getName());
		BetterItem komonoRank = new BetterItem(Material.WOOL, 1, 13, RankType.KOMONO.getName());
		BetterItem kachiRank = new BetterItem(Material.WOOL, 1, 1, RankType.KACHI.getName());
		BetterItem shogunRank = new BetterItem(Material.WOOL, 1, 10, RankType.SHOGUN.getName());
		BetterItem youtubeRank = new BetterItem(Material.WOOL, 1, 14, RankType.YOUTUBE.getName());
		BetterItem famousRank = new BetterItem(Material.WOOL, 1, 6, RankType.FAMOUS.getName());
		
		buttons.put(1, new DisplayButton(defaultRank));
		buttons.put(4, new DisplayButton(ninjaRank));
		buttons.put(7, new DisplayButton(roninRank));
		buttons.put(19, new DisplayButton(komonoRank));
		buttons.put(22, new DisplayButton(kachiRank));
		buttons.put(25, new DisplayButton(shogunRank));
		buttons.put(39, new DisplayButton(youtubeRank));
		buttons.put(41, new DisplayButton(famousRank));
	}

	@Override
	public void setup() {
		inventory.clear();
		for (Entry<Integer, Button> entry : buttons.entrySet())
			inventory.setItem(entry.getKey(), createItemStack(null, entry.getValue()));
	}

}
