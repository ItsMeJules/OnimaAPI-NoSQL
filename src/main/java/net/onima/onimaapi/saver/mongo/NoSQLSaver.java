package net.onima.onimaapi.saver.mongo;

import net.onima.onimaapi.mongo.api.result.MongoQueryResult;
import net.onima.onimaapi.saver.Saver;

/**
 * This class has the purpose to reuse objects by sending them to the server's database.
 */
public interface NoSQLSaver extends Saver, MongoSerializer {
	
	/**
	 * This method should be called once for each class implementing the Saver interface.
	 * @param result - The result of the query.
	 */
	public void queryDatabase(MongoQueryResult result);
	
}
