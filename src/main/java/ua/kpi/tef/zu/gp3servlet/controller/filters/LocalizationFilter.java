package ua.kpi.tef.zu.gp3servlet.controller.filters;

import ua.kpi.tef.zu.gp3servlet.controller.MappingUtility;
import ua.kpi.tef.zu.gp3servlet.controller.SupportedLanguages;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Anton Domin on 2020-04-16
 */
public class LocalizationFilter implements Filter {
	public static final String SUPPORTED_LANGUAGES = "supported";
	public static final String CURRENT_LANGUAGE = "locale";


	@Override
	public void init(FilterConfig filterConfig) {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		final HttpServletRequest req = (HttpServletRequest) servletRequest;
		HttpSession session = req.getSession();

		String langSwitch = req.getParameter(MappingUtility.PARAM_LOCALE_SWITCH);
		if (langSwitch == null) setDefaultsIfMissing(session);
		else setLocale(session, langSwitch);

		req.setAttribute(SUPPORTED_LANGUAGES, SupportedLanguages.values());

		filterChain.doFilter(servletRequest, servletResponse);
	}

	private void setDefaultsIfMissing(HttpSession session) {
		String langCode = (String) session.getAttribute(CURRENT_LANGUAGE);
		if (langCode == null) {
			langCode = SupportedLanguages.getDefault().getCode();
			setLocale(session, langCode);
		}
	}

	private void setLocale(HttpSession session, String langCode) {
		Locale locale = SupportedLanguages.determineLocale(langCode);
		session.setAttribute(CURRENT_LANGUAGE, locale.getLanguage());
	}

	@Override
	public void destroy() {

	}
}
