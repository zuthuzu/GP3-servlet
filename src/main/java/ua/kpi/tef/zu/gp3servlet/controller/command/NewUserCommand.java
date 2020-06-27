package ua.kpi.tef.zu.gp3servlet.controller.command;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.controller.MappingUtility;
import ua.kpi.tef.zu.gp3servlet.controller.RegistrationValidation;
import ua.kpi.tef.zu.gp3servlet.entity.User;
import ua.kpi.tef.zu.gp3servlet.service.ServiceFactory;
import ua.kpi.tef.zu.gp3servlet.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Created by Anton Domin on 2020-04-24
 */
public class NewUserCommand implements Command {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(NewUserCommand.class);
	private final UserService userService;

	public NewUserCommand() {
		userService = ServiceFactory.getUserService();
	}

	public NewUserCommand(UserService userService) {
		this.userService = userService;
	}

	@Override
	public String execute(HttpServletRequest request) {
		User user = getUserFromRequest(request);
		log.debug("New user details from front-end: " + user);

		if (!verifyUserFields(user)) {
			log.error("Malformed data in entity: " + user);
			request.getSession().setAttribute(MappingUtility.REJECTED_ENTITY, user);
			return MappingUtility.REDIRECT + MappingUtility.C_REG + "?" + MappingUtility.PARAM_GENERIC_ERROR;
		}

		try {
			userService.saveNewUser(user);
			return MappingUtility.getRedirectToDefault(null) + "?" + MappingUtility.PARAM_REG_OK;
		} catch (DatabaseException e) {
			log.error(e.getMessage());
			request.getSession().setAttribute(MappingUtility.REJECTED_ENTITY, user);
			return MappingUtility.REDIRECT + MappingUtility.C_REG + "?" +
					(e.isDuplicate() ? MappingUtility.PARAM_DUPLICATE_DATA : MappingUtility.PARAM_GENERIC_ERROR);
		}
	}

	private boolean verifyUserFields(User user) {
		return user.getName().matches(RegistrationValidation.NAME_REGEX) &&
				user.getLogin().matches(RegistrationValidation.LOGIN_REGEX) &&
				user.getPhone().matches(RegistrationValidation.PHONE_REGEX) &&
				(user.getEmail().isEmpty() || user.getEmail().matches(RegistrationValidation.EMAIL_REGEX));
	}

	private User getUserFromRequest(HttpServletRequest request) {
		return User.builder()
				.login(getStringFromRequest(request, MappingUtility.PARAM_USER_LOGIN))
				.name(getStringFromRequest(request, MappingUtility.PARAM_USER_NAME))
				.email(getStringFromRequest(request, MappingUtility.PARAM_USER_EMAIL))
				.phone(getStringFromRequest(request, MappingUtility.PARAM_USER_PHONE))
				.password(getStringFromRequest(request, MappingUtility.PARAM_USER_PASSWORD))
				.build();
	}

	private String getStringFromRequest(HttpServletRequest request, String param) {
		return Optional.ofNullable(request.getParameter(param)).orElse("");
	}
}
