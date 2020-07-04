package ua.kpi.tef.zu.gp3servlet.service;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.controller.security.BCryptPasswordEncoder;
import ua.kpi.tef.zu.gp3servlet.controller.security.PasswordEncoder;
import ua.kpi.tef.zu.gp3servlet.entity.RoleType;
import ua.kpi.tef.zu.gp3servlet.entity.User;
import ua.kpi.tef.zu.gp3servlet.repository.DaoFactory;
import ua.kpi.tef.zu.gp3servlet.repository.UserDao;

import java.util.List;

/**
 * Created by Anton Domin on 2020-04-16
 */
public class UserService {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserService.class);
	private static final String SYSADMIN = "admin";
	private static final String DEFAULT_EMAIL_DOMAIN = "@null";

	private final DaoFactory daoFactory;

	public UserService() {
		daoFactory = DaoFactory.getInstance();
	}

	public UserService(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public User findByLogin(String login) throws DatabaseException {
		try (UserDao dao = daoFactory.createUserDao()) {
			return dao.findByLogin(login).orElseThrow(() -> new DatabaseException("User not found: " + login));
		} catch (DatabaseException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException("Failed to connect to database", e);
		}
	}

	public List<User> findAll() throws DatabaseException {
		try (UserDao dao = daoFactory.createUserDao()) {
			return dao.findAll();
		} catch (DatabaseException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException("Failed to connect to database", e);
		}
	}

	public void updateRole(String login, RoleType role) throws DatabaseException, IllegalArgumentException {
		if (login.equals(SYSADMIN)) throw new IllegalArgumentException("Attempt to modify a protected user");
		try (UserDao dao = daoFactory.createUserDao()) {
			User user = dao.findByLogin(login).orElseThrow(() -> new DatabaseException("User not found: " + login));
			if (user.getRole() == role) return;
			user.setRole(role);
			dao.update(user);
			log.info("User " + user.getLogin() + " updated successfully. Role is now " + user.getRole());
		} catch (DatabaseException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException("Failed to connect to database", e);
		}
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

		createUser(completeUser);
		log.info("New user created: " + completeUser);
	}

	private void createUser(User user) throws DatabaseException {
		try (UserDao dao = daoFactory.createUserDao()) {
			dao.create(user);
		} catch (DatabaseException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException("Failed to connect to database", e);
		}
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
