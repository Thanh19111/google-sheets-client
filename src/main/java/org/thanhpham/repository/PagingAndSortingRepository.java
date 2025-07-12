package org.thanhpham.repository;

import org.thanhpham.entity.Page;
import org.thanhpham.entity.Pageable;
import org.thanhpham.entity.Sort;

import java.io.IOException;
import java.util.List;

public interface PagingAndSortingRepository<P, T> {
    Page<P> findAll(Pageable pageable) throws IOException;
    List<P> findAll(Sort sort) throws IOException;
}
