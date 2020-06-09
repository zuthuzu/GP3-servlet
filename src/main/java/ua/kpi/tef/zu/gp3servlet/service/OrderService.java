package ua.kpi.tef.zu.gp3servlet.service;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.controller.LocalizationUtility;
import ua.kpi.tef.zu.gp3servlet.dto.OrderDTO;
import ua.kpi.tef.zu.gp3servlet.entity.ArchiveOrder;
import ua.kpi.tef.zu.gp3servlet.entity.RoleType;
import ua.kpi.tef.zu.gp3servlet.entity.User;
import ua.kpi.tef.zu.gp3servlet.entity.WorkOrder;
import ua.kpi.tef.zu.gp3servlet.entity.states.AbstractState;
import ua.kpi.tef.zu.gp3servlet.entity.states.OrderStatus;
import ua.kpi.tef.zu.gp3servlet.entity.states.StateFactory;
import ua.kpi.tef.zu.gp3servlet.repository.DaoFactory;
import ua.kpi.tef.zu.gp3servlet.repository.OrderDao;
import ua.kpi.tef.zu.gp3servlet.repository.UserDao;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by Anton Domin on 2020-05-20
 */
@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class OrderService {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderService.class);

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
		//TODO denull
		if (command == null) return new ArrayList<>();
		else return LocalizationUtility.setLocalFields(
				wrapWorkCollectionInDTO(command.getActiveOrders(initiator)), locale);
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
		//TODO denull
		if (command == null) return new ArrayList<>();
		else return LocalizationUtility.setLocalFields(
				wrapWorkCollectionInDTO(command.getSecondaryOrders(initiator)), locale);
	}

	/**
	 * Primary logic that invokes the order state mechanism.<br />
	 * Preparation and support for state change.<br />
	 * Verifies data retrieved from front end, then passes it to the primary logic.
	 *
	 * @param modelOrder order the way it arrived from frontend, + initiator (user who calls it)
	 */
	public void updateOrder(OrderDTO modelOrder) throws DatabaseException, IllegalArgumentException {
		OrderDTO dbOrder = getOrderById(modelOrder.getId());
		AbstractState state = dbOrder.getLiveState();
		state.verifyRequest(modelOrder);
		state.processOrder(this, state.assembleOrder(dbOrder, modelOrder));
	}

	public OrderDTO getOrderById(String id) throws DatabaseException, IllegalArgumentException {
		long realID;
		try {
			realID = Long.parseLong(id);
		} catch (Exception e) {
			throw new IllegalArgumentException("Can't convert ID " + id + " to a number.");
		}
		return getOrderById(realID);
	}

	public OrderDTO getOrderById(long id) throws DatabaseException, IllegalArgumentException {
		WorkOrder order;
		try (OrderDao dao = DaoFactory.getInstance().createOrderDao()) {
			order = dao.findById(id).orElseThrow(() ->
					new IllegalArgumentException("Can't find order with ID " + id));
		} catch (IllegalArgumentException | DatabaseException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException("Failed to connect to database", e);
		}

		return wrapOrderInDTO(order, getUserCache(Arrays.asList(order)));
	}

	public void archiveOrder(OrderDTO order) throws DatabaseException {
		//transactions.archiveOrder(unwrapFullOrder(order));
		log.info("Order archived: " + order.toStringSkipEmpty());
	}

	public void saveNewOrder(OrderDTO order) throws DatabaseException {
		try (OrderDao dao = DaoFactory.getInstance().createOrderDao()) {
			dao.create(unwrapNewOrder(order));
		} catch (DatabaseException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException("Failed to connect to database", e);
		}
		log.info("New order created: " + order.toStringSkipEmpty());
	}

	public void saveExistingOrder(OrderDTO order) throws DatabaseException {
		saveOrder(unwrapFullOrder(order));
		log.info((order.getActualStatus().isArchived() ? "Archived order updated: " : "Order updated: ") +
				order.toStringSkipEmpty());
	}

	private void saveOrder(WorkOrder order) throws DatabaseException {
		OrderStatus status = order.getStatus();
		if (status.isArchived()) {
			ArchiveOrder archiveOrder;
			try {
				archiveOrder = (ArchiveOrder) order;
			} catch (ClassCastException e) {
				throw new DatabaseException("Bad order downcast: " + order);
			}

			try (OrderDao dao = DaoFactory.getInstance().createArchiveDao()) {
				dao.update(archiveOrder);
			} catch (DatabaseException e) {
				throw e;
			} catch (Exception e) {
				throw new DatabaseException("Failed to connect to database", e);
			}
		} else {
			try (OrderDao dao = DaoFactory.getInstance().createOrderDao()) {
				dao.update(order);
			} catch (DatabaseException e) {
				throw e;
			} catch (Exception e) {
				throw new DatabaseException("Failed to connect to database", e);
			}
		}
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

		//is this a good practice? maybe I should call UserService instead?
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
}
