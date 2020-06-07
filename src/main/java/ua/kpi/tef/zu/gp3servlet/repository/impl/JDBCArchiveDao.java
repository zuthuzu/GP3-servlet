package ua.kpi.tef.zu.gp3servlet.repository.impl;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.entity.ArchiveOrder;
import ua.kpi.tef.zu.gp3servlet.entity.ItemCategory;
import ua.kpi.tef.zu.gp3servlet.entity.WorkOrder;
import ua.kpi.tef.zu.gp3servlet.entity.states.OrderStatus;
import ua.kpi.tef.zu.gp3servlet.repository.OrderDao;

import java.sql.*;

/**
 * Created by Anton Domin on 2020-04-16
 */
public class JDBCArchiveDao extends JDBCOrderDao implements OrderDao  {
	private final static String TABLE = "archive";
	private final Connection connection;

	public JDBCArchiveDao(Connection connection) {
		super(connection);
		this.connection = connection;
	}

	@Override
	protected void insert(WorkOrder entity) throws DatabaseException {
		try (PreparedStatement ps = connection.prepareStatement
				("INSERT INTO `" + TABLE + "` (`id`, `author`, `category`, `complaint`, `item`," +
						"`manager`, `manager_comment`, `master`, `master_comment`, `status`, `price`, `creation_date`)" +
						" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
			ps.setLong(1, entity.getId());
			ps.setString(2, entity.getAuthor());
			ps.setString(3, entity.getCategory().name());
			ps.setString(4, entity.getComplaint());
			ps.setString(5, entity.getItem());
			ps.setString(6, entity.getManager());
			ps.setString(7, entity.getManagerComment());
			ps.setString(8, entity.getMaster());
			ps.setString(9, entity.getMasterComment());
			ps.setString(10, entity.getStatus().name());
			ps.setInt(11, entity.getPrice());
			ps.setDate(12, Date.valueOf(entity.getCreationDate()));
			ps.executeUpdate();
		} catch (Exception e) {
			DatabaseException dbe = new DatabaseException("Couldn't create an order: " + entity, e);
			if (e instanceof SQLIntegrityConstraintViolationException) dbe.setDuplicate(true);
			throw dbe;
		}
	}

	@Override
	public void update(WorkOrder entity) throws DatabaseException {
		ArchiveOrder archiveOrder;
		try {
			archiveOrder = (ArchiveOrder) entity;
		} catch (ClassCastException e) {
			throw new DatabaseException("Bad order downcast: " + entity);
		}

	}

	private WorkOrder extractFromResultSet(ResultSet rs) throws SQLException, IllegalArgumentException {
		//TODO archive impl
		return WorkOrder.builder()
				.id(rs.getLong("id"))
				.author(rs.getString("author"))
				.category(ItemCategory.valueOf(rs.getString("category")))
				.complaint(rs.getString("complaint"))
				.item(rs.getString("item"))
				.manager(rs.getString("manager"))
				.managerComment(rs.getString("manager_comment"))
				.master(rs.getString("master"))
				.masterComment(rs.getString("master_comment"))
				.price(rs.getInt("price"))
				.status(OrderStatus.valueOf(rs.getString("status")))
				.creationDate(rs.getDate("creation_date").toLocalDate())
				.build();
	}
}
