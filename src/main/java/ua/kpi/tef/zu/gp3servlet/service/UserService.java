package ua.kpi.tef.zu.gp3servlet.service;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.controller.RegistrationValidation;
import ua.kpi.tef.zu.gp3servlet.controller.security.BCryptPasswordEncoder;
import ua.kpi.tef.zu.gp3servlet.controller.security.PasswordEncoder;
import ua.kpi.tef.zu.gp3servlet.entity.RoleType;
import ua.kpi.tef.zu.gp3servlet.entity.User;
import ua.kpi.tef.zu.gp3servlet.repository.DaoFactory;
import ua.kpi.tef.zu.gp3servlet.repository.UserDao;

/**
 * Created by Anton Domin on 2020-04-16
 */
public class UserService {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserService.class);
	private static final String SYSADMIN = "admin";
	private static final String DEFAULT_EMAIL_DOMAIN = "@null";

	public User findByLogin(String login) throws DatabaseException {
		DaoFactory factory = DaoFactory.getInstance();
		UserDao dao = factory.createUserDao();
		return dao.findByLogin(login).orElseThrow(() -> new DatabaseException("User not found: " + login));
	}

	public void saveNewUser(User user) throws DatabaseException, IllegalArgumentException {
		if (!verifyUserFields(user)) throw new IllegalArgumentException("Malformed data in entity: " + user);

		PasswordEncoder encoder = new BCryptPasswordEncoder();

		user.setPhone(cleanPhoneNumber(user.getPhone()));
		user.setEmail(user.getEmail().isEmpty() ? user.getLogin() + DEFAULT_EMAIL_DOMAIN : user.getEmail());
		user.setPassword(encoder.encode(user.getPassword()));
		user.setRole(RoleType.ROLE_USER);

		saveUser(user);
		log.info("New user created: " + user);
	}

	private void saveUser(User user) throws DatabaseException {
		DaoFactory factory = DaoFactory.getInstance();
		UserDao dao = factory.createUserDao();
		try {
			dao.create(user);
		} catch (Exception e) {
			throw new DatabaseException("Couldn't save a user: " + user, e);
		}
	}

	private boolean verifyUserFields(User user) {
		return user.getName().matches(RegistrationValidation.NAME_REGEX) &&
				user.getLogin().matches(RegistrationValidation.LOGIN_REGEX) &&
				user.getPhone().matches(RegistrationValidation.PHONE_REGEX) &&
				(user.getEmail().isEmpty() || user.getEmail().matches(RegistrationValidation.EMAIL_REGEX));
	}

	private String cleanPhoneNumber(String rawNumber) {
		StringBuilder result = new StringBuilder();

		for (char n : rawNumber.toCharArray()) {
			if (Character.isDigit(n)) {
				result.append(n);
			}
		}

		return result.toString();
	}
}
