package ua.kpi.tef.zu.gp3servlet.controller.security;

/**
 * Created by Anton Domin on 2020-04-16
 */
public interface PasswordEncoder {
	String encode(CharSequence var1);

	boolean matches(CharSequence var1, String var2);
}