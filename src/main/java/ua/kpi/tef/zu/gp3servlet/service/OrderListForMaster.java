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
public class OrderListForMaster implements OrderListCommand {
	private final DaoFactory daoFactory;

	public OrderListForMaster() {
		daoFactory = DaoFactory.getInstance();
	}

	public OrderListForMaster(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public List<WorkOrder> getActiveOrders(User initiator) throws DatabaseException {
		List<OrderStatus> filter;
		try (OrderDao dao = daoFactory.createOrderDao()) {
			filter = Arrays.asList(OrderStatus.ACCEPTED, OrderStatus.WORKING);
			return dao.findByMasterAndStatusIn(initiator.getLogin(), filter);
		} catch (DatabaseException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException("Failed to connect to database", e);
		}
	}

	@Override
	public List<WorkOrder> getSecondaryOrders(User initiator) throws DatabaseException {
		List<OrderStatus> filter;
		try (OrderDao dao = daoFactory.createOrderDao()) {
			filter = Arrays.asList(OrderStatus.ACCEPTED);
			return dao.findByStatusIn(filter);
		} catch (DatabaseException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException("Failed to connect to database", e);
		}
	}
}
