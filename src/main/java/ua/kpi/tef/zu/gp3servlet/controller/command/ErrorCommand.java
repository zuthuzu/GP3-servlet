package ua.kpi.tef.zu.gp3servlet.controller.command;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton Domin on 2020-04-14
 */
public class ErrorCommand implements Command {
	@Override
	public String execute(HttpServletRequest request) {
		throw new RuntimeException("Generated exception");
	}
}
