package ua.kpi.tef.zu.gp3servlet.controller.command;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.service.ServiceFactory;
import ua.kpi.tef.zu.gp3servlet.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by Anton Domin on 2020-04-18
 */
public class UsersCommand implements Command {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UsersCommand.class);
	private final UserService userService;

	public UsersCommand() {
		userService = ServiceFactory.getUserService();
	}

	public UsersCommand(UserService userService) {
		this.userService = userService;
	}

	@Override
	public String execute(HttpServletRequest request) {
		try {
			request.setAttribute("users", userService.findAll());
		} catch (DatabaseException e) {
			log.error(e.getMessage());
			request.setAttribute("users", new ArrayList<>());
		}
		return "/WEB-INF/users.jsp";
	}
}
