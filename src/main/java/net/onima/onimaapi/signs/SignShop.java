package net.onima.onimaapi.signs;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import net.onima.onimaapi.items.Crowbar;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.utils.ConfigurationService;
import net.onima.onimaapi.utils.Methods;
import net.onima.onimaapi.utils.time.Time;

public class SignShop extends HCFSign { //TODO Custom buy : avec un item dans la main le pd clique dessus (le panneau) et le panneau enregistre le data de l'item

	private static boolean refreshed;
	
	private boolean buySign;
	private int matID, damage, stackSize;
	private double price;
	private ItemStack item;
	
	public SignShop(Sign sign, boolean buySign) {
		super(sign);
		this.buySign = buySign;
	}

	public boolean isBuySign() {
		return buySign;
	}

	public void setBuySign(boolean buySign) {
		this.buySign = buySign;
	}

	public int getMaterialtID() {
		return matID;
	}

	@SuppressWarnings("deprecation")
	public void setMaterial(int matID, int damage) {
		this.matID = matID;
		this.damage = damage;
		
		if (matID != Crowbar.getID())
			this.item = new ItemStack(matID, stackSize, (short) damage);
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getStackSize() {
		return stackSize;
	}

	public void setStackSize(int stackSize) {
		this.stackSize = stackSize;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}
	
	public boolean isSellingCrowbar() {
		return matID == Crowbar.getID();
	}
	
	public void updateLines(boolean runnable) {
		if (runnable)
			Bukkit.getScheduler().runTask(onimaAPI, () -> updateLines());
		else
			updateLines();
	}
	
	public void updateLines() {
		sign.setLine(0, buySign ? ConfigurationService.SHOP_SIGN_SELL_LINE : ConfigurationService.SHOP_SIGN_BUY_LINE);
		sign.setLine(1, matID == Crowbar.getID() ? "Crowbar" : Methods.getItemName(item));
		sign.setLine(2, String.valueOf(stackSize));
		sign.setLine(3, String.valueOf(price) + ConfigurationService.MONEY_SYMBOL);
		sign.update();
	}
	
	public void displayLines(APIPlayer player, Double price, Integer amount, DisplayLines lines) {
		player.sendSignChange(sign, lines.updatePlaceholders(player, amount, price, Methods.getItemName(item)).get().toArray(new String[4]), 4 * Time.SECOND, true);
	}
	
	public void displayLines(APIPlayer player, DisplayLines lines) {
		displayLines(player, (double) 0, (int) 0, lines);
	}
	
	public void reset(APIPlayer player) {
		player.resetSign(sign);
	}

	@Override
	public void serialize() {
		if (!refreshed)
			refreshFile();
		
		String path = "shop-signs." + Methods.serializeLocation(sign.getLocation(), false) + ".";
		
		config.set(path+"buy-sign", buySign);
		config.set(path+"material-id", matID);
		config.set(path+"material-damage", damage);
		config.set(path+"stack-size", stackSize);
		config.set(path+"price", price);
	}

	@Override
	public void refreshFile() {
		ConfigurationSection section = config.getConfigurationSection("shop-signs");
		
		if (section != null) {
			List<Location> shopLoc = signs.stream().filter(sign -> sign instanceof SignShop).map(hcfSign -> {
				return hcfSign.getSign().getLocation();
			}).collect(Collectors.toList());
			
			for (String stringLoc : section.getKeys(false)) {
				if (!shopLoc.contains(Methods.deserializeLocation(stringLoc, false)))
					signSerialConfig.remove("shop-signs."+stringLoc, false);
			}
		}
		
		refreshed = true;
	}
	
	public static enum DisplayLines {
		
		ITEMS_SOLD(ConfigurationService.SHOP_SIGN_SELL_LINES) {
			private List<String> lines;
			
			@Override
			DisplayLines updatePlaceholders(APIPlayer player, int amount, double price, String itemName) {
				lines = Methods.replacePlaceholder(super.lines, "%amount%", amount, "%price%", price, "%balance%", player.getBalance().getAmount());
				return this;
			}
			
			@Override
			List<String> get() {
				return lines;
			}
		},
		
		ITEMS_BOUGHT(ConfigurationService.SHOP_SIGN_BUY_LINES) {
			private List<String> lines;
			
			@Override
			DisplayLines updatePlaceholders(APIPlayer player, int amount, double price, String itemName) {
				lines = Methods.replacePlaceholder(super.lines, "%amount%", amount, "%price%", price, "%balance%", player.getBalance().getAmount());
				return this;
			}	
			
			@Override
			List<String> get() {
				return lines;
			}
		},
		
		ITEMS_MISSING(ConfigurationService.SHOP_SIGN_ITEM_MISSING_LINES) {
			private List<String> lines;
			
			@Override
			DisplayLines updatePlaceholders(APIPlayer player, int amount, double price, String itemName) {
				lines = Methods.replacePlaceholder(super.lines, "%id%", itemName);
				return this;
			}
			
			@Override
			List<String> get() {
				return lines;
			}
		},
		
		BALANCE_BANNED(ConfigurationService.SHOP_SIGN_BALANCE_BANNED_LINES) {
			@Override
			DisplayLines updatePlaceholders(APIPlayer player, int amount, double price, String itemName) {
				return this;
			}
			
			@Override
			List<String> get() {
				return lines;
			}
		},
		
		NOT_ENOUGH_MONEY(ConfigurationService.SHOP_SIGN_NOT_ENOUGH_MONEY_LINES) {
			private List<String> lines;
			
			@Override
			DisplayLines updatePlaceholders(APIPlayer player, int amount, double price, String itemName) {
				lines = Methods.replacePlaceholder(super.lines, "%balance%", (price - player.getBalance().getAmount()));
				return this;
			}
			
			@Override
			List<String> get() {
				return lines;
			}
		};
		
		List<String> lines;
		
		private DisplayLines(List<String> lines) {
			this.lines = lines;
		}
		
		abstract DisplayLines updatePlaceholders(APIPlayer player, int amount, double price, String itemName);
		abstract List<String> get();
	}
	
}
