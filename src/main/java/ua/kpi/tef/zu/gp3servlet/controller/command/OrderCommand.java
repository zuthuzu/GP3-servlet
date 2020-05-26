package ua.kpi.tef.zu.gp3servlet.controller.command;

import ua.kpi.tef.zu.gp3servlet.controller.LocalizationUtility;
import ua.kpi.tef.zu.gp3servlet.controller.MappingUtility;
import ua.kpi.tef.zu.gp3servlet.dto.OrderDTO;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton Domin on 2020-05-25
 */
public class OrderCommand implements Command {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderCommand.class);

	@Override
	public String execute(HttpServletRequest request) {
		request.setAttribute(MappingUtility.PARAM_ORDER_CATEGORIES,
				LocalizationUtility.getLocalCategories(LocalizationUtility.determineLocale(request.getSession())));

		setOrderAttributes(request);

		return "/WEB-INF/order-new.jsp";
	}

	private void setOrderAttributes(HttpServletRequest request) {
		OrderDTO order = getOrderFromSession(request);
		if (order != null) {
			request.setAttribute(MappingUtility.PARAM_ORDER_ITEM, order.getItem());
			request.setAttribute(MappingUtility.PARAM_ORDER_CATEGORY, order.getCategory());
			request.setAttribute(MappingUtility.PARAM_ORDER_COMPLAINT, order.getComplaint());
		}
	}

	private OrderDTO getOrderFromSession(HttpServletRequest request) {
		try {
			return (OrderDTO) request.getSession().getAttribute(MappingUtility.REJECTED_ENTITY);
		} catch (ClassCastException e) {
			return null;
		}
	}
}
