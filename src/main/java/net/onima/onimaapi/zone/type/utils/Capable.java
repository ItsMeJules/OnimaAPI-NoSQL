package net.onima.onimaapi.zone.type.utils;

import net.onima.onimaapi.players.APIPlayer;
import net.onima.onimaapi.zone.type.Region;

public interface Capable {
	
	public long getCapTime();
	public void setCapTime(long capTime);
	public Region getCapZone();
	public void setCapZone(Region capZone);
	public long getCapTimeLeft();
	public void setCapTimeLeft(long capTimeLeft);
	public long getTimeAtCap();
	public void setTimeAtCap(long timeAtCap);
	public void decreaseTime();
	public APIPlayer getCapper();
	public void setCapper(APIPlayer capper);
	public boolean isCapped();
	public boolean tryCapping(APIPlayer capper);
	public void onCap(APIPlayer capper);
	public void onKnock(APIPlayer capper, APIPlayer knocker);

}
