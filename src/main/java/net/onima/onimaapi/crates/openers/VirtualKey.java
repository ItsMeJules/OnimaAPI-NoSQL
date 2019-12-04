package net.onima.onimaapi.crates.openers;

import org.bson.Document;

import net.onima.onimaapi.crates.booster.KeyBooster;
import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.saver.mongo.MongoSerializer;

public class VirtualKey extends Key implements MongoSerializer {
	
	protected int amount;
	
	{
		amount = 1;
	}

	public VirtualKey(String crateName, KeyBooster booster) {
		super(crateName, booster);
	}
	
	@Override
	public void give(APIPlayer apiPlayer, boolean sendMessage) {
		apiPlayer.addVirtualKey(this);
		
		if (sendMessage)
			apiPlayer.sendMessage("§eVous §7avez reçu une clef §evirtuelle §7pour ouvrir la crate §e" + crateName + "§7.");
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void add() {
		amount++;
	}
	
	public void remove() {
		amount--;
	}
	
	@Override
	public Document getDocument(Object... objects) {
		return new Document("crate_name", crateName).append("booster", booster.asString()).append("amount", amount);
	}

}
