package ua.kpi.tef.zu.gp3servlet.controller;

import ua.kpi.tef.zu.gp3servlet.entity.RoleType;
import ua.kpi.tef.zu.gp3servlet.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Set;

/**
 * Created by Anton Domin on 2020-04-14
 */
public class UserSecurity {
	@SuppressWarnings("unchecked")
	private static Set<String> getLoggedUsers(HttpServletRequest request) {
		return (Set<String>) request.getSession().getServletContext().getAttribute("loggedUsers");
	}

	public static boolean userLoggedIn(HttpServletRequest request, String login) {
		return getLoggedUsers(request).contains(login);
	}

	public static void addLoggedUser(HttpServletRequest request, String login, RoleType role) {
		Set<String> loggedUsers = getLoggedUsers(request);
		loggedUsers.add(login);
		request.getSession().getServletContext().setAttribute("loggedUsers", loggedUsers);

		HttpSession session = request.getSession();
		session.setAttribute("login", login);
		session.setAttribute("role", role);
	}

	public static void removeLoggedUser(HttpServletRequest request) {
		removeLoggedUser(request, request.getSession().getAttribute("login").toString());
	}

	public static void removeLoggedUser(HttpServletRequest request, String login) {
		Set<String> loggedUsers = getLoggedUsers(request);
		loggedUsers.remove(login);
		request.getSession().getServletContext().setAttribute("loggedUsers", loggedUsers);

		HttpSession session = request.getSession();
		session.setAttribute("login", null);
		session.setAttribute("role", null);
	}

	public static boolean checkPassword(User user, String password) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(password, user.getPassword());
	}
}
