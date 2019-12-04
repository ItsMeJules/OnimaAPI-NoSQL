package net.onima.onimaapi.gui.menu;

public class PlayerInventoryMenu /*extends PacketMenu*/ { //TODO
	
//	private Player player;
//	private SpecialPlayerInventory editableInventory;
//	
//	public PlayerInventoryMenu(Player player) {
//		super("inventory", "§c§lInspection §7: §8" + player.getName(), MAX_SIZE, false);
//	
//		this.player = player;
//		permission = OnimaPerm.ONIMAAPI_COMMAND_OPENINV;
//	}
//
//	@Override
//	public void registerItems() {
//		PlayerInventory inventory = player.getInventory();
//		ItemStack[] contents = new ItemStack[size];
//		
//        System.arraycopy(inventory.getArmorContents(), 0, contents, 0, inventory.getArmorContents().length);
//        System.arraycopy(inventory.getContents(), 0, contents, 9, inventory.getContents().length);
//
//        buttons.put(12, new EditInventoryButton());
//        
//	    for (int i = 0; i < contents.length; i++) {
//	    	ItemStack item = contents[i];
//	    	
//	    	if (item == null)
//	    		continue;
//	    	
//	    	buttons.put(i, new DisplayButton(item));
//	    }
// 	}
//	
//	public class PunishmentButton implements Button {
//
//		@Override
//		public BetterItem getButtonItem(Player player) {
//			return new BetterItem(Material.ANVIL, 1, 0, "§c§lPunitions");
//		}
//
//		@Override
//		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
//			event.setCancelled(true);
//			
//			if (player == null || !player.isOnline()) {
//				close(APIPlayer.getByPlayer(clicker), true);
//				clicker.sendMessage("§cLe joueur n'est plus en ligne !");
//				return;
//			}
//			
//			Bukkit.dispatchCommand(clicker, "punishments " + player.getName());
//		}
//		
//	}
//	
//	public class ClearButton implements Button {
//
//		@Override
//		public BetterItem getButtonItem(Player player) {
//			return new BetterItem(Material.BOOK, 1, 0, "§6Clear l'inventaire");
//		}
//
//		@Override
//		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
//			event.setCancelled(true);
//			
//			APIPlayer apiPlayer = APIPlayer.getByPlayer(clicker);
//			
//			if (player == null || !player.isOnline()) {
//				close(apiPlayer, true);
//				clicker.sendMessage("§cLe joueur n'est plus en ligne !");
//				return;
//			}
//			
//			APICallback<Boolean> callback = (bool) -> {
//				if (bool) {
//					player.getInventory().setContents(new ItemStack[36]);
//					player.getInventory().setArmorContents(new ItemStack[4]);
//				}
//				
//				PlayerInventoryMenu.this.open(apiPlayer);
//				return true;
//			};
//			
//			Button button = new DisplayButton(new BetterItem(Material.IRON_FENCE, 1, 0, "§r"));
//		
//			new ConfirmationMenu("§cClear §l" + player.getName(), callback, false, new Button[] {button, button, button, button}).open(apiPlayer);
//		}
//		
//	}
//	
//	public class InventoryItemButton implements Button {
//
//		private ItemStack item;
//
//		public InventoryItemButton(ItemStack item) {
//			this.item = item;
//		}
//		
//		@Override
//		public BetterItem getButtonItem(Player player) {
//			return new BetterItem(Material.BOOK_AND_QUILL, 1, 0, "§9Editer l'inventaire.");
//		}
//		
//		public ItemStack getItem() {
//			return item;
//		}
//
//		@Override
//		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
//			
//		}
//		
//	}
//	
//	public class EditInventoryButton implements Button {
//
//		@Override
//		public BetterItem getButtonItem(Player player) {
//			return null;
//		}
//
//		@Override
//		public void click(PacketMenu menu, Player clicker, ItemStack current, InventoryClickEvent event) {
////			new SpecialPlayerInventory(player, player.isOnline()).open(clicker);
//		}
//		
//	}
//	
//	public static int getReversedItemSlotNum(int i) {
//		return i - 18;
//	}
//  
//	public static int getReversedArmorSlotNum(int i) {
//		if (i == 0)
//			return 3;
//		if (i == 1)
//			return 2;
//		if (i == 2)
//			return 1;
//		if (i == 3)
//			return 0;
//		
//		return i;
//	}
	
//	private static ItemStack noArmor, separator, punishments, clear, teleport;
//	
//	static {
//		clear = new BetterItem(Material.BOOK, 1, 0, "§6Clear l'inventaire").toItemStack();
//		punishments = new BetterItem(Material.ANVIL, 1, 0, "§c§lPunitions").toItemStack();
//		separator = new BetterItem(Material.STAINED_GLASS_PANE, 1, 7, " ").toItemStack();
//		noArmor = new BetterItem(Material.STAINED_GLASS_PANE, 1, 14, " ").toItemStack();
//		teleport = new BetterItem(Material.PORTAL, 1, 0, "§6Se téléporter").toItemStack();
//	}
//	
//	public PlayerInventoryMenu(Player player) {
//		super(MAX_SIZE, "inventory", "§c§lInspection §7: §8" + player.getName());
//
//		permission = OnimaPerm.ONIMAAPI_COMMAND_OPENINV;
//		PlayerInventory playerInventory = player.getInventory();
//		currentPage = mainPage = new MenuPage(this) {
//			
//			@Override
//			public void registerItems() {
//				inventory.setItem(0, separator);
//				inventory.setItem(1, separator);
//				inventory.setItem(2, playerInventory.getHelmet() == null ? noArmor : playerInventory.getHelmet());
//				inventory.setItem(3, playerInventory.getChestplate() == null ? noArmor : playerInventory.getChestplate());
//				inventory.setItem(4, playerInventory.getLeggings() == null ? noArmor : playerInventory.getLeggings());
//				inventory.setItem(5, playerInventory.getBoots() == null ? noArmor : playerInventory.getBoots());
//				inventory.setItem(6, separator);
//				inventory.setItem(7, separator);
//				inventory.setItem(8, punishments);
//				
//				for (int i = 0; i < playerInventory.getSize(); i++) {
//					ItemStack item = playerInventory.getItem(i);
//					
//					if (item != null) {
//						if (i < 9)
//							inventory.setItem(i + 9 * 4, item);
//						else
//							inventory.setItem(i, item);
//					}
//				}
//				
//				inventory.setItem(45, separator);
//				inventory.setItem(46, separator);
//				
//				double health = ((Damageable) player).getHealth();
//				
//				inventory.setItem(47, new BetterItem(Material.SPECKLED_MELON, health > 64 ? 64 : (int) health, 0, "§6PV §7: §a" + health + "§7/§6" + (int) ((Damageable) player).getMaxHealth()).toItemStack());
//				inventory.setItem(48, new BetterItem(Material.COOKED_BEEF, player.getFoodLevel() > 64 ? 64 : (int) player.getFoodLevel(), 0, "§6Nourriture §7: §a" + player.getFoodLevel()).toItemStack());
//				inventory.setItem(49, new BetterItem(Material.BREWING_STAND_ITEM, player.getActivePotionEffects().size(), 0, "§6Effets de potions §7:", Methods.setEffectsAsInInventory(player.getActivePotionEffects(), "§f", "§7")).toItemStack());
//				inventory.setItem(50, clear);
//				inventory.setItem(51, teleport);
//				inventory.setItem(52, separator);
//				inventory.setItem(53, separator);
//			}
//			
//			@Override
//			public MenuPage next() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			@Override
//			public void click(ItemStack item, Player player, InventoryClickEvent event) {
//				event.setCancelled(true);
//				
//				APIPlayer apiPlayer = APIPlayer.getByPlayer(player);
//				Player target = apiPlayer.getExaminating();
//				
//				if (target == null) {
//					apiPlayer.closeMenu(true);
//					player.sendMessage("§cLe joueur n'est plus en ligne !");
//					return;
//				}
//				
//				switch (event.getSlot()) {
//				case 8:
//					Bukkit.dispatchCommand(player, "punishments " + target.getName());
//					break;
//				case 50:
//					Bukkit.dispatchCommand(player, "clear " + target.getName());
//					break;
//				case 51:
//					Bukkit.dispatchCommand(player, "tp " + target.getName());
//					break;
//				default:
//					break;
//				}
//			}
//		};
//		
//		mainPage.registerItems();
//	}
	
}
