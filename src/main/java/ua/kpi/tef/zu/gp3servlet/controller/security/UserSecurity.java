package ua.kpi.tef.zu.gp3servlet.controller.security;

import ua.kpi.tef.zu.gp3servlet.entity.RoleType;
import ua.kpi.tef.zu.gp3servlet.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Set;

/**
 * Created by Anton Domin on 2020-04-14
 */
public class UserSecurity {
	public static boolean userLoggedIn(HttpServletRequest request) {
		return request.getSession().getAttribute("login") != null;
	}

	public static boolean userLoggedIn(HttpServletRequest request, String login) {
		return getLoggedUsers(request).contains(login);
	}

	public static void addLoggedUser(HttpServletRequest request, String login, RoleType role) {
		Set<String> loggedUsers = getLoggedUsers(request);
		loggedUsers.add(login);

		HttpSession session = request.getSession();
		session.setAttribute("login", login);
		session.setAttribute("role", role);
	}

	public static void removeLoggedUser(HttpServletRequest request) {
		String currentUser = (String) request.getSession().getAttribute("login");
		if (currentUser != null) removeLoggedUser(request, currentUser);
	}

	public static void removeLoggedUser(HttpServletRequest request, String login) {
		Set<String> loggedUsers = getLoggedUsers(request);
		loggedUsers.remove(login);

		HttpSession session = request.getSession();
		session.removeAttribute("login");
		session.removeAttribute("role");
	}

	@SuppressWarnings("unchecked")
	private static Set<String> getLoggedUsers(HttpServletRequest request) {
		return (Set<String>) request.getSession().getServletContext().getAttribute("loggedUsers");
	}

	public static boolean checkPassword(User user, String password) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(password, user.getPassword());
	}
}
