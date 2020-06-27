package ua.kpi.tef.zu.gp3servlet.service;

/**
 * Created by Anton Domin on 2020-06-13
 */
public class ServiceFactory {
	private static OrderService orderServiceInstance;
	private static UserService userServiceInstance;

	public static OrderService getOrderService() {
		if (orderServiceInstance == null) {
			synchronized (ServiceFactory.class) {
				if (orderServiceInstance == null) {
					orderServiceInstance = new OrderService();
				}
			}
		}
		return orderServiceInstance;
	}

	public static UserService getUserService() {
		if (userServiceInstance == null) {
			synchronized (ServiceFactory.class) {
				if (userServiceInstance == null) {
					userServiceInstance = new UserService();
				}
			}
		}
		return userServiceInstance;
	}
}
