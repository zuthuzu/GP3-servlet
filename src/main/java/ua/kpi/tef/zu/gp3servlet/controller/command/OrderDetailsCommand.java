package ua.kpi.tef.zu.gp3servlet.controller.command;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.controller.LocalizationUtility;
import ua.kpi.tef.zu.gp3servlet.controller.MappingUtility;
import ua.kpi.tef.zu.gp3servlet.controller.security.UserSecurity;
import ua.kpi.tef.zu.gp3servlet.dto.OrderDTO;
import ua.kpi.tef.zu.gp3servlet.entity.states.AbstractState;
import ua.kpi.tef.zu.gp3servlet.service.OrderService;
import ua.kpi.tef.zu.gp3servlet.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by Anton Domin on 2020-05-25
 */
public class OrderDetailsCommand implements Command {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderDetailsCommand.class);
	private final OrderService orderService;

	public OrderDetailsCommand() {
		orderService = ServiceFactory.getOrderService();
	}

	public OrderDetailsCommand(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	public String execute(HttpServletRequest request) {
		OrderDTO order;
		try {
			order = orderService.getOrderById(request.getParameter("id"));
		} catch (Exception e) {
			log.warn(e.getMessage());
			return MappingUtility.getRedirectToDefault(UserSecurity.getCurrentRole(request.getSession())) + "?"
					+ MappingUtility.PARAM_ACCESS_DENIED;
		}

		Locale locale = LocalizationUtility.determineLocale(request.getSession());
		LocalizationUtility.setLocalFields(order, locale);

		request.setAttribute(MappingUtility.PARAM_ORDER_CATEGORIES, LocalizationUtility.getLocalCategories(locale));
		setOrderAttributes(request, order);

		AbstractState state = order.getLiveState();
		request.setAttribute("submit", LocalizationUtility.getLocalizedText(state.getButtonText(), locale));
		request.setAttribute("archived", order.isArchived());

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
		request.setAttribute(MappingUtility.PARAM_ORDER_ID, order.getId());
		request.setAttribute(MappingUtility.PARAM_ORDER_ITEM, order.getItem());
		request.setAttribute(MappingUtility.PARAM_ORDER_CATEGORY, order.getCategory());
		request.setAttribute(MappingUtility.PARAM_ORDER_COMPLAINT, order.getComplaint());
		request.setAttribute(MappingUtility.PARAM_ORDER_DATE, order.getCreationDate());
		request.setAttribute(MappingUtility.PARAM_ORDER_STATUS, order.getStatus());
		request.setAttribute(MappingUtility.PARAM_ORDER_AUTHOR, order.getAuthor());
		request.setAttribute(MappingUtility.PARAM_ORDER_MANAGER, order.getManager());
		request.setAttribute(MappingUtility.PARAM_ORDER_MASTER, order.getMaster());
		request.setAttribute(MappingUtility.PARAM_ORDER_MANAGER_COMMENT, order.getManagerComment());
		request.setAttribute(MappingUtility.PARAM_ORDER_MASTER_COMMENT, order.getMasterComment());
		request.setAttribute(MappingUtility.PARAM_ORDER_PRICE, order.getPrice());
		request.setAttribute(MappingUtility.PARAM_ORDER_USER_RATING, order.getUserStars());
		request.setAttribute(MappingUtility.PARAM_ORDER_USER_COMMENT, order.getUserComment());
	}
}
