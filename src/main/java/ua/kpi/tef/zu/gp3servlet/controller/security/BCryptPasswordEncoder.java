package ua.kpi.tef.zu.gp3servlet.controller.security;

import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;
import java.util.regex.Pattern;

/**
 * Simplified copy of Spring implementation
 */
public class BCryptPasswordEncoder implements PasswordEncoder {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BCryptPasswordEncoder.class);
	private final Pattern BCRYPT_PATTERN;
	private final int strength;
	private final SecureRandom random;

	public BCryptPasswordEncoder() {
		this(-1);
	}

	public BCryptPasswordEncoder(int strength) {
		this(strength, null);
	}

	public BCryptPasswordEncoder(int strength, SecureRandom random) {
		this.BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");
		if (strength == -1 || strength >= 4 && strength <= 31) {
			this.strength = strength == -1 ? 10 : strength;
			this.random = random;
		} else {
			throw new IllegalArgumentException("Bad strength");
		}
	}

	public String encode(CharSequence rawPassword) {
		String salt;
		if (this.random != null) {
			salt = BCrypt.gensalt(this.strength, this.random);
		} else {
			salt = BCrypt.gensalt(this.strength);
		}

		return BCrypt.hashpw(rawPassword.toString(), salt);
	}

	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (encodedPassword != null && encodedPassword.length() != 0) {
			if (!this.BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
				log.warn("Encoded password does not look like BCrypt");
				return false;
			} else {
				return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
			}
		} else {
			log.warn("Empty encoded password");
			return false;
		}
	}
}
