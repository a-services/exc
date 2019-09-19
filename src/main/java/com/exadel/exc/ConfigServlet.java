package com.exadel.exc;

import static com.exadel.exc.Utils.*;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to edit .exc property file.
 */
public class ConfigServlet extends HttpServlet {

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("/exc/config - edit .exc property file...");
		try {
			Properties pp = loadProperties();
			String propFile = Utils.getPropFileName();

			String result = "";
			try {
				result = loadStr(propFile);
			} catch (IOException e) {
				// ignore
			}

			request.setAttribute("configName", propFile);
			request.setAttribute("configText", result);

			request.getRequestDispatcher("config.jsp").forward(request, response);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("/exc/config - post...");
		String result = request.getParameter("config");
		String propFile = Utils.getPropFileName();
		try {
			saveStr(propFile, result);
		} catch(IOException e) {
			// ignore
		}
		request.setAttribute("configName", propFile);
		request.setAttribute("configText", result);

		request.getRequestDispatcher("config.jsp").forward(request, response);
	}

}
