package org.thanhpham.repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CRUDRepository<P, T> {
    Optional<P> findById(T id) throws IOException;
    List<P> findAllById(Iterable<T> ids) throws IOException;
    P save(P entity) throws IOException;
    List<P> saveAll(Iterable<P> entities) throws IOException;
    boolean existsById(T id) throws IOException;
    void deleteById(T id) throws IOException;
    void delete(P entity) throws IOException;
    List<P> findAll(String column, String keyword, boolean match) throws IOException;
    List<P> findALl() throws IOException;
    boolean existAllById(Iterable<T> ids) throws IOException;
    void deleteAll(String column, String keyword) throws IOException;
    boolean existAllByColumn(Iterable<T> ids, String column) throws IOException;
}
