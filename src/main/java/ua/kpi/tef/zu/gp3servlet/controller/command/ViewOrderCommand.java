package ua.kpi.tef.zu.gp3servlet.controller.command;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.controller.LocalizationUtility;
import ua.kpi.tef.zu.gp3servlet.controller.MappingUtility;
import ua.kpi.tef.zu.gp3servlet.controller.SupportedLanguages;
import ua.kpi.tef.zu.gp3servlet.controller.security.UserSecurity;
import ua.kpi.tef.zu.gp3servlet.dto.OrderDTO;
import ua.kpi.tef.zu.gp3servlet.entity.states.AbstractState;
import ua.kpi.tef.zu.gp3servlet.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by Anton Domin on 2020-05-25
 */
public class ViewOrderCommand implements Command {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ViewOrderCommand.class);
	private final OrderService orderService;

	public ViewOrderCommand() {
		orderService = new OrderService();
	}

	public ViewOrderCommand(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	public String execute(HttpServletRequest request) {
		Locale locale = SupportedLanguages.determineLocale(request.getSession());

		OrderDTO order;
		try {
			order = orderService.getOrderById(request.getParameter("id"));
			LocalizationUtility.setLocalFields(order, locale);
		} catch (Exception e) {
			log.warn(e.getMessage());
			return MappingUtility.getRedirectToDefault(UserSecurity.getCurrentRole(request.getSession())) + "?"
					+ MappingUtility.PARAM_ACCESS_DENIED;
		}

		request.setAttribute("categories", LocalizationUtility.getLocalCategories(locale));
		setOrderAttributes(request, order);

		AbstractState state = order.getLiveState();
		request.setAttribute("submit", LocalizationUtility.getLocalizedText(state.getButtonText(), locale));

		if (UserSecurity.getCurrentRole(request.getSession()) == order.getLiveState().getRequiredRole()) {
			request.setAttribute("available", state.getAvailableFields());
			request.setAttribute("proceed", true);
			request.setAttribute("cancel", state.isCancelable());
		} else {
			request.setAttribute("available", Collections.emptyList());
			request.setAttribute("proceed", false);
			request.setAttribute("cancel", false);
		}

		return "/WEB-INF/order-details.jsp";
	}

	private void setOrderAttributes(HttpServletRequest request, OrderDTO order) {
		request.setAttribute("category", order.getCategory());
		request.setAttribute("item", order.getItem());
		request.setAttribute("status", order.getStatus());
	}
}
