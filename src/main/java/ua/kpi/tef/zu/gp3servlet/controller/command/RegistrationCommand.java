package ua.kpi.tef.zu.gp3servlet.controller.command;

import ua.kpi.tef.zu.gp3servlet.controller.RegistrationValidation;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton Domin on 2020-04-14
 */
public class RegistrationCommand implements Command {
	@Override
	public String execute(HttpServletRequest request) {
		request.setAttribute("nameRegex", "^" + RegistrationValidation.NAME_REGEX + "$");
		request.setAttribute("loginRegex", "^" + RegistrationValidation.LOGIN_REGEX + "$");
		request.setAttribute("phoneRegex", "^" + RegistrationValidation.PHONE_REGEX + "$");
		return "/WEB-INF/reg.jsp";
	}
}
