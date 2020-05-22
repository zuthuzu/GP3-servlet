package ua.kpi.tef.zu.gp3servlet.controller;

import ua.kpi.tef.zu.gp3servlet.controller.command.*;
import ua.kpi.tef.zu.gp3servlet.entity.RoleType;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anton Domin on 2020-04-18
 */
public class MappingUtility {
	public static final String DOMAIN = "/repair"; //pom.xml: from tomcat7-maven-plugin configuration
	public static final String MAPPING = "/app"; //web.xml: everything is redirected here via MappingFilter
	public static final String REDIRECT = "redirect:";

	public static final String C_BLANK = "";
	public static final String C_INDEX = "index";
	public static final String C_REG = "reg";
	public static final String C_NEW_USER = "newuser";
	public static final String C_LOGIN = "login";
	public static final String C_LOGOUT = "logout";
	public static final String C_LOBBY = "lobby";
	public static final String C_USERS = "users";

	//session parameters
	public static final String REJECTED_ENTITY = "rejected";

	//request parameters
	public static final String PARAM_LOCALE_SWITCH = "l";
	public static final String PARAM_ACCESS_DENIED = "denied";
	public static final String PARAM_GENERIC_ERROR = "error";
	public static final String PARAM_DUPLICATE_DATA = "duplicate";
	public static final String PARAM_LOGOUT_OK = "logout";
	public static final String PARAM_REG_OK = "reg";
	public static final String PARAM_ORDER_OK = "success";

	//request parameters AND page attributes
	public static final String PARAM_USER_LOGIN = "login";
	public static final String PARAM_USER_NAME = "name";
	public static final String PARAM_USER_PASSWORD = "password";
	public static final String PARAM_USER_PHONE = "phone";
	public static final String PARAM_USER_EMAIL = "email";

	private static final String[] URL_JUNK_TOKENS = new String[]{
			"redirect:",
			".*" + DOMAIN,
			MAPPING,
			"/",
			"\\.jsp",
			"\\.html"
	};
	private static final String[] ALLOWED_URL_ROOT_TOKENS = new String[]{
			"index\\.jsp",
			".+\\.css",
			".+\\.jpg",
			".+\\.png"
	};

	private static final Map<RoleType, Set<String>> allowedCommands = new HashMap<>();

	static {
		allowedCommands.put(null, //GUEST
				new HashSet<>(Arrays.asList(C_BLANK, C_INDEX, C_REG, C_NEW_USER, C_LOGIN)));
		allowedCommands.put(RoleType.ROLE_USER,
				new HashSet<>(Arrays.asList(C_BLANK, C_INDEX, C_LOBBY, C_LOGOUT)));
		allowedCommands.put(RoleType.ROLE_MANAGER,
				new HashSet<>(Arrays.asList(C_BLANK, C_INDEX, C_LOBBY, C_LOGOUT)));
		allowedCommands.put(RoleType.ROLE_MASTER,
				new HashSet<>(Arrays.asList(C_BLANK, C_INDEX, C_LOBBY, C_LOGOUT)));
		allowedCommands.put(RoleType.ROLE_ADMIN,
				new HashSet<>(Arrays.asList(C_BLANK, C_INDEX, C_USERS, C_LOGOUT)));
	}

	public static Command getCommand(String path) {
		String commandName = digCommandFromURI(path);
		switch (commandName) {
			case C_INDEX:
				return new IndexCommand();
			case C_REG:
				return new RegistrationCommand();
			case C_NEW_USER:
				return new NewUserCommand();
			case C_LOGIN:
				return new LoginCommand();
			case C_LOGOUT:
				return new LogoutCommand();
			case C_LOBBY:
				return new LobbyCommand();
			case C_USERS:
				return new UsersCommand();
			//TODO proper error mapping, one way or another
			case "error":
				new ErrorCommand();
			default:
				return new IndexCommand();
		}
	}

	public static boolean canAccess(RoleType role, String path) {
		if (pathNotInServletDirectory(path)) return true; //if it wasn't remapped in filter, then it's CSS or some such
		String command = digCommandFromURI(path);
		Set<String> allowed = allowedCommands.get(role);
		return allowed.contains(command);
	}

	public static String getAccessDeniedPage(RoleType role) {
		return DOMAIN + MAPPING + "/" + getDefaultCommand(role) + "?" + PARAM_ACCESS_DENIED;
	}

	public static String getRedirectToDefault(RoleType role) {
		return REDIRECT + getDefaultCommand(role);
	}

	public static String getDefaultCommand(RoleType role) {
		return role == null ? "" : (role == RoleType.ROLE_ADMIN ? C_USERS : C_LOBBY);
	}

	public static boolean needsRemap(String path) {
		return pathNotInServletDirectory(path) &&
				Arrays.stream(ALLOWED_URL_ROOT_TOKENS)
						.filter(path::matches)
						.noneMatch(ut -> true);
	}

	public static boolean pathNotInServletDirectory(String path) {
		StringBuilder clearPath = new StringBuilder(path);
		stripToken(clearPath, DOMAIN);
		return clearPath.indexOf(MAPPING + "/") != 0;
	}

	public static String digCommandFromURI(String path) {
		StringBuilder result = new StringBuilder(path);
		Arrays.stream(URL_JUNK_TOKENS).forEach(jt -> stripToken(result, jt));
		return result.toString();
	}

	public static void stripToken(StringBuilder path, String token) {
		Matcher m = Pattern.compile(token).matcher(path);
		path.replace(0, path.length(), m.replaceFirst(""));
	}
}
