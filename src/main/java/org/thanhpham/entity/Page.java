package org.thanhpham.entity;

import java.util.List;
import java.util.function.Function;

public interface Page<T> extends Iterable<T> {
    int getTotalPages();
    long getTotalElements();

    <S> Page<S> map(Function<? super T, ? extends S> converter);

    int getNumber();
    int getSize();
    int getNumberOfElements();
    List<T> getContent();
    boolean hasContent();

    boolean isFirst();
    boolean isLast();
    boolean hasNext();
    boolean hasPrevious();
    Pageable getPageable();
}
