package iths.theroom.dao;

import java.util.List;
import java.util.Optional;

/*
https://www.baeldung.com/java-dao-pattern
 */
public interface Dao<T> {

    Optional<T> findOne(Long id);

    List<T> getAll();

    void save(T t);

    void delete(T t);
}
