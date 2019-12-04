package net.onima.onimaapi.mongo.api;

import org.bson.Document;
import org.bukkit.Bukkit;

import net.onima.onimaapi.OnimaAPI;
import net.onima.onimaapi.utils.callbacks.VoidCallback;

public class AsyncExecutor {

	private Document[] documents;
	
	public AsyncExecutor(Document... documents) {
		this.documents = documents;
	}
	
	public void sync(VoidCallback<Document[]> callback) {
		Bukkit.getScheduler().runTask(OnimaAPI.getInstance(), () -> callback.call(documents));
	}
	
}
