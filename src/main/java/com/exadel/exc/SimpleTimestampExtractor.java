package com.exadel.exc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Extract timestamp from the beginning of log line
 * in common `yyyy-MM-dd HH:mm:ss.SSS` format.
 *
 * It that fails, try `yyyy-MM-dd HH:mm:ss` format.
 *
 * It that fails, try `yyyy-MM-dd HH:mm` format.
 */
public class SimpleTimestampExtractor implements TimestampExtractor {

	/**
	 * Expected length of timestamp
	 */
	private static final int LEN = 24;

	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static final SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	Date date;

	@Override
	public String extractTimestamp(String line) {
		if (line==null || line.length()<LEN) {
			return null;
		}
		try {
			String tstamp = line.substring(0,LEN);
		    date = df.parse(tstamp);
		    return tstamp;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Date getTimestamp() {
		return date;
	}

	public Date parse(String tstamp) {
		try {
			return df.parse(tstamp);
		} catch (ParseException e) {
		    try {
		        return df2.parse(tstamp);
		    } catch (ParseException e2) {
                try {
                    return df3.parse(tstamp);
                } catch (ParseException e3) {
                    return new Date(0);
                }
		    }
		}
	}

	/**
	 * The following code can be included into servlet filter
	 * of the app to track which URL causes which exception.
	 *
	 * ```java
		private void memoryInfo(HttpServletRequest request) {
			long heapSize = Runtime.getRuntime().totalMemory();
			long heapMaxSize = Runtime.getRuntime().maxMemory();
			long heapFreeSize = Runtime.getRuntime().freeMemory();
			String uri = request.getRequestURI();
			log.info("[memoryInfo] uri=" + uri + ", free=" + heapFreeSize + ", total=" + heapSize + ", max=" + heapMaxSize);
		}
	 * ```
	 */
	@Override
	public String extractComment(String line) {
	    if (line==null) {
	        return null;
	    }
	    final String FLAG = "[memoryInfo] uri=";
	    int k = line.indexOf(FLAG);
	    if (k==-1) {
	        return null;
	    }
	    k += FLAG.length();
	    int n = line.indexOf(",", k);
	    if (n==-1) {
	        return null;
	    }
		return line.substring(k, n);
	}

}
