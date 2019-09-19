package com.exadel.exc;

import static com.exadel.exc.Utils.loadProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Welcome page
 */
public class StartServlet extends HttpServlet {

	private static final long serialVersionUID = 7642347973961332421L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		try {
			Properties pp = loadProperties();

			List<String> logFiles = new ArrayList<String>();
			Map<String,String> logNames = new HashMap<String,String>();
			Map<String,String> logStamps = new HashMap<String,String>();
			for (String key: pp.stringPropertyNames()) {
			    if (key.endsWith(".name")) {
			        logNames.put(key.substring(0, key.length()-".name".length()), pp.getProperty(key));
				} else
				if (key.endsWith(".tstamp")) {
			        logStamps.put(key.substring(0, key.length()-".tstamp".length()), pp.getProperty(key));
				} else {
			        logFiles.add(key);
			    }
			}
			Collections.sort(logFiles);

			for (String key: logFiles) {
			    if (logNames.get(key)==null) {
			        logNames.put(key,key);
			    }
		    }

			request.setAttribute("logFiles", logFiles);
			request.setAttribute("logNames", logNames);
			request.getSession().setAttribute("logStamps", logStamps);
			request.getRequestDispatcher("index.jsp")
					.forward(request, response);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
