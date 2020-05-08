import org.junit.Test;
import ua.kpi.tef.zu.gp3servlet.controller.RegistrationValidation;
import ua.kpi.tef.zu.gp3servlet.service.UserService;

import static org.junit.Assert.assertEquals;

/**
 * Created by Anton Domin on 2020-04-24
 */
public class UserServiceTest {
	private final UserService userService;

	public UserServiceTest() {
		this.userService = new UserService();
	}

	@Test
	public void phoneNumberCleaningTest() {
		assertEquals("invalid", cleanPhoneNumber("(050)+123 45 67"));
		assertEquals("invalid", cleanPhoneNumber("051234567"));
		assertEquals("invalid", cleanPhoneNumber("(050)1234567,"));
		assertEquals("0501234567", cleanPhoneNumber("0501234567"));
		assertEquals("0501234567", cleanPhoneNumber("(050) 123 45 67"));
		assertEquals("0501234567", userService.cleanPhoneNumber("050 123-45-67"));
	}

	private String cleanPhoneNumber(String phone) {
		return phone.matches(RegistrationValidation.PHONE_REGEX) ?
				userService.cleanPhoneNumber(phone) :
				"invalid";
	}
}
