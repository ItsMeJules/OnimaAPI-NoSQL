package net.onima.onimaapi.gui.menu;

import java.util.Map.Entry;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.PacketStaticMenu;
import net.onima.onimaapi.gui.buttons.TeleportHeadButton;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.rank.OnimaPerm;

public class OnlineStaffMenu extends PacketMenu implements PacketStaticMenu {

	public OnlineStaffMenu() {
		super("online_staff", "§cStaff en ligne", MAX_SIZE, true);
		
		permission = OnimaPerm.ONIMAAPI_GUI_STAFF_MENU;
	}

	@Override
	public void registerItems() {
		APIPlayer.getOnlineAPIPlayers().parallelStream()
				.filter(online -> OnimaPerm.STAFF_COUNTER_COUNT.has(online.toPlayer()))
				.sorted((x, y) -> -Integer.compare(x.getRank().getRankType().getValue(), y.getRank().getRankType().getValue()))
				.forEach(staff -> buttons.put(buttons.size(), new TeleportHeadButton(staff)));
	}

	@Override
	public void setup() {
		inventory.clear();
		for (Entry<Integer, Button> entry : buttons.entrySet())
			inventory.setItem(entry.getKey(), createItemStack(null, entry.getValue()));
	}

}
