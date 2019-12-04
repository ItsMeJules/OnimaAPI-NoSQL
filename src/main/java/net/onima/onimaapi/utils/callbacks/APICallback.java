package net.onima.onimaapi.utils.callbacks;

public interface APICallback<T> {

	public boolean call(T t);
	
}
