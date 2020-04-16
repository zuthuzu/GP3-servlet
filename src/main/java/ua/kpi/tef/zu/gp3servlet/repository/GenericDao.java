package ua.kpi.tef.zu.gp3servlet.repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Anton Domin on 2020-04-16
 */
public interface GenericDao<T> extends AutoCloseable{
	void create (T entity);
	Optional<T> findById(int id);
	List<T> findAll();
	void update(T entity);
	void delete(int id);
}

