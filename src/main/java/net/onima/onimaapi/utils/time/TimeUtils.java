package net.onima.onimaapi.utils.time;

import net.onima.onimaapi.utils.Methods;

public class TimeUtils {
	
	public static long timeToMillis(String time) {
		String[] splitTime = time.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)"); //Not my regex, found it at AdvancedBan's source code.

		if (splitTime.length == 1)
			return -2;
		
		long finalTime = 0;
		
		for (int i = 0; i < splitTime.length; i+=2) {
			long toMillis = Methods.toLong(splitTime[i]);
			
			if (toMillis == -1)
				return -1;
				
			switch(splitTime[i+1]) {
			case "se":
				finalTime += toMillis * Time.SECOND;
				break;
			case "mi":
				finalTime += toMillis * Time.MINUTE;
				break;
			case "ho":
				finalTime+=toMillis * Time.HOUR;
				break;
			case "da":
				finalTime+=toMillis * Time.DAY;
				break;
			case "mo":
				finalTime+=toMillis * Time.MONTH;
				break;
			case "ye":
				finalTime+=toMillis * Time.YEAR;
				break;
			default:
				return -1;
			}
		}
		return finalTime;
	}
	
	public static long timeToSeconds(String time) {
		long fTime = timeToMillis(time);
		
		if (fTime == -1)
			return -1;
		else if (fTime == -2)
			return -2;
		
		return fTime / 1000;
	}

}