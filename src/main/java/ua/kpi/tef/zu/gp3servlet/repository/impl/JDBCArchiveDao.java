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
	protected String getTable() {
		return TABLE;
	}

	@Override
	public void update(WorkOrder entity) throws DatabaseException {
		ArchiveOrder archiveOrder;
		try {
			archiveOrder = (ArchiveOrder) entity;
		} catch (ClassCastException e) {
			throw new DatabaseException("Bad order downcast: " + entity);
		}

		try (PreparedStatement ps = connection.prepareStatement
				("UPDATE `" + getTable() + "` SET user_comment = ?, user_stars = ? WHERE id = ?")) {
			ps.setString(1, archiveOrder.getUserComment());
			ps.setInt(2, archiveOrder.getUserStars());
			ps.setLong(3, archiveOrder.getId());
			ps.executeUpdate();
		} catch (Exception e) {
			DatabaseException dbe = new DatabaseException("Couldn't update an order: " + entity, e);
			if (e instanceof SQLIntegrityConstraintViolationException) dbe.setDuplicate(true);
			throw dbe;
		}

	}

	@Override
	protected ArchiveOrder extractFromResultSet(ResultSet rs) throws SQLException, IllegalArgumentException {
		return ArchiveOrder.builder()
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
				.userComment(rs.getString("user_comment"))
				.userStars(rs.getInt("user_stars"))
				.build();
	}
}
