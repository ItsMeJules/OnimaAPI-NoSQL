package net.onima.onimaapi.zone.struct;

/**
 * It's only for the class that implements {@link net.onima.onimaapi.zone.type.utils.FlagZone}
 */
public enum Flag {
	
	PVP_TIMER_DENY_ENTRY("pvp-timer-deny-entry"),
	DENY_ENDERPEARL("deny-enderpearl"),
	NEED_PERMISSION_TO_ENTER("need-permission-to-enter"),
	COMBAT_TAG_DENY_ENTRY("combat-tag-deny-entry"),
	PVP_TIMER_PAUSE("pvp-timer-pause"),
	SAFE_DISCONNECT("safe-disconnect"),
	COMBAT_SAFE("combat-safe"),
	NO_LEAVING_MESSAGE("no-leaving-message"),
	NO_ENTERING_MESSAGE("no-entering-message"),
	NO_BARDING("no-barding"),
	BREAK_BLOCK("break-block"),
	PLACE_BLOCK("place-block"),
	ITEM_DROP("item-drop"),
	PICKUP_ITEM("pickup-item"),
	NO_INTERACT("no-interact"),
	NATURAL_PROTECT("natural-protect"),
	LIQUID_FLOW("liquid-flow");
//	NEED_PERMISSION_TO_LEAVE("need-permission-to-leave");
	
	private String name;
	
	private Flag(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static Flag fromName(String string) {
		for (Flag flag : Flag.values()) {
			if (flag.getName().equalsIgnoreCase(string))
				return flag;
		}
		return null;
	}

}
