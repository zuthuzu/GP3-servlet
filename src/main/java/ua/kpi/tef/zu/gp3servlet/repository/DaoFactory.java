package ua.kpi.tef.zu.gp3servlet.repository;

import ua.kpi.tef.zu.gp3servlet.repository.impl.JDBCDaoFactory;

/**
 * Created by Anton Domin on 2020-04-16
 */
public abstract class DaoFactory {
	private static DaoFactory daoFactory;

	public abstract UserDao createUserDao();

	public abstract OrderDao createOrderDao();

	public abstract OrderDao createArchiveDao();

	public static DaoFactory getInstance(){
		if( daoFactory == null ){
			synchronized (DaoFactory.class){
				if(daoFactory==null){
					daoFactory = new JDBCDaoFactory();
				}
			}
		}
		return daoFactory;
	}
}
