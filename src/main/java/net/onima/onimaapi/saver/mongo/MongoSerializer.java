package net.onima.onimaapi.saver.mongo;

import org.bson.Document;

public interface MongoSerializer {

	/**
	 * This method allows the actual class to serialize into a document.
	 * @param objects - Objects that might be useful for the creation of the document.
	 */
	public Document getDocument(Object... objects);
	
}
