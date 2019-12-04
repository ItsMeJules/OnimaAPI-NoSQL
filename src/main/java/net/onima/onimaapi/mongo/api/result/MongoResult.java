package net.onima.onimaapi.mongo.api.result;

public class MongoResult {
	
	private boolean failed;
	
	public void setFailed(boolean failed) {
		this.failed = failed;
	}
	
	public boolean isFailed() {
		return failed;
	}

}
