package net.onima.onimaapi.utils;

import java.util.Calendar;
import java.util.Date;

public class CasualFormatDate {
	 
	private String pattern;
	
	/**
	 * Patterns: 
	 * d = day in letters
	 * z = month in letters
	 * u = day of month
	 * y = year
	 * h = hour
	 * i = minute
	 * s = second
	 * 
	 */
	public CasualFormatDate(String pattern) {
		this.pattern = pattern;
	}
	
	public String toNormalDate(long time) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(new Date(time));
		
		int day = calendar.get(Calendar.DAY_OF_MONTH), hour = calendar.get(Calendar.HOUR_OF_DAY), minute = calendar.get(Calendar.MINUTE);
		
		pattern = pattern.replace("y", String.valueOf(calendar.get(Calendar.YEAR)));
		pattern = pattern.replace("u", (day < 10 ? "0" : "") + String.valueOf(day));
		pattern = pattern.replace("h", (hour < 10 ? "0" : "") + String.valueOf(hour) + 'H');
		pattern = pattern.replace("i", (minute < 10 ? "0" : "") + String.valueOf(minute));

		
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
		case 1:
			pattern = pattern.replace("d", "Dimanche");
			break;
		case 2:
			pattern = pattern.replace("d", "Lundi");
			break;
		case 3:
			pattern = pattern.replace("d", "Mardi");
			break;
		case 4:
			pattern = pattern.replace("d", "Mercredi");
			break;
		case 5:
			pattern = pattern.replace("d", "Jeudi");
			break;
		case 6:
			pattern = pattern.replace("d", "Vendredi");
			break;
		case 7:
			pattern = pattern.replace("d", "Samedi");
			break;
		default:
			break;
		}
		
		switch (calendar.get(Calendar.MONTH) + 1) {
		case 1:
			pattern = pattern.replace("z", "Janvier");
			break;
		case 2:
			pattern = pattern.replace("z", "Février");
			break;
		case 3:
			pattern =	pattern.replace("z", "Mars");
			break;
		case 4:
			pattern = pattern.replace("z", "Avril");
			break;
		case 5:
			pattern = pattern.replace("z", "Mai");
			break;
		case 6:
			pattern = pattern.replace("z", "Juin");
			break;
		case 7:
			pattern = pattern.replace("z", "Juillet");
			break;
		case 8:
			pattern = pattern.replace("z", "Août");
			break;
		case 9:
			pattern = pattern.replace("z", "Septembre");
			break;
		case 10:
			pattern = pattern.replace("z", "Octobre");
			break;
		case 11:
			pattern = pattern.replace("z", "Novembre");
			break;
		case 12:
			pattern = pattern.replace("z", "Décembre");
			break;
		default:
			break;
		}
		
		return pattern;
	}

}
