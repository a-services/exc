package com.exadel.exc;

import static com.exadel.exc.Utils.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to show a piece of log file.
 */
public class BlockServlet extends HttpServlet {

	private static final long serialVersionUID = -3650786007931850999L;
	
	private static final int LINES_BEFORE = 20;
	private static final int LINES_AFTER = 300;
	
	/**
	 * Show a piece of log file.
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException 
	{
		try {
			Properties pp = loadProperties();
			String pName = getRequestParameter("name", IN_NAME, request);
			String inName = expand(pp.getProperty(pName));
			int totalCount = getTotalCount(inName, pName, request.getSession());
			
			int startLNo = 1;
			try {
			    Integer lnoAttr = (Integer) request.getAttribute("lno");
			    if (lnoAttr==null) {
				    startLNo = Integer.parseInt(request.getParameter("lno"));
				} else {
					startLNo = lnoAttr;
				}
			} catch (NumberFormatException e) {
				startLNo = totalCount - LINES_AFTER;
			}
			if (startLNo < 1)
				startLNo = 1;

			int n2 = startLNo + LINES_AFTER;
			if (n2 > totalCount)
				n2 = totalCount;

			int n1 = startLNo - LINES_BEFORE;

			int linesOnScreen = LINES_BEFORE + LINES_AFTER; 
			if (n1 > n2 - linesOnScreen)
				n1 = n2 - linesOnScreen;
			if (n1 < 1)
				n1 = 1;
			if (startLNo > n2)
				startLNo = n2;

			String[] slines = new String[n2 - n1 + 1];

			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(inName), "UTF-8"));
			int lno = 0;
			while (in.ready()) {
				String line = in.readLine();
				if (line == null) {
					n2 = lno;
					saveTotalCount(lno, pName, request.getSession());
					break;
				}

				lno++;
				if (lno >= n1) {
					slines[lno - n1] = xmp(line);
					if (lno >= n2) {
						break;
					}
				}
			}
			in.close();

			//System.out.println("[BlockServlet] n1=" + n1 + ", n2=" + n2);
			request.setAttribute("n1", n1);
			request.setAttribute("n2", n2);
			request.setAttribute("lno", startLNo);
			request.setAttribute("lines", slines);

			request.setAttribute("inName", inName);
			if (request.getParameter("name") != null)
				request.setAttribute("pName", pName);
			request.getRequestDispatcher("block.jsp")
					.forward(request, response);

		} catch (IOException e) {
			System.out.println("/exc/block - problem: "+e.getMessage());
		}
	}
	
	/**
	 * Find by timestamp.
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException 
	{
		try {
			Properties pp = loadProperties();
			String pName = getRequestParameter("name", IN_NAME, request);
			String inName = expand(pp.getProperty(pName));
			String tstamp = request.getParameter("tstamp");
			int lno = ExcServlet.proc.findTime(tstamp, inName, ExcServlet.tse);
			request.setAttribute("lno", lno);
			doGet(request, response);
			
		} catch (IOException e) {
			System.out.println("/exc/block - problem: "+e.getMessage());
		}
	}

}
