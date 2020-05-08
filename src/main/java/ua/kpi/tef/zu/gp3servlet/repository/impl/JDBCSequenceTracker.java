package ua.kpi.tef.zu.gp3servlet.repository.impl;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Anton Domin on 2020-04-24
 */
public class JDBCSequenceTracker {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JDBCSequenceTracker.class);
	private static final String TABLE = "hibernate_sequence";
	private static final String COLUMN = "next_val";

	public static int getId(Connection connection) throws DatabaseException {
		int id = 0;
		try (Statement ps = connection.createStatement()) {
			connection.setAutoCommit(false);

			ResultSet rs = ps.executeQuery("SELECT "+ COLUMN + " FROM " + TABLE);
			if (rs.next()) {
				id = rs.getInt(COLUMN);
				log.debug("Got ID from sequence table: " + id);
				ps.execute("UPDATE " + TABLE + " SET " + COLUMN + "=" + (id+1) + "");
			}

			connection.setAutoCommit(true);
		} catch (SQLException e) {
			throw new DatabaseException("Couldn't get ID from the sequence table", e);
		}
		return id;
	}
}
