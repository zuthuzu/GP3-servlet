package ua.kpi.tef.zu.gp3servlet.controller.filters;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by Anton Domin on 2020-04-14
 */
public class EncodingFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		servletResponse.setContentType("text/html");
		servletResponse.setCharacterEncoding("UTF-8");
		servletRequest.setCharacterEncoding("UTF-8");
		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {

	}
}
