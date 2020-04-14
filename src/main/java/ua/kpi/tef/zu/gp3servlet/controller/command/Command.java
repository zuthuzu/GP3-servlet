package ua.kpi.tef.zu.gp3servlet.controller.command;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton Domin on 2020-04-14
 */
@FunctionalInterface
public interface Command {
	String execute(HttpServletRequest request);
}