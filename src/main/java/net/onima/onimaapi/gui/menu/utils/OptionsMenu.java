package net.onima.onimaapi.gui.menu.utils;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import net.onima.onimaapi.gui.PacketMenu;
import net.onima.onimaapi.gui.buttons.utils.Button;
import net.onima.onimaapi.gui.buttons.utils.NamedButton;
import net.onima.onimaapi.gui.buttons.utils.UpdatableButton;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.players.utils.PlayerOption;
import net.onima.onimaapi.utils.BetterItem;
import net.onima.onimaapi.utils.Options;
import net.onima.onimaapi.utils.callbacks.APICallback;

public abstract class OptionsMenu extends PacketMenu {

	protected static Button noPerm;
	
	static {
		noPerm = new Button() {
			
			@Override
			public BetterItem getButtonItem(Player player) {
				return new BetterItem(Material.STAINED_GLASS_PANE, 1, 1, "§c§oPermission manquante", "§cVous n'avez pas la permission", "§cd'afficher cet item...");
			}
			
			@Override
			public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
				event.setCancelled(true);
			}
		};
	}
	
	protected APIPlayer apiPlayer;
	protected Options options;
	
	public OptionsMenu(String id, String title, int size, APIPlayer apiPlayer) {
		super(id, title, size, false);
		
		this.apiPlayer = apiPlayer;
		options = apiPlayer.getOptions();
	}

	public class OptionsButton extends NamedButton implements UpdatableButton {
		
		private PlayerOption option;
		private Object object;
		
		public OptionsButton(PlayerOption option) {
			super(null);
			
			this.option = option;
			object = options.get(option);
		}
		
		@Override
		public BetterItem getButtonItem(Player player) {
			if (option.permission() != null && !option.permission().has(player))
				return noPerm.getButtonItem(player);
			
			Dye dye = new Dye();
			
			if (object instanceof Boolean) {
				boolean bool = (boolean) object;
				name = option.display().replace("%value%", (bool ? "§a" : "§c") + bool);
				
				dye.setColor(bool ? DyeColor.LIME : DyeColor.GRAY);
			} else if (object instanceof Number) {
				name = option.display().replace("%value%", String.valueOf(object));
				
				dye.setColor(DyeColor.CYAN);
			}
			
			return new BetterItem(dye.toItemStack(), name, lore);
		}

		@Override
		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
			event.setCancelled(true);
			
			if (object instanceof Boolean) {
				boolean result = options.reverseBoolean(option);
				object = result;
				
				if (option == PlayerOption.ModOptions.PICKUP_ITEM) {
					System.out.println(clicker.getCanPickupItems());
					clicker.setCanPickupItems(!result);
				}
			}
			
			update(menu);
		}
		
		@Override
		public void update(PacketMenu menu) {
			if (object instanceof Boolean) {
				menu.getInventory().setItem(slot(), getButtonItem(null).toItemStack());
				return;
			}
				
			final UpdaterMenu updater = object instanceof Integer ? new IntegerUpdaterMenu(title, OptionsMenu.this) : new DoubleUpdaterMenu(title, OptionsMenu.this);
			
			updater.toUpdate = this;
			updater.callback = new APICallback<Number>() {
				
				@Override
				public boolean call(Number value) {
					if (value instanceof Integer)
						options.getSettings().put(option, object = (((Number) options.get(option)).intValue() + value.intValue()));
					else 
						options.getSettings().put(option, object = (((Number) options.get(option)).floatValue() + value.floatValue()));
					
					updater.getInventory().setItem(updater.toUpdateSlot, getButtonItem(null).toItemStack());
					return true;
				}
			};
			
			updater.open(apiPlayer);
		}
		
		public int slot() {
			return option.slot();
		}
		
		public PlayerOption getOption() {
			return option;
		}
	}
	
}