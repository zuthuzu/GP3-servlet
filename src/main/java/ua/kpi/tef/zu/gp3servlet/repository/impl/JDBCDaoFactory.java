package ua.kpi.tef.zu.gp3servlet.repository.impl;

import ua.kpi.tef.zu.gp3servlet.repository.DaoFactory;
import ua.kpi.tef.zu.gp3servlet.repository.UserDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Anton Domin on 2020-04-16
 */
public class JDBCDaoFactory extends DaoFactory {
	private DataSource dataSource = ConnectionPoolHolder.getDataSource();

	@Override
	public UserDao createUserDao() {
		return new JDBCUserDao(getConnection());
	}

	private Connection getConnection() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}

