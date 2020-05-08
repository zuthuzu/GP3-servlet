package ua.kpi.tef.zu.gp3servlet.repository;

import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;

import java.util.List;
import java.util.Optional;

/**
 * Created by Anton Domin on 2020-04-16
 */
public interface GenericDao<T> extends AutoCloseable{
	void create (T entity) throws DatabaseException;
	Optional<T> findById(int id) throws DatabaseException;
	List<T> findAll() throws DatabaseException;
	void update(T entity) throws DatabaseException;
	void delete(int id) throws DatabaseException;
}

