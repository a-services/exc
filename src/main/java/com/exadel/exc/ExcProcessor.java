package com.exadel.exc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class ExcProcessor {

	int lno;
	
	public List<Exc> process(String inName, TimestampExtractor tse)
			throws IOException {
		LinkedList<Exc> exceptions = new LinkedList<Exc>();
		lno = 0;
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(inName), "UTF-8"));
		String lastTime = null;
		String lastComment = null;
		while (in.ready()) {
			String line = in.readLine();
			lno++;
			if (line == null)
				break;
			
			String tstamp = tse.extractTimestamp(line);
			if (tstamp!=null) {
			    if (lastTime==null) {
				    exceptions.add(createExc("Start time", tstamp, null));
			    }
				lastTime = tstamp;
			}

			String comment = tse.extractComment(line);
			if (comment!=null) {
				lastComment = comment;
			}
			
			// process exceptions
			String sig = extractException(line);
			if (sig != null) {
				exceptions.add(createExc(sig, lastTime, lastComment));
			}
		}
        if (lastTime!=null) {
            exceptions.add(createExc("End time", lastTime, null));
        }
		in.close();
		return exceptions;
	}

	Exc createExc(String sig, String time, String cmt) {
        Exc exc = new Exc();
        exc.lno = lno;
        exc.sig = sig;
        exc.time = time;
        exc.cmt = cmt;
        return exc;
	}
	
    private static final Pattern exceptionPattern = Pattern.compile("(([a-z])+\\.)+[A-Z][a-zA-Z]*(Exception|Error)");

    private String extractException(String line) {
        StringTokenizer st = new StringTokenizer(line);
        while (st.hasMoreTokens()) {
            String token = trimSpecial(st.nextToken());
            if (exceptionPattern.matcher(token).matches())
                return token;
        }
        return null;
	}

    /** Trims not only spaces but any non-letters. */
    String trimSpecial(String token) {
        int k1 = 0;
        int k2 = token.length() - 1;
        for (; k1 <= k2; k1++) {
            if (Character.isLetter(token.charAt(k1)))
                break;
        }
        for (; k1 <= k2; k2--) {
            if (Character.isLetter(token.charAt(k2)))
                break;
        }
        return token.substring(k1, k2 + 1);
    }

	public int findTime(String start, String inName, TimestampExtractor tse) throws IOException {
	    Date targetTime = tse.parse(start);
	    if (targetTime.getTime()==0) {
	        return 1;
	    }
	    BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(inName), "UTF-8"));
		lno = 0;
		while (in.ready()) {
		    String line = in.readLine();
			lno++;
		    if (line == null) {
				break;
		    }
			String tstamp = tse.extractTimestamp(line);
			if (tstamp!=null) {
			    Date date = tse.getTimestamp();
			    if (targetTime.compareTo(date)<=0) {
			        break;
			    }
			}
		}
		in.close();    
		return lno;
	}

}
