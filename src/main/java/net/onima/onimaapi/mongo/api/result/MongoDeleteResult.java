package net.onima.onimaapi.mongo.api.result;

public class MongoDeleteResult extends MongoResult {
	
	private long documentsDeleted;
	
	public long getDocumentsDeleted() {
		return documentsDeleted;
	}

	public void setDocumentsDeleted(long documentsDeleted) {
		this.documentsDeleted = documentsDeleted;
	}

}
