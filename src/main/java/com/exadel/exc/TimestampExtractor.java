package com.exadel.exc;

import java.util.Date;

public interface TimestampExtractor {

	String extractTimestamp(String line);

	Date parse(String tstamp);
	
	Date getTimestamp();
	
	String extractComment(String line);
	
}
