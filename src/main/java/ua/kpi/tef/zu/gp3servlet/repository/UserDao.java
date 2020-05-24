package ua.kpi.tef.zu.gp3servlet.repository;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by Anton Domin on 2020-04-16
 */
public interface UserDao extends GenericDao<User> {
	Optional<User> findByLogin(String login) throws DatabaseException;

	List<User> findByLoginIn(Collection<String> logins) throws DatabaseException;
}
