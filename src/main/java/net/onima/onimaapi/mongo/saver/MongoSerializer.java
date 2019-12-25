package net.onima.onimaapi.mongo.saver;

import org.bson.Document;

public interface MongoSerializer {

	/**
	 * This method allows the actual class to serialize into a document.
	 * @param objects - Objects that might be useful for the document creation.
	 */
	public Document getDocument(Object... objects);
	
}
