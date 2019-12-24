package net.onima.onimaapi.players.utils;

import net.onima.onimaapi.rank.OnimaPerm;

public interface PlayerOption {
	
	String name();
	int slot();
	OnimaPerm permission();
	String display();
	
	enum ModOptions implements PlayerOption {
		
		/**
		 * Storing true or false to know if the player can pickup items.
		 */
		PICKUP_ITEM(0, null, "§7Ramasser les items : %value%"),
		
		/**
		 * Storing true or false to know if the player can drop items.
		 */
		DROP_ITEM(1, null, "§7Drop d'items : %value%"),
		
		/**
		 * Storing true or false to know if the player can break blocks.
		 */
		BREAK_BLOCK(2, null, "§7Casser les blocks : %value%"),
		
		/**
		 * Storing true or false to know if the player can place blocks.
		 */
		PLACE_BLOCK(3, null, "§7Placer des blocks : %value%"),
		
		/**
		 * Storing true or false to know if the player can send chat messages.
		 */
		CHAT_MESSAGE(4, null, "§7Parler dans le chat : %value%"),
		
		/**
		 * Storing true or false to know if the player can attack another player.
		 */
		ATTACK_PLAYER(5, null, "§7Dégâts sur les joueurs : %value%"),
		
		/**
		 * Storing an Integer to know under which layer the player can teleport for miners.
		 */
		TELEPORT_LAYER(6, null, "§7Couche de tp pour les mineurs : §c%value%"),
		
		/**
		 * Storing a Double which is the multiplier for self boosting.
		 */
		PLAYER_BOOSTER(7, null, "§7Multiplicateur de boost sur soi : §c%value%"),
		
		/**
		 * Storing a Double which is the multiplier for entities boosting.
		 */
		ENTITY_BOOSTER(8, null, "§7Multiplicateur de boost sur les entités : §c%value%"),
		
		/**
		 * Storing an Integer which is the time before a player clicked reappears.
		 */
		HIDE_PLAYER_TIME(9, null, "§7Temps (en s) pour cacher un joueur cliqué : §c%value%"),
		
		/**
		 * Storing true or false to know if the player can open chests silently.
		 */
		SILENT_CHEST(10, null, "§7Ouvrir silencieusement les coffres : %value%");
		
		private int slot;
		private OnimaPerm permission;
		private String menuDisplay;
		
		private ModOptions(int slot, OnimaPerm permission, String menuDisplay) {
			this.slot = slot;
			this.permission = permission;
			this.menuDisplay = menuDisplay;
		}

		@Override
		public int slot() {
			return slot;
		}
		
		@Override
		public OnimaPerm permission() {
			return permission;
		}
		
		@Override
		public String display() {
			return menuDisplay;
		}
		
		public static ModOptions fromSlot(int slot) {
			for (ModOptions option : values()) {
				if (option.slot() == slot)
					return option;
			}
			
			return null;
		}
 		
	}
	
	enum GlobalOptions implements PlayerOption {
		
		/**
		 * If the player can receive private messages.
		 */
		PRIVATE_MESSAGE(0, null, "§7Messages privés : %value%"),
		
		/**
		 * If the player spies the private messages.
		 */
		SOCIAL_SPY(1, OnimaPerm.ONIMAAPI_COMMAND_SOCIALSPY, "§7Social spy : %value%"),
		
		/**
		 * Every kind of sound heard by the player coming from the plugin.
		 */
		SOUNDS(2, null, "§7Sons (plugins) : %value%"),
		
		/**
		 * If the cobble block broken should be dropped.
		 */
		COBBLE_DROP(3, null, "§7Drop de cobblestone : %value%"),
		
		/**
		 * If the player should receive death messages.
		 */
		DEATH_MESSAGES(4, null, "§7Messages de mort : %value%"),
		
		/**
		 * If the player should receive the FD messages.
		 */
		FOUND_DIAMONDS(5, null, "§7Alertes '§f[FD]§7' : %value%"),
		
		/**
		 * Whether the player should be alerted when 2 players with the same ip connects. 
		 */
		ALTS_CONNECT_MESSAGE(6, OnimaPerm.ALTS_COMMAND, "§7Notifier quand un joueur rejoint et à un alt en ligne : %value%"),
		
		/**
		 * Whether the player should be alerted when a player with an important note joins.
		 */
		IMPORTANT_NOTE_NOTIFY_CONNECT(7, OnimaPerm.ONIMAAPI_NOTE_COMMAND, "§7Notifier les notes importantes quand un joueur rejoint : %value%"),
		
		/**
		 * Whether the player can see or not other players in spawn while himself in spawn.
		 */
		SHOW_PLAYERS_WHEN_IN_SPAWN(8, null, "§7Montrer les joueurs dans le spawn : %value%"),
		
		/**
		 * Whether the player can see or not the invisible players.
		 */
		SHOW_INVISIBLE_PLAYERS(9, OnimaPerm.ONIMAAPI_SEE_INVISIBLE, "§7Afficher les joueurs invisible : %value%");
		
		private int slot;
		private OnimaPerm permission;
		private String menuDisplay;
		
		private GlobalOptions(int slot, OnimaPerm permission, String menuDisplay) {
			this.slot = slot;
			this.permission = permission;
			this.menuDisplay = menuDisplay;
		}
		
		@Override
		public int slot() {
			return slot;
		}
		
		@Override
		public OnimaPerm permission() {
			return permission;
		}
		
		public static GlobalOptions fromSlot(int slot) {
			for (GlobalOptions option : values()) {
				if (option.slot() == slot)
					return option;
			}
			
			return null;
		}
		
		@Override
		public String display() {
			return menuDisplay;
		}
		
	}

}
