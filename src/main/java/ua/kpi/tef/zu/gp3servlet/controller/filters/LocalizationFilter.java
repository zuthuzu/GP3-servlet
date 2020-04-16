package ua.kpi.tef.zu.gp3servlet.controller.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Anton Domin on 2020-04-16
 */
public class LocalizationFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig)  {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		final HttpServletRequest req = (HttpServletRequest) servletRequest;
		HttpSession session = req.getSession();

		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {

	}
}
