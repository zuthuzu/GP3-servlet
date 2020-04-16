package ua.kpi.tef.zu.gp3servlet.service;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.entity.User;
import ua.kpi.tef.zu.gp3servlet.repository.DaoFactory;
import ua.kpi.tef.zu.gp3servlet.repository.UserDao;

/**
 * Created by Anton Domin on 2020-04-16
 */
public class UserService {
	public static User findByLogin(String login) throws DatabaseException {
		DaoFactory factory = DaoFactory.getInstance();
		UserDao dao = factory.createUserDao();
		return dao.findByLogin(login).orElseThrow(() -> new DatabaseException("User not found: " + login));
	}
}
