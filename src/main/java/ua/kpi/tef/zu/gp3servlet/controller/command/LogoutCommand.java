package ua.kpi.tef.zu.gp3servlet.controller.command;

import ua.kpi.tef.zu.gp3servlet.controller.security.UserSecurity;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton Domin on 2020-04-14
 */
public class LogoutCommand implements Command {
	@Override
	public String execute(HttpServletRequest request) {
		UserSecurity.removeLoggedUser(request.getSession());
		return "redirect:?logout";
	}
}
