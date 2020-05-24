package ua.kpi.tef.zu.gp3servlet.service;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.dto.OrderDTO;
import ua.kpi.tef.zu.gp3servlet.entity.ArchiveOrder;
import ua.kpi.tef.zu.gp3servlet.entity.RoleType;
import ua.kpi.tef.zu.gp3servlet.entity.User;
import ua.kpi.tef.zu.gp3servlet.entity.WorkOrder;
import ua.kpi.tef.zu.gp3servlet.entity.states.OrderStatus;
import ua.kpi.tef.zu.gp3servlet.entity.states.StateFactory;
import ua.kpi.tef.zu.gp3servlet.repository.DaoFactory;
import ua.kpi.tef.zu.gp3servlet.repository.OrderDao;
import ua.kpi.tef.zu.gp3servlet.repository.UserDao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

/**
 * Created by Anton Domin on 2020-05-20
 */
public class OrderService {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderService.class);
	private static final String BUNDLE_NAME = "messages";

	private final Map<RoleType, OrderListCommand> orderListMap = new HashMap<>();

	public OrderService() {
		orderListMap.put(RoleType.ROLE_USER, new OrderListForUser());
		orderListMap.put(RoleType.ROLE_MANAGER, new OrderListForManager());
		orderListMap.put(RoleType.ROLE_MASTER, new OrderListForMaster());
	}

	/**
	 * Returns so called "active orders", i.e. requiring primary attention from the user.
	 * <ul>
	 * <li>For masters it's orders they're working on (status.WORKING, master==themselves)
	 * <li>For managers it's orders they have to deal with (PENDING with no manager or READY with manager==themselves)
	 * <li>For users it's their orders that are still being repaired (any non-archived with author==themselves)
	 * </ul>
	 *
	 * @param initiator user who initiated the request
	 * @return list of the orders that satisfy the condition
	 */
	public List<OrderDTO> getActiveOrders(User initiator, Locale locale) throws DatabaseException {
		OrderListCommand command = orderListMap.get(initiator.getRole());
		if (command == null) return new ArrayList<>();
		else return setLocalFields(wrapWorkCollectionInDTO(command.getActiveOrders(initiator)), locale);
	}

	/**
	 * Returns so called "secondary orders", i.e. orders that user should care about less.
	 * <ul>
	 * <li>For masters it's accepted orders that still haven't been taken (status.ACCEPTED)
	 * <li>For managers it's their orders that are still being worked on (ACCEPTED or WORKING, manager==themselves)
	 * <li>For users it's their long term history (archived and cancelled)
	 * </ul>
	 *
	 * @param initiator user who initiated the request
	 * @return list of the orders that satisfy the condition
	 */
	public List<OrderDTO> getSecondaryOrders(User initiator, Locale locale) throws DatabaseException {
		OrderListCommand command = orderListMap.get(initiator.getRole());
		if (command == null) return new ArrayList<>();
		else return setLocalFields(wrapWorkCollectionInDTO(command.getSecondaryOrders(initiator)), locale);
	}

	private List<OrderDTO> wrapWorkCollectionInDTO(List<? extends WorkOrder> entities) {
		Map<String, String> userCache = getUserCache(entities);
		List<OrderDTO> result = new ArrayList<>();
		for (WorkOrder order : entities) {
			result.add(wrapOrderInDTO(order, userCache));
		}
		return result;
	}

	/**
	 * Without this cache wrapWorkCollectionInDTO would've performed 3 separate DB reads for each order
	 */
	private Map<String, String> getUserCache(List<? extends WorkOrder> entities) {
		Map<String, String> userCache = new HashMap<>();
		for (WorkOrder order : entities) {
			userCache.put(order.getAuthor(), "");
			userCache.put(order.getManager(), "");
			userCache.put(order.getMaster(), "");
		}

		try (UserDao dao = DaoFactory.getInstance().createUserDao()) {
			List<User> userList = dao.findByLoginIn(userCache.keySet());
			userList.forEach(u -> userCache.put(u.getLogin(), u.getName()));
		} catch (Exception ignored) {}
		return userCache;
	}

	private OrderDTO wrapOrderInDTO(WorkOrder order, Map<String, String> userCache) {
		OrderDTO result = OrderDTO.builder()
				.id(order.getId())
				.actualCreationDate(order.getCreationDate())
				.author(userCache.get(order.getAuthor()))
				.authorLogin(order.getAuthor())
				.manager(userCache.get(order.getManager()))
				.managerLogin(order.getManager())
				.master(userCache.get(order.getMaster()))
				.masterLogin(order.getMaster())
				.actualCategory(order.getCategory())
				.item(order.getItem())
				.complaint(order.getComplaint())
				.actualStatus(order.getStatus())
				.isArchived(order.getStatus().isArchived())
				.price(order.getPrice())
				.managerComment(order.getManagerComment())
				.masterComment(order.getMasterComment())
				.build();

		StateFactory.setState(result);
		if (result.isArchived()) {
			try {
				ArchiveOrder archiveOrder = (ArchiveOrder) order;
				result.setUserComment(archiveOrder.getUserComment());
				result.setUserStars(archiveOrder.getUserStars());
			} catch (ClassCastException e) {
				log.error("Bad order downcast: " + order);
			}
		}
		return result;
	}

	private WorkOrder unwrapNewOrder(OrderDTO order) {
		return WorkOrder.builder()
				.author(order.getAuthor())
				.category(order.getActualCategory())
				.item(order.getItem())
				.complaint(order.getComplaint())
				.status(OrderStatus.PENDING)
				.creationDate(LocalDate.now())
				.build();
	}

	private WorkOrder unwrapFullOrder(OrderDTO order) {
		WorkOrder result;
		if (order.getActualStatus().isArchived()) {
			result = new ArchiveOrder(order.getUserComment(), order.getUserStars());
		} else {
			result = new WorkOrder();
		}
		result.setId(order.getId());
		result.setCreationDate(order.getActualCreationDate());
		result.setAuthor(order.getAuthorLogin());
		result.setManager(order.getManagerLogin());
		result.setMaster(order.getMasterLogin());
		result.setStatus(order.getActualStatus());
		result.setCategory(order.getActualCategory());
		result.setItem(order.getItem());
		result.setComplaint(order.getComplaint());
		result.setPrice(order.getPrice());
		result.setManagerComment(order.getManagerComment());
		result.setMasterComment(order.getMasterComment());
		return result;
	}

	public List<OrderDTO> setLocalFields(List<OrderDTO> orders, Locale locale) {
		orders.forEach((o) -> setLocalFields(o, locale));
		return orders;
	}

	public void setLocalFields(OrderDTO order, Locale locale) {
		DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(locale);
		order.setCreationDate(order.getActualCreationDate().format(dtf));

		ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
		order.setCategory(getLocalizedText(bundle, order.getActualCategory().toString()));
		order.setStatus(getLocalizedText(bundle, order.getActualStatus().toString()));
	}

	private String getLocalizedText(ResourceBundle bundle, String token) {
		return bundle.keySet().contains(token) ? bundle.getString(token) : token;
	}
}
