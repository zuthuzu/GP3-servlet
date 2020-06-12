package ua.kpi.tef.zu.gp3servlet.repository.impl;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.entity.WorkOrder;
import ua.kpi.tef.zu.gp3servlet.repository.UtilityDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by Anton Domin on 2020-06-10
 */
public class JDBCUtilityDao implements UtilityDao {
	private final Connection connection;
	private final JDBCOrderDao orderDao;
	private final JDBCOrderDao archiveDao;

	public JDBCUtilityDao(Connection connection, JDBCOrderDao orderDao, JDBCOrderDao archiveDao) {
		this.connection = connection;
		this.orderDao = orderDao;
		this.archiveDao = archiveDao;
	}

	@Override
	public void archiveOrder(WorkOrder order) throws DatabaseException {
		try {
			connection.setAutoCommit(false);
			try {
				archiveDao.insert(order);
				orderDao.delete(order.getId());
			} catch (Exception e) {
				connection.rollback();
				connection.setAutoCommit(true);
				throw e;
			}
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			throw new DatabaseException("Failed to archive the order: " + e.getMessage(), e);
		}
	}

	@Override
	public void close() throws Exception {
		connection.close();
	}
}
