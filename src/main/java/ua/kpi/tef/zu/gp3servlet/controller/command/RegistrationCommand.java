package ua.kpi.tef.zu.gp3servlet.controller.command;

import ua.kpi.tef.zu.gp3servlet.controller.MappingUtility;
import ua.kpi.tef.zu.gp3servlet.controller.RegistrationValidation;
import ua.kpi.tef.zu.gp3servlet.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton Domin on 2020-04-14
 */
public class RegistrationCommand implements Command {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RegistrationCommand.class);

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
			request.setAttribute("name", user.getName());
			request.setAttribute("login", user.getLogin());
			request.setAttribute("phone", user.getPhone());
			request.setAttribute("email", user.getEmail());
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
