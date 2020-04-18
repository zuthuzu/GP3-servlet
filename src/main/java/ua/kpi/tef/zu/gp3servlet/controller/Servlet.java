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
	private final Map<String, Command> commands = new HashMap<>();
	//private ResourceBundle bundle;

	public void init(ServletConfig servletConfig) {
		UserSecurity.initializeLoggedUsers(servletConfig.getServletContext());
		MappingUtility.initializeCommands(commands);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getRequestURI();
		String query = request.getQueryString();

		if (path.contains("redirect:")) {
			path = path.replace("redirect:", "") + (query == null ? "" : "?" + query);
			response.sendRedirect(path);
			return;
		}

		path = MappingUtility.digCommandFromURI(path);
		log.debug("Raw URI: " + request.getRequestURI() + " -> sanitized command from URI: " + path);
		Command command = commands.getOrDefault(path, commands.get("index"));
		String page = command.execute(request);
		request.getRequestDispatcher(page).forward(request, response);
	}

	/*public String getLocalizedText(String token) {
		return bundle.keySet().contains(token) ? bundle.getString(token) : token;
	}*/
}
