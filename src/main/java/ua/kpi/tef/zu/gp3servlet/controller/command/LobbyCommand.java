package ua.kpi.tef.zu.gp3servlet.controller.command;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.controller.SupportedLanguages;
import ua.kpi.tef.zu.gp3servlet.controller.security.UserSecurity;
import ua.kpi.tef.zu.gp3servlet.entity.User;
import ua.kpi.tef.zu.gp3servlet.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Anton Domin on 2020-04-14
 */
public class LobbyCommand implements Command {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LobbyCommand.class);
	private final OrderService orderService;

	public LobbyCommand() {
		orderService = new OrderService();
	}

	public LobbyCommand(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	public String execute(HttpServletRequest request) {
		User currentUser = UserSecurity.getCurrentUser(request.getSession());
		Locale locale = SupportedLanguages.determineLocale(request.getSession());

		try {
			request.setAttribute("activeOrders", orderService.getActiveOrders(currentUser, locale));
			request.setAttribute("secondaryOrders", orderService.getSecondaryOrders(currentUser, locale));
		} catch (DatabaseException e) {
			log.error(e.getMessage());
			request.setAttribute("activeOrders", new ArrayList<>());
			request.setAttribute("secondaryOrders", new ArrayList<>());
		}
		return "/WEB-INF/lobby.jsp";
	}
}
