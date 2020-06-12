package ua.kpi.tef.zu.gp3servlet.repository.impl;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.math.NumberUtils;

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

	/** The only required properties
	 */
	private static void setProperties(BasicDataSource ds) {
		try (InputStream input = ConnectionPoolHolder.class.getClassLoader().getResourceAsStream(PROPERTIES)) {
			Properties prop = new Properties();
			prop.load(input);
			setMandatoryProperties(ds, prop);
			setAdditionalProperties(ds, prop);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void setMandatoryProperties(BasicDataSource ds, Properties prop) {
		ds.setUrl(prop.getProperty(DB_URL));
		ds.setUsername(prop.getProperty(DB_USER));
		ds.setPassword(prop.getProperty(DB_PWD));
	}

	private static void setAdditionalProperties(BasicDataSource ds, Properties prop) {
		ds.setMinIdle(NumberUtils.toInt(prop.getProperty(DB_IDLE_MIN), 1));
		ds.setMaxIdle(NumberUtils.toInt(prop.getProperty(DB_IDLE_MAX), 10));
		ds.setMaxOpenPreparedStatements(NumberUtils.toInt(prop.getProperty(DB_PREP_MAX), 100));
		ds.setAutoCommitOnReturn(true);
	}
}
