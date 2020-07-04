import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.controller.RegistrationValidation;
import ua.kpi.tef.zu.gp3servlet.controller.security.BCryptPasswordEncoder;
import ua.kpi.tef.zu.gp3servlet.controller.security.PasswordEncoder;
import ua.kpi.tef.zu.gp3servlet.entity.RoleType;
import ua.kpi.tef.zu.gp3servlet.entity.User;
import ua.kpi.tef.zu.gp3servlet.repository.DaoFactory;
import ua.kpi.tef.zu.gp3servlet.repository.UserDao;
import ua.kpi.tef.zu.gp3servlet.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Anton Domin on 2020-04-24
 */
public class UserServiceTest {
	private final UserService userService;
	@Mock
	private DaoFactory daoFactory;
	@Mock
	private UserDao userDao;

	private List<User> allUsers = new ArrayList<>();
	private User admin;
	private User master;
	private User user;

	public UserServiceTest() throws DatabaseException {
		fillSampleUserList();
		MockitoAnnotations.initMocks(this);
		when(daoFactory.createUserDao()).thenReturn(userDao);
		when(userDao.findAll()).thenReturn(allUsers);
		when(userDao.findByLogin(any())).thenReturn(Optional.empty());
		when(userDao.findByLogin("admin")).thenReturn(Optional.of(admin));
		when(userDao.findByLogin("eus")).thenReturn(Optional.of(user));
		when(userDao.findByLogin("bcawl")).thenReturn(Optional.of(master));
		this.userService = new UserService(daoFactory);
	}

	@Test(expected = DatabaseException.class)
	public void findMissing() throws DatabaseException {
		userService.findByLogin("does-not-exist");
	}

	@Test
	public void findExisting() throws DatabaseException {
		assertEquals("Sysadmin", userService.findByLogin("admin").getName());
		assertEquals("Евлампий", userService.findByLogin("eus").getName());
		assertEquals("Belisarius", userService.findByLogin("bcawl").getName());
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateProtected() throws DatabaseException {
		userService.updateRole("admin", RoleType.ROLE_USER);
	}

	@Test(expected = DatabaseException.class)
	public void updateMissing() throws DatabaseException {
		userService.updateRole("does-not-exist", RoleType.ROLE_USER);
	}

	@Test
	public void phoneNumberCleaningTest() {
		assertEquals("invalid", cleanPhoneNumber("(050)+123 45 67"));
		assertEquals("invalid", cleanPhoneNumber("051234567"));
		assertEquals("invalid", cleanPhoneNumber("(050)1234567,"));
		assertEquals("0501234567", cleanPhoneNumber("0501234567"));
		assertEquals("0501234567", cleanPhoneNumber("(050) 123 45 67"));
		assertEquals("0501234567", cleanPhoneNumber("050 123-45-67"));
	}

	private String cleanPhoneNumber(String phone) {
		return phone.matches(RegistrationValidation.PHONE_REGEX) ?
				userService.cleanPhoneNumber(phone) :
				"invalid";
	}

	private void fillSampleUserList() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();

		admin = User.builder()
				.login("admin")
				.name("Sysadmin")
				.role(RoleType.ROLE_ADMIN)
				.phone("0000000000")
				.email("admin@null")
				.password(encoder.encode("admin"))
				.build();
		user = User.builder()
				.login("eus")
				.name("Евлампий")
				.role(RoleType.ROLE_USER)
				.phone("0501234568")
				.email("eus@null")
				.password(encoder.encode("eus"))
				.build();
		master = User.builder()
				.login("bcawl")
				.name("Belisarius")
				.role(RoleType.ROLE_MASTER)
				.phone("0110110111")
				.email("bcawl@null")
				.password(encoder.encode("omnissiah"))
				.build();

		allUsers.add(admin);
		allUsers.add(user);
		allUsers.add(master);
	}
}
