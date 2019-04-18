package com.exadel.exc;

import static com.exadel.exc.Utils.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to find lines containing pattern in log file.
 */
public class PatServlet extends HttpServlet {

	private static final long serialVersionUID = 1873629131714535270L;
	
	final int linesBefore = 5;
	final int linesAfter = 5;
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//System.out.println("/exc/pat - find lines containing pattern in log file...");
		try {
			Properties pp = loadProperties();
			String pName = getRequestParameter("name", IN_NAME, request);
			String inName = expand(pp.getProperty(pName));
			String patParamName = getRequestParameter("p", "patName", request);
			String pattern = pp.getProperty(patParamName);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(inName), "UTF-8"));

			LinkedList<Line> result;
			String[] buffer = new String[linesBefore+1];
			int bufHead = 0;
			int lastStored = Integer.MIN_VALUE;
			int aheadCounter = 0;

			result = new LinkedList<Line>();
			
			int lno = 0;
			while (in.ready()) {
				String line = in.readLine();
				if (line == null) {
					saveTotalCount(lno, pName, request.getSession());
					break;
				}

				// save line in buffer
				if (bufHead == buffer.length)
					bufHead = 0;
				buffer[bufHead++] = line;

				lno++;
				if (line.contains(pattern)) {
					//System.out.println("Found at "+lno);
					
					// store previous lines, if not already stored
					for (int i=linesBefore+1; i>1; i--) {
						int n = lno - i + 1;
						if (lastStored>=n) continue; 
						int k = (bufHead + buffer.length - i) % buffer.length;
						result.add(new Line(n, buffer[k], false));
					}
						
					// store current line
					result.add(new Line(lno, line, true));
					aheadCounter = linesAfter;
					/*
					if (result.size() != 0) {
						Line last = result.getLast();
						if (last.lno < lno - 1)
							result.add(new Line(lno - 1, prev));
					}*/
					
				} else
				if (aheadCounter>0) {
					result.add(new Line(lno, line, false));
					aheadCounter--;
					lastStored = lno;
				}
			}
			// System.out.println("in.markSupported()="+in.markSupported()); // markSupported = true
			in.close();

			request.setAttribute("lines", result);
			request.setAttribute("pattern", pattern);

			request.setAttribute("inName", inName);
			if (request.getParameter("name") != null)
				request.setAttribute("pName", pName);
			request.getRequestDispatcher("pattern.jsp")
					.forward(request, response);
			
		} catch (IOException e) {
			System.out.println("/exc/pat - problem: "+e.getMessage());
		}
	}


}
