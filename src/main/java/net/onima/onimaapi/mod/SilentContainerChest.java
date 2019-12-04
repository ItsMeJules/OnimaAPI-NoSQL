package net.onima.onimaapi.mod;

import net.minecraft.server.v1_7_R4.ContainerChest;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.IInventory;

public class SilentContainerChest extends ContainerChest {

	public IInventory inventory;
	  
	public SilentContainerChest(IInventory inventory1, IInventory inventory2) {
		super(inventory1, inventory2);
		
		this.inventory = inventory2;
		this.inventory.closeContainer();
	}

	@Override
	public void b(EntityHuman entityhuman) {}

}
