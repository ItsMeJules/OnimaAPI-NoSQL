package net.onima.onimaapi.utils.callbacks;

import net.onima.onimaapi.mongo.api.AsyncExecutor;

public interface AsyncVoidCallback<T> {

	public boolean call(T t, AsyncExecutor executor);
	
}
