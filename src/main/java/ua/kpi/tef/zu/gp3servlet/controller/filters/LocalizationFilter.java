package ua.kpi.tef.zu.gp3servlet.controller.filters;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class LocalizationFilter implements Filter {
	private static final String BUNDLE_NAME = "messages";

	@Override
	public void init(FilterConfig filterConfig) {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		final HttpServletRequest req = (HttpServletRequest) servletRequest;
		HttpSession session = req.getSession();

		String langSwitch = req.getParameter("l");
		if (langSwitch == null) setDefaultsIfMissing(session);
		else setLocale(session, langSwitch);

		req.setAttribute("supported", SupportedLanguages.values());

		filterChain.doFilter(servletRequest, servletResponse);
	}

	private void setDefaultsIfMissing(HttpSession session) {
		String langCode = (String) session.getAttribute("langCode");
		ResourceBundle bundle = (ResourceBundle) session.getAttribute("bundle");
		if (langCode == null || bundle == null) {
			langCode = SupportedLanguages.getDefault().getCode();
			setLocale(session, langCode);
		}
	}

	private void setLocale(HttpSession session, String langCode) {
		Locale locale = SupportedLanguages.determineLocale(langCode);
		session.setAttribute("langCode", locale.getLanguage());
		session.setAttribute("bundle", ResourceBundle.getBundle(BUNDLE_NAME, locale));
	}

	@Override
	public void destroy() {

	}
}
