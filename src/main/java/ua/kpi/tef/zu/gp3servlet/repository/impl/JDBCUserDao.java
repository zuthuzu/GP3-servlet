package ua.kpi.tef.zu.gp3servlet.repository.impl;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.entity.RoleType;
import ua.kpi.tef.zu.gp3servlet.entity.User;
import ua.kpi.tef.zu.gp3servlet.repository.UserDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Anton Domin on 2020-04-16
 */
public class JDBCUserDao implements UserDao {
	private final static String TABLE = "users";
	private final Connection connection;

	public JDBCUserDao(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void create(User entity) throws DatabaseException {
		entity.setId(JDBCSequenceTracker.getId(connection));
		insert(entity);
	}

	private void insert(User entity) throws DatabaseException {
		try (PreparedStatement ps = connection.prepareStatement
				("INSERT INTO `" + TABLE + "` (`id`, `login`, `name`, `role`, `email`, `phone`, `password`, " +
						"`account_non_expired`, `account_non_locked`, `credentials_non_expired`, `enabled`)" +
						" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
			ps.setInt(1, entity.getId());
			ps.setString(2, entity.getLogin());
			ps.setString(3, entity.getName());
			ps.setString(4, entity.getRole().toString());
			ps.setString(5, entity.getEmail());
			ps.setString(6, entity.getPhone());
			ps.setString(7, entity.getPassword());
			ps.setBoolean(8, true);
			ps.setBoolean(9, true);
			ps.setBoolean(10, true);
			ps.setBoolean(11, true);
			ps.executeUpdate();
		} catch (Exception e) {
			DatabaseException dbe = new DatabaseException("Couldn't save a user: " + entity, e);
			if (e instanceof SQLIntegrityConstraintViolationException) dbe.setDuplicate(true);
			throw dbe;
		}
	}

	@Override
	public Optional<User> findById(int id) {
		return Optional.empty();
	}

	@Override
	public Optional<User> findByLogin(String login) {
		User entity = null;
		try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM `" + TABLE + "` WHERE login=?")) {
			ps.setString(1, login);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				entity = extractFromResultSet(rs);
			}
		} catch (Exception ignored) {

		}
		return Optional.ofNullable(entity);
	}

	@Override
	public List<User> findAll() {
		return new ArrayList<>();
	}

	private User extractFromResultSet(ResultSet rs) throws SQLException {
		return User.builder()
				.id(rs.getInt("id"))
				.login(rs.getString("login"))
				.name(rs.getString("name"))
				.role(RoleType.valueOf(rs.getString("role")))
				.email(rs.getString("email"))
				.phone(rs.getString("phone"))
				.password(rs.getString("password"))
				.build();
	}

	@Override
	public void update(User entity) {

	}

	@Override
	public void delete(int id) {

	}

	@Override
	public void close() throws Exception {
		try {
			connection.close();
		} catch (SQLException e) {
			throw new Exception(e);
		}
	}
}
