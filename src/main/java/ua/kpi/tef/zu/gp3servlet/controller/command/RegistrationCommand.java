package ua.kpi.tef.zu.gp3servlet.controller.command;

import ua.kpi.tef.zu.gp3servlet.controller.MappingUtility;
import ua.kpi.tef.zu.gp3servlet.controller.RegistrationValidation;
import ua.kpi.tef.zu.gp3servlet.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton Domin on 2020-04-14
 */
public class RegistrationCommand implements Command {
	@Override
	public String execute(HttpServletRequest request) {
		setUserAttributes(request);
		request.setAttribute("nameRegex", "^" + RegistrationValidation.NAME_REGEX + "$");
		request.setAttribute("loginRegex", "^" + RegistrationValidation.LOGIN_REGEX + "$");
		request.setAttribute("phoneRegex", "^" + RegistrationValidation.PHONE_REGEX + "$");
		return "/WEB-INF/reg.jsp";
	}

	private void setUserAttributes(HttpServletRequest request) {
		User user = getUserFromSession(request);
		if (user != null) {
			request.setAttribute(MappingUtility.PARAM_USER_LOGIN, user.getLogin());
			request.setAttribute(MappingUtility.PARAM_USER_NAME, user.getName());
			request.setAttribute(MappingUtility.PARAM_USER_PHONE, user.getPhone());
			request.setAttribute(MappingUtility.PARAM_USER_EMAIL, user.getEmail());
		}
	}

	private User getUserFromSession(HttpServletRequest request) {
		try {
			return (User) request.getSession().getAttribute(MappingUtility.REJECTED_ENTITY);
		} catch (ClassCastException e) {
			return null;
		}
	}
}
