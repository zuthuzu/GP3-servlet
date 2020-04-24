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

	public void saveNewUser(User frontUser) throws DatabaseException, IllegalArgumentException {
		if (!verifyUserFields(frontUser)) throw new IllegalArgumentException("Malformed data in entity: " + frontUser);

		PasswordEncoder encoder = new BCryptPasswordEncoder();

		User completeUser = User.builder()
				.login(frontUser.getLogin())
				.name(frontUser.getName())
				.role(RoleType.ROLE_USER)
				.phone(cleanPhoneNumber(frontUser.getPhone()))
				.email(frontUser.getEmail().isEmpty() ? frontUser.getLogin() + DEFAULT_EMAIL_DOMAIN : frontUser.getEmail())
				.password(encoder.encode(frontUser.getPassword()))
				.build();

		saveUser(completeUser);
		log.info("New user created: " + completeUser);
	}

	private void saveUser(User user) throws DatabaseException {
		throw new DatabaseException("Couldn't save a user: " + user);
		/*DaoFactory factory = DaoFactory.getInstance();
		UserDao dao = factory.createUserDao();
		try {
			dao.create(user);
		} catch (Exception e) {
			throw new DatabaseException("Couldn't save a user: " + user, e);
		}*/
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
