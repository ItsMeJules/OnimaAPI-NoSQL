package net.onima.onimaapi.zone.type.utils;

/**
 * This interface represents an area where you can apply deathban and DTR loss.
 */
public interface Deathbannable {
	
	/**
	 * This method checks is this area is deathbannable.
	 * 
	 * @return true if the area is deathbannable.<br>
	 * false if the area is not deathbannable.
	 */
	public boolean isDeathbannable();
	
	/**
	 * This method sets if this area should be deathbannable.
	 * 
	 * @param deathban - Whether yes or not the area is deathbannable.
	 */
	public void setDeathban(boolean deathban);
	
	/**
	 * This method sets if this area should have the DTR loss enabled.
	 * 
	 * @param dtrLoss - Whether yes or not you can lose DTR on this area.
	 */
	public void setDTRLoss(boolean dtrLoss);
	
	/**
	 * This method checks if this area has the DTR loss.
	 * 
	 * @return true if the area has DTR loss.<br>
	 * false if the area has no DTR loss.
	 */
	public boolean hasDTRLoss();
	
	/**
	 * This method returns the deathban multiplier for an area. For example if you have a deathban of 30 minutes and
	 * you loose 1 DTR. If this area has a multiplier of 2, you'll be banned for 1 hour and will loose 2 DTR.
	 * 
	 * @return The deathban multiplier.
	 */
	public double getDeathbanMultiplier();
	
	/**
	 * This method sets the death ban multiplier and the DTR loss multiplier for the area.
	 * 
	 * @param multiplier - Sets the multiplier of this area.
	 */
	public void setDeathbanMultiplier(double multiplier);

}
