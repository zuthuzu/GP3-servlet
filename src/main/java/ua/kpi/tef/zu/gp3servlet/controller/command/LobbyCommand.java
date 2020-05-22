package ua.kpi.tef.zu.gp3servlet.controller.command;

import ua.kpi.tef.zu.gp3servlet.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

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
		request.setAttribute("activeOrders", new ArrayList<>());
		request.setAttribute("secondaryOrders", new ArrayList<>());
		return "/WEB-INF/lobby.jsp";
	}
}
