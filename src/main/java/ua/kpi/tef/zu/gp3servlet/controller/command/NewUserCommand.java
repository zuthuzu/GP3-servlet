package ua.kpi.tef.zu.gp3servlet.controller.command;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton Domin on 2020-04-18
 */
public class NewUserCommand implements Command {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(NewUserCommand.class);

	@Override
	public String execute(HttpServletRequest request) {
		log.debug("NEW USER REQUEST: " + request.getParameterMap());
		return "redirect:?reg";
	}
}
