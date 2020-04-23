package ua.kpi.tef.zu.gp3servlet.controller.filters;

import ua.kpi.tef.zu.gp3servlet.controller.MappingUtility;
import ua.kpi.tef.zu.gp3servlet.controller.security.UserSecurity;
import ua.kpi.tef.zu.gp3servlet.entity.RoleType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Anton Domin on 2020-04-14
 */
public class AuthFilter implements Filter {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthFilter.class);

	@Override
	public void init(FilterConfig filterConfig) {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		final HttpServletRequest req = (HttpServletRequest) servletRequest;
		final HttpServletResponse resp = (HttpServletResponse) servletResponse;

		RoleType role = UserSecurity.getCurrentRole(req.getSession());
		if (MappingUtility.canAccess(role, req.getRequestURI())) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else {
			resp.sendRedirect(MappingUtility.getAccessDeniedPage(role));
			log.warn("AUTH: access denied at " + req.getRequestURI());
		}
	}

	@Override
	public void destroy() {

	}
}
