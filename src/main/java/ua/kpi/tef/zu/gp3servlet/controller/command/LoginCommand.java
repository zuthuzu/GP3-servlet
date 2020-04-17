package ua.kpi.tef.zu.gp3servlet.controller.command;

import ua.kpi.tef.zu.gp3servlet.controller.*;
import ua.kpi.tef.zu.gp3servlet.controller.security.UserSecurity;
import ua.kpi.tef.zu.gp3servlet.entity.User;
import ua.kpi.tef.zu.gp3servlet.service.UserService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton Domin on 2020-04-14
 */
public class LoginCommand implements Command {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LoginCommand.class);
	private final UserService userService;

	public LoginCommand() {
		userService = new UserService();
	}

	@Override
	public String execute(HttpServletRequest request) {
		try {
			attemptLogin(request);
		} catch (SecurityException e) {
			log.warn(e.getMessage());
			return "redirect:?error";
		} catch (DatabaseException | IllegalArgumentException e) {
			log.error(e.getMessage());
			return "redirect:?error";
		}
		return "redirect:lobby";
	}

	private void attemptLogin(HttpServletRequest request)
			throws DatabaseException, IllegalArgumentException, SecurityException {
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		log.debug("Login request received: " + login + ":" + password); //TODO: disable logging user passwords, lol

		checkLogin(login);
		checkAlreadyLoggedIn(request, login);

		User user = userService.findByLogin(login);
		checkPassword(user, password);

		UserSecurity.addLoggedUser(request, login, user.getRole());
	}

	private void checkLogin(String login) throws IllegalArgumentException {
		if (login == null)
			throw new IllegalArgumentException("Missing login string");

		if (!login.matches(RegistrationValidation.LOGIN_REGEX))
			throw new IllegalArgumentException("Malformed login string: " + login);
	}

	private void checkAlreadyLoggedIn(HttpServletRequest request, String login) {
		if (UserSecurity.userLoggedIn(request, login))
			throw new SecurityException("Login failed: user already logged in: " + login);
	}

	private void checkPassword(User user, String password) {
		if (password == null)
			throw new IllegalArgumentException("Missing password string for user: " + user.getLogin());

		if (!UserSecurity.checkPassword(user, password))
			throw new SecurityException("Login failed: wrong password for user: " + user.getLogin());
	}
}
