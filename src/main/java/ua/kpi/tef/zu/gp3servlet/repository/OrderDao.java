package ua.kpi.tef.zu.gp3servlet.repository;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.entity.WorkOrder;
import ua.kpi.tef.zu.gp3servlet.entity.states.OrderStatus;

import java.util.Collection;
import java.util.List;

/**
 * Created by Anton Domin on 2020-04-16
 */
public interface OrderDao extends GenericDao<WorkOrder> {
	List<WorkOrder> findByStatusIn(Collection<OrderStatus> statuses) throws DatabaseException;

	List<WorkOrder> findByAuthor(String author) throws DatabaseException;

	List<WorkOrder> findByAuthorAndStatusIn(String author, Collection<OrderStatus> statuses) throws DatabaseException;

	List<WorkOrder> findByManager(String manager) throws DatabaseException;

	List<WorkOrder> findByManagerAndStatusIn(String manager, Collection<OrderStatus> statuses) throws DatabaseException;

	List<WorkOrder> findByMaster(String master) throws DatabaseException;

	List<WorkOrder> findByMasterAndStatusIn(String master, Collection<OrderStatus> statuses) throws DatabaseException;
}
