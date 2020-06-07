package ua.kpi.tef.zu.gp3servlet.controller.command;

import org.apache.commons.lang3.math.NumberUtils;
import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.controller.LocalizationUtility;
import ua.kpi.tef.zu.gp3servlet.controller.MappingUtility;
import ua.kpi.tef.zu.gp3servlet.controller.security.UserSecurity;
import ua.kpi.tef.zu.gp3servlet.dto.OrderDTO;
import ua.kpi.tef.zu.gp3servlet.service.OrderService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton Domin on 2020-05-25
 */
public class UpdateOrderCommand implements Command {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UpdateOrderCommand.class);
	private final OrderService orderService;

	public UpdateOrderCommand() {
		this.orderService = new OrderService();
	}

	public UpdateOrderCommand(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	public String execute(HttpServletRequest request) {
		OrderDTO order = getOrderFromRequest(request);
		log.info("Obtained order update request from front end: " + order.toStringSkipEmpty());

		try {
			orderService.updateOrder(order);
			return MappingUtility.getRedirectToDefault(UserSecurity.getCurrentRole(request.getSession())) + "?" +
					MappingUtility.PARAM_ORDER_OK;
		} catch (IllegalArgumentException | DatabaseException e) {
			log.error(e.getMessage());
			return MappingUtility.getRedirectToDefault(UserSecurity.getCurrentRole(request.getSession())) + "?" +
					MappingUtility.PARAM_GENERIC_ERROR;
		}
	}

	private OrderDTO getOrderFromRequest(HttpServletRequest request) throws IllegalArgumentException {
		OrderDTO order = OrderDTO.builder()
				.id(NumberUtils.toLong(request.getParameter(MappingUtility.PARAM_ORDER_ID), 0L))
				.item(request.getParameter(MappingUtility.PARAM_ORDER_ITEM))
				.category(request.getParameter(MappingUtility.PARAM_ORDER_CATEGORY))
				.complaint(request.getParameter(MappingUtility.PARAM_ORDER_COMPLAINT))
				.managerComment(request.getParameter(MappingUtility.PARAM_ORDER_MANAGER_COMMENT))
				.masterComment(request.getParameter(MappingUtility.PARAM_ORDER_MASTER_COMMENT))
				.price(NumberUtils.toInt(request.getParameter(MappingUtility.PARAM_ORDER_PRICE), 0))
				.userStars(NumberUtils.toInt(request.getParameter(MappingUtility.PARAM_ORDER_USER_RATING), 0))
				.userComment(request.getParameter(MappingUtility.PARAM_ORDER_USER_COMMENT))
				.initiator(UserSecurity.getCurrentUser(request.getSession()))
				.action(OrderDTO.ACTION_PROCEED)
				.build();

		LocalizationUtility.restoreCategoryFromLocalView(order, LocalizationUtility.determineLocale(request.getSession()));
		return order;
	}
}
