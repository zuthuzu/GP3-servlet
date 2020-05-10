package ua.kpi.tef.zu.gp3servlet.repository.impl;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Anton Domin on 2020-05-10
 */
public class ConnectionPoolHolder {
	private static final String PROPERTIES = "db.properties";
	private static final String DB_URL = "db.url";
	private static final String DB_USER = "db.user";
	private static final String DB_PWD = "db.password";
	private static final String DB_IDLE_MIN = "db.idle.min";
	private static final String DB_IDLE_MAX = "db.idle.max";
	private static final String DB_PREP_MAX = "db.prep.max";

	private static volatile DataSource dataSource;

	public static DataSource getDataSource(){
		if (dataSource == null){
			synchronized (ConnectionPoolHolder.class) {
				if (dataSource == null) {
					BasicDataSource ds = new BasicDataSource();
					setProperties(ds);
					dataSource = ds;
				}
			}
		}
		return dataSource;
	}

	private static void setProperties(BasicDataSource ds) {
		try (InputStream input = ConnectionPoolHolder.class.getClassLoader().getResourceAsStream(PROPERTIES)) {
			Properties prop = new Properties();
			prop.load(input);
			ds.setUrl(prop.getProperty(DB_URL));
			ds.setUsername(prop.getProperty(DB_USER));
			ds.setPassword(prop.getProperty(DB_PWD));
			ds.setMinIdle(getIntProperty(prop, DB_IDLE_MIN, 5));
			ds.setMaxIdle(getIntProperty(prop, DB_IDLE_MAX, 10));
			ds.setMaxOpenPreparedStatements(getIntProperty(prop, DB_PREP_MAX, 100));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static int getIntProperty(Properties prop, String name, int defaultValue) {
		try {
			return Integer.parseInt(prop.getProperty(name));
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
}
