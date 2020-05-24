package ua.kpi.tef.zu.gp3servlet.service;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.entity.User;
import ua.kpi.tef.zu.gp3servlet.entity.WorkOrder;

import java.util.List;

/**
 * Created by Anton Domin on 2020-05-24
 */
public interface OrderListCommand {
	List<WorkOrder> getActiveOrders(User initiator) throws DatabaseException;

	List<WorkOrder> getSecondaryOrders(User initiator) throws DatabaseException;
}
