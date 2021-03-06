package ua.kpi.tef.zu.gp3servlet.repository.impl;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.entity.ItemCategory;
import ua.kpi.tef.zu.gp3servlet.entity.WorkOrder;
import ua.kpi.tef.zu.gp3servlet.entity.states.OrderStatus;
import ua.kpi.tef.zu.gp3servlet.repository.OrderDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Anton Domin on 2020-04-16
 */
public class JDBCOrderDao implements OrderDao {
	private final static String TABLE = "orders";
	private final Connection connection;

	public JDBCOrderDao(Connection connection) {
		this.connection = connection;
	}

	protected String getTable() {
		return TABLE;
	}

	@Override
	public void create(WorkOrder entity) throws DatabaseException {
		entity.setId(JDBCSequenceTracker.getId(connection));
		insert(entity);
	}

	public void insert(WorkOrder entity) throws DatabaseException {
		try (PreparedStatement ps = connection.prepareStatement
				("INSERT INTO `" + getTable() + "` (`id`, `author`, `category`, `complaint`, `item`," +
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
	public Optional<WorkOrder> findById(long id) throws DatabaseException {
		WorkOrder entity = null;
		String query = "SELECT * FROM `" + getTable() + "` WHERE id=?";

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) entity = extractFromResultSet(rs);
		} catch (Exception e) {
			throw new DatabaseException("Couldn't select orders by ID=" + id + ": " + e.toString(), e);
		}
		return Optional.ofNullable(entity);
	}

	@Override
	public List<WorkOrder> findAll() {
		return new ArrayList<>();
	}

	@Override
	public void update(WorkOrder entity) throws DatabaseException {
		try (PreparedStatement ps = connection.prepareStatement
				("UPDATE `" + getTable() + "` SET category = ?, complaint = ?, item = ?, " +
						"manager = ?, manager_comment = ?, master = ?, master_comment = ?, status = ?, price = ? " +
						" WHERE id = ?")) {
			ps.setString(1, entity.getCategory().name());
			ps.setString(2, entity.getComplaint());
			ps.setString(3, entity.getItem());
			ps.setString(4, entity.getManager());
			ps.setString(5, entity.getManagerComment());
			ps.setString(6, entity.getMaster());
			ps.setString(7, entity.getMasterComment());
			ps.setString(8, entity.getStatus().name());
			ps.setInt(9, entity.getPrice());
			ps.setLong(10, entity.getId());
			ps.executeUpdate();
		} catch (Exception e) {
			DatabaseException dbe = new DatabaseException("Couldn't update an order: " + entity, e);
			if (e instanceof SQLIntegrityConstraintViolationException) dbe.setDuplicate(true);
			throw dbe;
		}
	}

	@Override
	public void delete(long id) throws DatabaseException {
		try (PreparedStatement ps = connection.prepareStatement("DELETE FROM `" + getTable() + "` WHERE id = ?")) {
			ps.setLong(1, id);
			ps.executeUpdate();
		} catch (Exception e) {
			throw new DatabaseException("Couldn't delete an order by ID=" + id, e);
		}
	}

	@Override
	public List<WorkOrder> findByStatusIn(Collection<OrderStatus> statuses) throws DatabaseException {
		List<WorkOrder> result = new ArrayList<>();
		String query = "SELECT * FROM `" + getTable() + "` WHERE status IN (?)";
		query = putCollectionInQuery(query, statuses);

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) result.add(extractFromResultSet(rs));
		} catch (Exception e) {
			throw new DatabaseException("Couldn't select orders by status: " + e.toString(), e);
		}
		return result;
	}

	@Override
	public List<WorkOrder> findByAuthor(String author) throws DatabaseException {
		List<WorkOrder> result = new ArrayList<>();
		String query = "SELECT * FROM `" + getTable() + "` WHERE author=?";

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, author);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) result.add(extractFromResultSet(rs));
		} catch (Exception e) {
			throw new DatabaseException("Couldn't select orders by author: " + e.toString(), e);
		}
		return result;
	}

	@Override
	public List<WorkOrder> findByAuthorAndStatusIn(String author, Collection<OrderStatus> statuses)
			throws DatabaseException {
		List<WorkOrder> result = new ArrayList<>();
		String query = "SELECT * FROM `" + getTable() + "` WHERE author=? AND status IN (?)";
		query = putCollectionInQuery(query, statuses);

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, author);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) result.add(extractFromResultSet(rs));
		} catch (Exception e) {
			throw new DatabaseException("Couldn't select orders by author and status: " + e.toString(), e);
		}
		return result;
	}

	@Override
	public List<WorkOrder> findByManager(String manager) {
		return new ArrayList<>();
	}

	@Override
	public List<WorkOrder> findByManagerAndStatusIn(String manager, Collection<OrderStatus> statuses)
			throws DatabaseException {
		List<WorkOrder> result = new ArrayList<>();
		String query = "SELECT * FROM `" + getTable() + "` WHERE manager=? AND status IN (?)";
		query = putCollectionInQuery(query, statuses);

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, manager);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) result.add(extractFromResultSet(rs));
		} catch (Exception e) {
			throw new DatabaseException("Couldn't select orders by manager and status: " + e.toString(), e);
		}
		return result;
	}

	@Override
	public List<WorkOrder> findByMaster(String master) {
		return new ArrayList<>();
	}

	@Override
	public List<WorkOrder> findByMasterAndStatusIn(String master, Collection<OrderStatus> statuses)
			throws DatabaseException {
		List<WorkOrder> result = new ArrayList<>();
		String query = "SELECT * FROM `" + getTable() + "` WHERE master=? AND status IN (?)";
		query = putCollectionInQuery(query, statuses);

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, master);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) result.add(extractFromResultSet(rs));
		} catch (Exception e) {
			throw new DatabaseException("Couldn't select orders by master and status: " + e.toString(), e);
		}
		return result;
	}

	private String putCollectionInQuery(String query, Collection<OrderStatus> statuses) {
		String filter = statuses.stream()
				.map(Enum::name)
				.collect(Collectors.joining("','", "('", "')"));
		return query.replace("(?)", filter);
	}

	protected WorkOrder extractFromResultSet(ResultSet rs) throws SQLException, IllegalArgumentException {
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

	@Override
	public void close() throws Exception {
		connection.close();
	}
}
