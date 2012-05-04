package com.yippee.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tdu2 Yippee servlet: GET request the search keyword page. POST
 *         requests accept a RESTful input and sends QUERY to the FreePastry
 *         ring.
 * 
 */
public class YippeeSearch extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -848966826189202866L;

	// URL-pattern: /yippee
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		out.println("<HTML><HEAD><TITLE>Yippee Search Engine</TITLE></HEAD>");
		out.println("<H1>Yippee!</H1>");
		out.println("<form action=\"" + request.getContextPath()
				+ "/yippee\" method=\"post\">"
				+ "Keywords: <input type=\"text\" name=\"keywords\" /><br />"
				+ "<input type=\"submit\" value=\"Submit\" />" + "</form>");
		out.println("</HTML>");

		out.close();
		response.flushBuffer();
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// Get starting params
		String keywords = request.getParameter("keywords");
		String cacheServer = getServletConfig().getInitParameter("cacheServer");
		int cacheServerPort = Integer.parseInt(getServletConfig()
				.getInitParameter("cacheServerPort"));

		if (keywords.length() != 0) {
			// The search box wasn't blank
			// Send query request to the ring
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/xhtml+xml");

			PrintWriter out = response.getWriter();

			Socket client = new Socket(cacheServer, cacheServerPort);
			PrintWriter searchOut = new PrintWriter(client.getOutputStream(),
					false);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					client.getInputStream()));

			searchOut.write(keywords);
			searchOut.flush();
			client.shutdownOutput();

			// Get response from the ring
			String results = "", tmp;
			while ((tmp = reader.readLine()) != null) {
				results += tmp;
			}

//			System.out.println(results);

			client.close();

			out.print(results);
			out.close();
			response.flushBuffer();
		} else {
			// Blank keywords display the form again
			
			response.setContentType("text/html");

			PrintWriter out = response.getWriter();

			out.println("<HTML><HEAD><TITLE>Yippee Search</TITLE></HEAD>");
			out.println("<H1>Yippee!</H1>");
			out
					.println("<form action=\""
							+ request.getContextPath()
							+ "/yippee\" method=\"post\">"
							+ "Keywords: <input type=\"text\" name=\"keywords\" /><br />"
							+ "<input type=\"submit\" value=\"Submit\" />"
							+ "</form>");
			out.println("</HTML>");

			out.close();
			response.flushBuffer();
		}
	}

}
