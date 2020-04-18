package ua.kpi.tef.zu.gp3servlet.controller.security;

import ua.kpi.tef.zu.gp3servlet.entity.RoleType;
import ua.kpi.tef.zu.gp3servlet.entity.User;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Anton Domin on 2020-04-14
 */
public class UserSecurity {
	private static final String ALL_USERS = "loggedUsers";
	private static final String CURRENT_USER = "user";

	public static boolean userLoggedIn(HttpSession session) {
		return session.getAttribute(CURRENT_USER) != null;
	}

	public static boolean userLoggedIn(HttpSession session, String login) {
		return getLoggedUsers(session).contains(login);
	}

	public static User getCurrentUser(HttpSession session) {
		return (User) session.getAttribute(CURRENT_USER);
	}

	public static RoleType getCurrentRole(HttpSession session) {
		User user = (User) session.getAttribute(CURRENT_USER);
		return user == null ? null : user.getRole();
	}

	public static void addLoggedUser(HttpSession session, User user) {
		getLoggedUsers(session).add(user.getLogin());
		session.setAttribute(CURRENT_USER, user);
	}

	public static void removeLoggedUser(HttpSession session) {
		User user = (User) session.getAttribute(CURRENT_USER);
		if (user != null) removeLoggedUser(session, user.getLogin());
	}

	public static void removeLoggedUser(HttpSession session, String login) {
		getLoggedUsers(session).remove(login);
		session.removeAttribute(CURRENT_USER);
	}

	public static void initializeLoggedUsers(ServletContext context) {
		context.setAttribute(ALL_USERS, new HashSet<String>());
	}

	@SuppressWarnings("unchecked")
	public static Set<String> getLoggedUsers(HttpSession session) {
		return (Set<String>) session.getServletContext().getAttribute(ALL_USERS);
	}

	public static boolean checkPassword(User user, String password) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(password, user.getPassword());
	}
}
