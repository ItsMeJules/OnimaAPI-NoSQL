package net.onima.onimaapi.utils.time;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DurationFormatUtils;

public class Time { 
	
	public static final long SECOND = 1000L;
	public static final long MINUTE = SECOND * 60L;
	public static final long HOUR = MINUTE * 60L;
	public static final long DAY = HOUR * 24L;
	public static final long WEEK = DAY * 7L;
	public static final long MONTH = DAY * 31L;
	public static final long YEAR = MONTH * 12L;
	
	private static final String[] format = { " année", " mois", " semaine", " jour", " heure", " minute", " seconde" };
	private static long[] values = { YEAR, MONTH, WEEK, DAY, HOUR, MINUTE, SECOND };
	
	private static DecimalFormat msFormat = new DecimalFormat("0.0");
	private static DecimalFormat sFormat = new DecimalFormat("0");
  
	public static DecimalFormat getDecimalFormat(boolean ms) {
		return ms == true ? msFormat : sFormat;
	}
    
	public static class IntegerTime {  
		
		public static int convertMillisecondsToSeconds(Long paramLong) {
			return (int) (paramLong / 1000L);
		}
    
		public static String setHMSFormat(Integer paramInteger)	{
			int remainder = paramInteger * 1000;
    
			int seconds = remainder / 1000 % 60;
			int minutes = remainder / 60000 % 60;
			int hours = remainder / 3600000 % 24;
    
			return (hours > 0 ? String.format("%02d" + ":", hours) : "") + String.format("%02d:%02d", minutes, seconds);
		}
    
		public String setMSFormat(Integer paramInteger) {
			int minutes = (int) (paramInteger / 60.0D);
			int seconds = paramInteger % 60;
    
			return String.format("%d:%02d", minutes, seconds);
		}
		
		public static String setYMDWHMSFormat(Integer paramInteger) {
			StringBuilder builder = new StringBuilder();
			String formatStr = "";
			long paramLong = (long) paramInteger;
			
			for (int i = 0; i < 7 && paramLong > 0; ++i) {
				long o = paramLong / values[i];
				
				if (o > 0) {
					formatStr = format[i];
					builder.append(o).append(Long.valueOf(o) > 1 ? formatStr.equals(" mois") ? formatStr : formatStr + 's' : formatStr)
							.append(" ");
				}
				paramLong %= values[i];
			}
			
			builder.deleteCharAt(builder.length() - 1);
			return builder.toString();
		}
  }
  
  public static class LongTime { 
    
	  public static long convertSecondsToMilliseconds(Integer paramInteger) {
		  return (long) (paramInteger * 1000L);
	  }   
    
    
	  public static String setHMSFormat(Long paramLong)  {
		  if (paramLong < TimeUnit.MINUTES.toMillis(1L))
			  return getDecimalFormat(true).format(paramLong / 1000.0D) + "s";

		  return DurationFormatUtils.formatDuration(paramLong, (paramLong >= TimeUnit.HOURS.toMillis(1L) ? "HH:" : "") + "mm:ss");
	  }
	  
	  public static String setHMSFormatOnlySeconds(Long paramLong)  {
		  if (paramLong < TimeUnit.MINUTES.toMillis(1L))
			  return getDecimalFormat(false).format(paramLong / 1000.0D) + "s";

		  return DurationFormatUtils.formatDuration(paramLong, (paramLong >= TimeUnit.HOURS.toMillis(1L) ? "HH:" : "") + "mm:ss");
	  }
	  
	  public static String setDHMSFormat(Long paramLong) {
		  if (paramLong < TimeUnit.MINUTES.toMillis(1L))
			  return getDecimalFormat(false).format(paramLong / 1000.0D) + "s";
		  
		  return DurationFormatUtils.formatDuration(paramLong, (paramLong >= TimeUnit.DAYS.toMillis(1L) ? "dd:HH:" : "HH:") + "mm:ss");
	  }
	  
		public static String setYMDWHMSFormat(long paramLong) {
			if(paramLong == 0 || paramLong < 1000) return "null";
			String s = "";
			String formatStr = "";
			
			for(int i = 0; i < 7 && paramLong > 0; ++i) {
				long o = paramLong / values[i];
				
				if(o > 0l) {
					formatStr = format[i];
					s += o + (Long.valueOf(o) > 1 ? formatStr.equals(" mois") ? formatStr : formatStr + "s" : formatStr)
							+ " ";
				}
				paramLong %= values[i];
			}
			return s.substring(0, s.length() - 1);
	  }
  }
  
}
