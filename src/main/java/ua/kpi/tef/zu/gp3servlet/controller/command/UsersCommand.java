package ua.kpi.tef.zu.gp3servlet.controller.command;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton Domin on 2020-04-18
 */
public class UsersCommand implements Command {
	@Override
	public String execute(HttpServletRequest request) {
		return "/WEB-INF/users.jsp";
	}
}
