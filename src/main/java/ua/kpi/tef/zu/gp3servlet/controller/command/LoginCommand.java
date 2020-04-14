package ua.kpi.tef.zu.gp3servlet.controller.command;

import lombok.extern.slf4j.Slf4j;
import ua.kpi.tef.zu.gp3servlet.controller.UserSecurity;
import ua.kpi.tef.zu.gp3servlet.entity.RoleType;

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
		log.info("Login request received: " + login + ":" + password);

		//TODO actual DB lookup etc

		UserSecurity.addLoggedUser(request, login, RoleType.ROLE_USER);
		return "redirect:lobby";
	}
}
