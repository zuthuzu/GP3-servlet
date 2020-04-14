package ua.kpi.tef.zu.gp3servlet.controller.filters;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Anton Domin on 2020-04-14
 */
@Slf4j
public class AuthFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		final HttpServletRequest req = (HttpServletRequest) servletRequest;

		HttpSession session = req.getSession();
		ServletContext context = req.getServletContext();
		log.info("AUTH: current session login: " + session.getAttribute("login") +
				", role: " + session.getAttribute("role") +
				", global context user pool: " + context.getAttribute("loggedUsers"));

		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {

	}
}
