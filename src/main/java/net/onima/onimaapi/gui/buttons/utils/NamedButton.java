package net.onima.onimaapi.gui.buttons.utils;

import java.util.Arrays;
import java.util.List;

public abstract class NamedButton implements Button {

	protected String name;
	protected List<String> lore;
	
	public NamedButton(String name, List<String> lore) {
		this.name = name;
		this.lore = lore;
	}
	
	public NamedButton(String name, String... lore) {
		this.name = name;
		
		if (lore.length != 0)
			this.lore = Arrays.asList(lore);
	}

	public String getName() {
		return name;
	}

	public List<String> getLore() {
		return lore;
	}
	
}
