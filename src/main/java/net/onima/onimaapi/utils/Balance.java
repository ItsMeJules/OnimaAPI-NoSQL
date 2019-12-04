package net.onima.onimaapi.utils;

import java.util.List;

import org.bson.Document;

import com.google.common.collect.Lists;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;

import net.onima.onimaapi.mongo.OnimaMongo;
import net.onima.onimaapi.mongo.OnimaMongo.OnimaCollection;
import net.onima.onimaapi.saver.mongo.MongoSerializer;
import net.onima.onimaapi.utils.callbacks.VoidCallback;

public class Balance implements MongoSerializer {
	
	private double amount;
	private boolean ban;
	
	public Balance(double amount) {
		this.amount = amount;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public double setAmount(double amount) {
		return this.amount = amount;
	}
	
	public double removeAmount(double amount) {
		return this.amount -= amount;
	}
	
	public double addAmount(double amount) {
		return this.amount += amount;
	}
	
	public boolean isBanned() {
		return ban;
	}
	
	public void setBan(boolean ban) {
		this.ban = ban;
	}
	
	@Override
	public Document getDocument(Object... objects) {
		return new Document("amount", amount).append("banned", ban);
	}
	
	public static void getHighestBalances(VoidCallback<List<Document>> callback, int size) {
		OnimaMongo.executeAsync(OnimaCollection.PLAYERS, (collection) -> {
			callback.call(Lists.newArrayList(collection.find()
					.sort(Sorts.ascending("balance.amount"))
					.projection(Projections.include("uuid", "balance.amount"))
					.limit(size).iterator()));
		}, true);
	}
	
}
