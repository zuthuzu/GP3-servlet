package ua.kpi.tef.zu.gp3servlet.controller.command;

import ua.kpi.tef.zu.gp3servlet.controller.MappingUtility;
import ua.kpi.tef.zu.gp3servlet.controller.security.UserSecurity;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton Domin on 2020-05-25
 */
public class UpdateOrderCommand implements Command {
	@Override
	public String execute(HttpServletRequest request) {
		return MappingUtility.getRedirectToDefault(UserSecurity.getCurrentRole(request.getSession()));
	}
}
