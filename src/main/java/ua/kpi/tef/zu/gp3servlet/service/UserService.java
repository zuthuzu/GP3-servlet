package ua.kpi.tef.zu.gp3servlet.service;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.controller.security.BCryptPasswordEncoder;
import ua.kpi.tef.zu.gp3servlet.controller.security.PasswordEncoder;
import ua.kpi.tef.zu.gp3servlet.entity.RoleType;
import ua.kpi.tef.zu.gp3servlet.entity.User;
import ua.kpi.tef.zu.gp3servlet.repository.DaoFactory;
import ua.kpi.tef.zu.gp3servlet.repository.UserDao;

import java.util.Arrays;

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

	public void saveNewUser(User frontUser) throws DatabaseException {
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
		//throw new DatabaseException("Saving is under construction, unable to save a user: " + user);
		DaoFactory factory = DaoFactory.getInstance();
		UserDao dao = factory.createUserDao();
		dao.create(user);
	}

	public String cleanPhoneNumber(String rawNumber) {
		return rawNumber.chars()
				.filter(Character::isDigit)
				.collect(StringBuilder::new,
						StringBuilder::appendCodePoint,
						StringBuilder::append)
				.toString();
	}
}
