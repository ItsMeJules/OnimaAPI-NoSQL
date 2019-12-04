package net.onima.onimaapi.mongo.api;

import com.mongodb.MongoException;

import net.onima.onimaapi.mongo.api.result.MongoResult;
import net.onima.onimaapi.utils.callbacks.VoidCallback;

public abstract class MongoAccessor {
	
	protected VoidCallback<MongoResult> callback;
	protected MongoResult result;

	public MongoAccessor(VoidCallback<MongoResult> callback) {
		this.callback = callback;
	}

	public abstract void execute() throws MongoException;
	
	public MongoResult getResult() {
		return result;
	}
	
}
