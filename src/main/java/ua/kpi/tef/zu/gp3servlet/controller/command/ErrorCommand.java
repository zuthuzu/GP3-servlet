package ua.kpi.tef.zu.gp3servlet.controller.command;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton Domin on 2020-04-14
 */
public class ErrorCommand implements Command {
	@Override
	public String execute(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		request.setAttribute("status_code", statusCode);
		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
		request.setAttribute("exception", String.valueOf(throwable));
		return "/WEB-INF/error.jsp";
	}
}
