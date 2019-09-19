package com.exadel.exc;

import static com.exadel.exc.Utils.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to collect list of exceptions in file.
 */
public class ExcServlet extends HttpServlet {

	private static final long serialVersionUID = -2167027890772474468L;

	public static final ExcProcessor proc = new ExcProcessor();
	public static final TimestampExtractor tse = new SimpleTimestampExtractor();

	/**
	 * Collect list of exceptions in file
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		try {
			Properties pp = loadProperties();
			String pName = getRequestParameter("name", IN_NAME, request);
			String inName = expand(pp.getProperty(pName));

			Map<String,String> logStamps = (Map<String,String>)request.getSession().getAttribute("logStamps");
			tse.setDateFormat(logStamps.get(pName));

			List<Exc> result;
			try {
				result = proc.process(inName, tse);
			} catch (IOException e) {
				appendToErrorLog("/exc - file not found: "+inName);
				result = new LinkedList<Exc>();
			}
			saveTotalCount(proc.lno, pName, request.getSession());
			Collections.sort(result, new Comparator<Exc>() {
                public int compare(Exc e1, Exc e2) {
                    Date t1 = tse.parse(e1.time);
                    Date t2 = tse.parse(e2.time);
                    return -t1.compareTo(t2);
                }
			});
			request.setAttribute("exceptions", result);
			request.setAttribute("inName", inName);
			if (request.getParameter("name") != null)
				request.setAttribute("pName", pName);
			request.getRequestDispatcher("exceptions.jsp").forward(request,
					response);

		} catch (IOException e) {
			appendToErrorLog(e.getClass() + ": " + e.getMessage());
		}
	}

}
