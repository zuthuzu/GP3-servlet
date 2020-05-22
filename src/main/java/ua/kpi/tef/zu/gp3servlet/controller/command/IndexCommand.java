package ua.kpi.tef.zu.gp3servlet.controller.command;

import ua.kpi.tef.zu.gp3servlet.controller.MappingUtility;
import ua.kpi.tef.zu.gp3servlet.controller.security.UserSecurity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Anton Domin on 2020-04-18
 */
public class IndexCommand implements Command {
	@Override
	public String execute(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (UserSecurity.userLoggedIn(session))
			return MappingUtility.getRedirectToDefault(UserSecurity.getCurrentRole(session));
		else
			return "/WEB-INF/index.jsp";
	}
}
