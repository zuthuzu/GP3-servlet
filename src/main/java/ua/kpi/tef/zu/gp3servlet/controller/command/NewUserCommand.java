package ua.kpi.tef.zu.gp3servlet.controller.command;

import ua.kpi.tef.zu.gp3servlet.controller.MappingUtility;
import ua.kpi.tef.zu.gp3servlet.entity.User;
import ua.kpi.tef.zu.gp3servlet.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Created by Anton Domin on 2020-04-18
 */
public class NewUserCommand implements Command {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(NewUserCommand.class);

	private final UserService userService;

	public NewUserCommand() {
		userService = new UserService();
	}

	@Override
	public String execute(HttpServletRequest request) {
		User user = getUserFromRequest(request);
		log.debug("New user details from front-end: " + user);

		try {
			userService.saveNewUser(user);
			return MappingUtility.getRedirectToDefault(null) + "?" + MappingUtility.PARAM_REG_OK;
		} catch (Exception e) {
			//TODO 1. distinguish duplicate fields and other exs
			//TODO 2. retain object fields through redirect (via session?)
			log.error("EXCEPTION: " + e);
			return MappingUtility.REDIRECT + MappingUtility.C_REG + "?" + MappingUtility.PARAM_GENERIC_ERROR;
		}
	}

	private User getUserFromRequest(HttpServletRequest request) {
		return User.builder()
				.login(getStringFromRequest(request, "login"))
				.name(getStringFromRequest(request,"name"))
				.email(getStringFromRequest(request,"email"))
				.phone(getStringFromRequest(request,"phone"))
				.password(getStringFromRequest(request,"password"))
				.build();
	}

	private String getStringFromRequest(HttpServletRequest request, String param) {
		return Optional.ofNullable(request.getParameter(param)).orElse("");
	}
}
