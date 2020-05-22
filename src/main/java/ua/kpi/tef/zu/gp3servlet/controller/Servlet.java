package ua.kpi.tef.zu.gp3servlet.controller;

import ua.kpi.tef.zu.gp3servlet.controller.command.Command;
import ua.kpi.tef.zu.gp3servlet.controller.security.UserSecurity;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by Anton Domin on 2020-04-14
 */
public class Servlet extends HttpServlet {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Servlet.class);

	public void init(ServletConfig servletConfig) {
		UserSecurity.initializeLoggedUsers(servletConfig.getServletContext());
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getRequestURI();
		String query = request.getQueryString();

		if (path.contains(MappingUtility.REDIRECT)) {
			path = path.replace(MappingUtility.REDIRECT, "") + (query == null ? "" : "?" + query);
			response.sendRedirect(path);
			return;
		}

		Command command = MappingUtility.getCommand(path);
		String page = command.execute(request);
		request.getRequestDispatcher(page).forward(request, response);
	}
}
