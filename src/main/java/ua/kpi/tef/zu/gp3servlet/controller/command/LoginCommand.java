package ua.kpi.tef.zu.gp3servlet.controller.command;

import lombok.extern.slf4j.Slf4j;
import ua.kpi.tef.zu.gp3servlet.controller.*;
import ua.kpi.tef.zu.gp3servlet.entity.User;
import ua.kpi.tef.zu.gp3servlet.service.UserService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton Domin on 2020-04-14
 */
@Slf4j
public class LoginCommand implements Command {
	@Override
	public String execute(HttpServletRequest request) {
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		log.debug("Login request received: " + login + ":" + password); //TODO: disable logging user passwords, lol
		User user;

		try {
			checkLogin(login);
			checkAlreadyLoggedIn(request, login);
			user = UserService.findByLogin(login);
			checkPassword(user, password);
		} catch (DatabaseException | IllegalArgumentException e) {
			log.error(e.getMessage());
			return "redirect:?error";
		}

		UserSecurity.addLoggedUser(request, login, user.getRole());
		return "redirect:lobby";
	}

	private void checkLogin(String login) throws IllegalArgumentException {
		if (!login.matches(RegistrationValidation.LOGIN_REGEX))
			throw new IllegalArgumentException("Wrong login: " + login);
	}

	private void checkAlreadyLoggedIn(HttpServletRequest request, String login) {
		if (UserSecurity.userLoggedIn(request, login)) {
			throw new IllegalArgumentException("User already logged in: " + login);
		}
	}

	private void checkPassword(User user, String password) throws IllegalArgumentException {
		if (!UserSecurity.checkPassword(user, password))
			throw new IllegalArgumentException("Wrong password for user: " + user.getLogin());
	}
}
