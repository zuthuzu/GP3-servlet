package ua.kpi.tef.zu.gp3servlet.repository;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.entity.WorkOrder;

/**
 * Created by Anton Domin on 2020-06-10
 */
public interface UtilityDao extends AutoCloseable {
	void archiveOrder(WorkOrder order) throws DatabaseException;
}
