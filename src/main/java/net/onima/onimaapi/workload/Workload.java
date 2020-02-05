package net.onima.onimaapi.workload;

public interface Workload {
	
	public void compute();
	
	public default boolean reschedule() {
		return false;
	}

	public default boolean shouldExecute() {
		return true;
	}

}
