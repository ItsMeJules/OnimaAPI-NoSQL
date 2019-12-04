package net.onima.onimaapi.event.mongo;

import java.util.UUID;

public abstract class AbstractPlayerLoadEvent extends PlayerLoadEvent {

	public AbstractPlayerLoadEvent(UUID uuid) {
		super(uuid);
	}
	
	public abstract void done();

}
