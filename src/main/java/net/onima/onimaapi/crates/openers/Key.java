package net.onima.onimaapi.crates.openers;

import net.onima.onimaapi.crates.booster.KeyBooster;
import net.onima.onimaapi.crates.utils.Crate;
import net.onima.onimaapi.players.APIPlayer;

public abstract class Key {

	protected String crateName;
	protected KeyBooster booster;

	public Key(String crateName, KeyBooster booster) {
		this.crateName = crateName;
		this.booster = booster;
	}
	
	public abstract void give(APIPlayer apiPlayer, boolean sendMessage);
	
	public boolean match(Crate crate) {
		return crateName.equalsIgnoreCase(crate.getName()) || crateName.equalsIgnoreCase(crate.getDisplayName());
	}

	public String getCrateName() {
		return crateName;
	}
	
	public KeyBooster getBooster() {
		return booster;
	}
	
	public void setBooster(KeyBooster booster) {
		this.booster = booster;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((booster == null) ? 0 : booster.hashCode());
		result = prime * result + ((crateName == null) ? 0 : crateName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Key other = (Key) obj;
		if (booster == null) {
			if (other.booster != null)
				return false;
		} else if (!booster.equals(other.booster))
			return false;
		if (crateName == null) {
			if (other.crateName != null)
				return false;
		} else if (!crateName.equals(other.crateName))
			return false;
		return true;
	}
	
}
