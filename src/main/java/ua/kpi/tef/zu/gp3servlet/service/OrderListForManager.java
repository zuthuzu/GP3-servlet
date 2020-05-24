package ua.kpi.tef.zu.gp3servlet.service;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.entity.User;
import ua.kpi.tef.zu.gp3servlet.entity.WorkOrder;
import ua.kpi.tef.zu.gp3servlet.entity.states.OrderStatus;
import ua.kpi.tef.zu.gp3servlet.repository.DaoFactory;
import ua.kpi.tef.zu.gp3servlet.repository.OrderDao;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Anton Domin on 2020-05-24
 */
@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class OrderListForManager implements OrderListCommand {
	@Override
	public List<WorkOrder> getActiveOrders(User initiator) throws DatabaseException {
		List<OrderStatus> filter;
		try (OrderDao dao = DaoFactory.getInstance().createOrderDao()) {
			filter = Arrays.asList(OrderStatus.PENDING);
			List<WorkOrder> orders = dao.findByStatusIn(filter);

			filter = Arrays.asList(OrderStatus.READY);
			orders.addAll(dao.findByManagerAndStatusIn(initiator.getLogin(), filter));

			return orders;
		} catch (DatabaseException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException("Failed to connect to database", e);
		}
	}

	@Override
	public List<WorkOrder> getSecondaryOrders(User initiator) throws DatabaseException {
		List<OrderStatus> filter;
		try (OrderDao dao = DaoFactory.getInstance().createOrderDao()) {
			filter = Arrays.asList(OrderStatus.ACCEPTED, OrderStatus.WORKING);
			return dao.findByManagerAndStatusIn(initiator.getLogin(), filter);
		} catch (DatabaseException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException("Failed to connect to database", e);
		}
	}
}
