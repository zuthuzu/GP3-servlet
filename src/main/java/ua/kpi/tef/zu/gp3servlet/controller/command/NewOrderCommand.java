package ua.kpi.tef.zu.gp3servlet.controller.command;

import org.apache.commons.lang3.StringUtils;
import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.controller.LocalizationUtility;
import ua.kpi.tef.zu.gp3servlet.controller.MappingUtility;
import ua.kpi.tef.zu.gp3servlet.controller.security.UserSecurity;
import ua.kpi.tef.zu.gp3servlet.dto.OrderDTO;
import ua.kpi.tef.zu.gp3servlet.service.OrderService;
import ua.kpi.tef.zu.gp3servlet.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton Domin on 2020-05-25
 */
public class NewOrderCommand implements Command {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(NewOrderCommand.class);
	private final OrderService orderService;

	public NewOrderCommand() {
		orderService = ServiceFactory.getOrderService();
	}

	public NewOrderCommand(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	public String execute(HttpServletRequest request) {
		OrderDTO order = getOrderFromRequest(request);
		log.info("Obtained new order request from front end: " + order.toStringSkipEmpty());

		try {
			checkRequiredFields(order);
			orderService.saveNewOrder(order);
			return MappingUtility.getRedirectToDefault(UserSecurity.getCurrentRole(request.getSession()));
		} catch (IllegalArgumentException e) {
			log.warn("Malformed data in entity: " + order);
			request.getSession().setAttribute(MappingUtility.REJECTED_ENTITY, order);
			return MappingUtility.REDIRECT + MappingUtility.C_ORDER + "?" + MappingUtility.PARAM_GENERIC_ERROR;
		} catch (DatabaseException e) {
			log.error(e.getMessage());
			request.getSession().setAttribute(MappingUtility.REJECTED_ENTITY, order);
			return MappingUtility.REDIRECT + MappingUtility.C_ORDER + "?" + MappingUtility.PARAM_GENERIC_ERROR;
		}
	}

	private OrderDTO getOrderFromRequest(HttpServletRequest request) {
		OrderDTO order = OrderDTO.builder()
				.item(request.getParameter(MappingUtility.PARAM_ORDER_ITEM))
				.category(request.getParameter(MappingUtility.PARAM_ORDER_CATEGORY))
				.complaint(request.getParameter(MappingUtility.PARAM_ORDER_COMPLAINT))
				.author(UserSecurity.getCurrentUser(request.getSession()).getLogin())
				.action(OrderDTO.ACTION_PROCEED)
				.build();

		LocalizationUtility.restoreCategoryFromLocalView(order, LocalizationUtility.determineLocale(request.getSession()));
		return order;
	}

	private void checkRequiredFields(OrderDTO order) throws IllegalArgumentException {
		if (StringUtils.isBlank(order.getItem()))
			throw new IllegalArgumentException("Incomplete data: missing item in order request");
		if (StringUtils.isBlank(order.getComplaint()))
			throw new IllegalArgumentException("Incomplete data: missing complaint in order request");
		if (StringUtils.isBlank(order.getCategory()))
			throw new IllegalArgumentException("Incomplete data: missing category in order request");
	}
}
