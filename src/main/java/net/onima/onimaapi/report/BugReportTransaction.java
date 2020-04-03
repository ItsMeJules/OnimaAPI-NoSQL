package net.onima.onimaapi.report;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.onima.onimaapi.utils.Methods;

public class BugReportTransaction {
	
	private static Pattern pattern;
	
	static	{
		pattern = Pattern.compile("[0-9]+");
	}

	protected String bugDescription;
	protected String playerActionsDescription;
	protected long timeWhenBugOccured;
	protected String linkToBugProof;
	
	{
		timeWhenBugOccured = -1L;
	}
	
	public boolean submitBug(UUID reporter) {
		if (reporter == null || !hasBugDescription() || !hasPlayerActionsDescription())
			return false;
		
		BugReport report = new BugReport(reporter, bugDescription);
		report.playerActionsDescription = playerActionsDescription;
		report.timeWhenBugOccured = timeWhenBugOccured;
		report.linkToProof = linkToBugProof;

		return report.execute();
	}
	
	public String getBugDescription() {
		return bugDescription;
	}
	
	public boolean hasBugDescription() {
		return bugDescription != null;
	}
	
	public void setBugDescription(String bugDescription) {
		this.bugDescription = bugDescription;
	}
	
	public String getPlayerActionsDescription() {
		return playerActionsDescription;
	}
	
	public boolean hasPlayerActionsDescription() {
		return playerActionsDescription != null;
	}
	
	public void setPlayerActionsDescription(String playerActionsDescription) {
		this.playerActionsDescription = playerActionsDescription;
	}
	
	public long getTimeWhenBugOccured() {
		return timeWhenBugOccured;
	}
	
	public boolean hasTimeWhenBugOccured() {
		return timeWhenBugOccured != -1L;
	}
	
	public boolean setTimeWhenBugOccured(String time) {
	    Matcher matcher = pattern.matcher(time);
		Integer[] date = {0, 0, 0, 0, 0};
	    
	    for (int i = 0; matcher.find() && i < 5; i++) {
	    	String group = matcher.group();
	    	Integer digits = Methods.toInteger(group);
	    	
	    	if (digits == null)
	    		return false;
	    	
	    	date[i] = digits;
	    }
	    
	    if (date[1] < 1 || date[1] > 12 || date[0] < 1 || date[0] > 31 || date[3] < 1 || date[4] > 60 || date[4] < 1 || date[4] > 60)
	    	return false;
	    
	    timeWhenBugOccured = ZonedDateTime.now().withYear(date[2]).withMonth(date[1]).withDayOfMonth(date[0]).withHour(date[3]).withMinute(date[4]).toInstant().toEpochMilli();
	    return true;
	}
	
	public String getLinkToBugProof() {
		return linkToBugProof;
	}
	
	public boolean hasLinkToBugProof() {
		return linkToBugProof != null;
	}
	
	public void setLinkToBugProof(String linkToBugProof) {
		this.linkToBugProof = linkToBugProof;
	}
	
}
