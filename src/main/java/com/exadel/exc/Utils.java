package com.exadel.exc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Misc static utilities.
 */
public class Utils {

	/**
	 * Property name which value is the path to log file.
	 */
	public static String IN_NAME;

	/**
	 * Load properties from user's home folder, file name will be
	 * `~/exc.properties`. If `spring.profiles.active` environment variable is
	 * defined, then file name will be `~/exc-<env>.properties`.
	 */
	public static Properties loadProperties() throws IOException {
		String propFileName = getPropFileName();
		Properties pp = new Properties();

		/* Configure logs here */
		if (!new File(propFileName).exists()) {
			String fileName = appendToErrorLog("java.io.FileNotFoundException: " + propFileName);
			pp.put("exc", fileName);
		} else {
			FileInputStream in = new FileInputStream(propFileName);
			pp.load(in);
			in.close();
		}

		/* First property is expected to contain the path to log file. */
		IN_NAME = (String) pp.stringPropertyNames().toArray()[0];
		return pp;
	}

	/**
	 * Load string from file.
	 */
	public static String loadStr(String fname) throws FileNotFoundException, IOException {
		FileInputStream in = new FileInputStream(fname);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int count = 0;
		final int BUF_SIZE = 8 * 1024;
		byte[] buffer = new byte[BUF_SIZE];
		while ((count = in.read(buffer, 0, BUF_SIZE)) > 0)
			out.write(buffer, 0, count);
		out.flush();
		in.close();
		return out.toString();
	}

	/**
	 * Save string to file.
	 */
	public static void saveStr(String fname, String text) throws IOException {
		PrintWriter out = new PrintWriter(new FileOutputStream(fname));
		out.print(text);
		out.close();
	}

	public static String getPropFileName() {
		String configFile = System.getenv("EXC_CONFIG");
		if (configFile == null) {
			String profile = System.getProperty("spring.profiles.active");
			configFile = System.getProperty("user.home") + "/.exc" + (profile != null ? "-" + profile : "");
		}
		return configFile;
	}

	/**
	 * Create error log in home folder.
	 */
	public static String appendToErrorLog(String errorMessage) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String fileName = System.getProperty("user.home") + "/exc_error.log";
		String tstamp = df.format(new Date());
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(new File(fileName), true)));
			out.print(tstamp + ": " + errorMessage);
			out.close();
		} catch (IOException e) {
			System.out.println(tstamp + ": Error writing to `" + fileName + "`: " + e);
		}
		return fileName;
	}

	public static String getRequestParameter(String paramName, String defaultValue, HttpServletRequest request) {
		String value = request.getParameter(paramName == null ? IN_NAME : paramName);
		return value == null ? defaultValue : value;
	}

	public static void saveTotalCount(int totalCount, String pName, HttpSession session) {
		String aName = pName + "_count";
		session.setAttribute(aName, new Integer(totalCount));
	}

	public static int getTotalCount(String inName, String pName, HttpSession session) {
		if (pName == null)
			pName = IN_NAME;
		String aName = pName + "_count";
		Integer k = (Integer) session.getAttribute(aName);
		if (k == null) {
			k = countLines(inName, pName, session);
		}
		return k;
	}

	public static int countLines(String inName, String pName, HttpSession session) {
		int lno = 0;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inName), "UTF-8"));
			while (in.ready()) {
				String line = in.readLine();
				if (line == null) {
					break;
				}
				lno++;
			}
		} catch (IOException ex) {
		}
		saveTotalCount(lno, pName, session);
		return lno;
	}

	public static String xmp(String s) {
		return s.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;");
	}

	public static String expand(String s) {
		if (s.contains("<date>")) {
			String curDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			s = s.replace("<date>", curDate);
		}
		return s;
	}
}
