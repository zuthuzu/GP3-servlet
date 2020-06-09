package ua.kpi.tef.zu.gp3servlet.controller.command;

import org.apache.commons.lang3.EnumUtils;
import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.controller.MappingUtility;
import ua.kpi.tef.zu.gp3servlet.controller.security.UserSecurity;
import ua.kpi.tef.zu.gp3servlet.entity.RoleType;
import ua.kpi.tef.zu.gp3servlet.service.UserService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton Domin on 2020-05-25
 */
public class UserRoleCommand implements Command {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserRoleCommand.class);
	private final UserService userService;

	public UserRoleCommand() {
		userService = new UserService();
	}

	public UserRoleCommand(UserService userService) {
		this.userService = userService;
	}

	@Override
	public String execute(HttpServletRequest request) {
		String login = request.getParameter("login");
		RoleType role = EnumUtils.getEnum(RoleType.class, request.getParameter("role"), null);

		if (login == null || role == null)
			return MappingUtility.getRedirectToDefault(UserSecurity.getCurrentRole(request.getSession())) + "?" +
					MappingUtility.PARAM_GENERIC_ERROR;

		try {
			userService.updateRole(login, role);
		} catch (DatabaseException | IllegalArgumentException e) {
			log.error(e.getMessage());
			return MappingUtility.getRedirectToDefault(UserSecurity.getCurrentRole(request.getSession())) + "?" +
					MappingUtility.PARAM_GENERIC_ERROR;
		}

		return MappingUtility.getRedirectToDefault(UserSecurity.getCurrentRole(request.getSession()));
	}
}
