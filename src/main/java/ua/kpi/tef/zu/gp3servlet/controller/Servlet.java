package ua.kpi.tef.zu.gp3servlet.controller;

import lombok.extern.slf4j.Slf4j;
import ua.kpi.tef.zu.gp3servlet.controller.command.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anton Domin on 2020-04-14
 */
@Slf4j
public class Servlet extends HttpServlet {
	private final static String DOMAIN = "/repair"; //pom.xml: tomcat7-maven-plugin configuration
	private final static String MAPPING = "/"; //web.xml: currently mapped to domain root
	private final static String[] URL_JUNK_TOKENS = new String[] {".*" + DOMAIN, MAPPING, "/", "\\.jsp", "\\.html"};

	private Map<String, Command> commands = new HashMap<>();

	public void init(ServletConfig servletConfig) {
		servletConfig.getServletContext().setAttribute("loggedUsers", new HashSet<String>());

		commands.put("index" , (request) -> "/index.jsp");
		commands.put("registration" , new RegistrationCommand());
		commands.put("reg" , new RegistrationCommand());
		commands.put("login" , new LoginCommand());
		commands.put("logout" , new LogoutCommand());
		commands.put("lobby" , new LobbyCommand());
		commands.put("error" , new ErrorCommand());
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getRequestURI();
		if (path.contains("redirect:")) {
			response.sendRedirect(path.replace("redirect:", ""));
			return;
		}

		path = digCommandFromURI(path);
		log.info("Sanitized command from URL: " + path);
		Command command = commands.getOrDefault(path, commands.get("index"));
		String page = command.execute(request);
		request.getRequestDispatcher(page).forward(request, response);
	}

	private String digCommandFromURI(String path) {
		StringBuilder result = new StringBuilder(path);
		Arrays.stream(URL_JUNK_TOKENS).forEach(jt -> stripToken(result, jt));
		return result.toString();
	}

	private void stripToken(StringBuilder path, String token) {
		Matcher m = Pattern.compile(token).matcher(path);
		path.replace(0, path.length(), m.replaceAll(""));
	}
}
