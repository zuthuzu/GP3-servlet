package ua.kpi.tef.zu.gp3servlet.controller.filters;

import ua.kpi.tef.zu.gp3servlet.controller.MappingUtility;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Anton Domin on 2020-04-18
 */
public class MappingFilter implements Filter {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MappingFilter.class);

	@Override
	public void init(FilterConfig filterConfig) {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		final HttpServletRequest req = (HttpServletRequest) servletRequest;
		final HttpServletResponse resp = (HttpServletResponse) servletResponse;

		String path = req.getRequestURI();
		String query = req.getQueryString();
		//log.debug("MAP: entry at " + path + (query == null ? "" : "?" + query));

		if (MappingUtility.needsRemap(path)) {
			path = path.replace(MappingUtility.DOMAIN,
					MappingUtility.DOMAIN + MappingUtility.MAPPING) + (query == null ? "" : "?" + query);
			resp.sendRedirect(path);
			log.debug("MAP: remapped to " + path);
		} else {
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	@Override
	public void destroy() {

	}
}
