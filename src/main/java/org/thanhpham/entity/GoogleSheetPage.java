package org.thanhpham.entity;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

public class GoogleSheetPage<T> implements Page<T>{
    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final Pageable pageable;

    public GoogleSheetPage(List<T> content, Pageable pageable, long totalElements) {
        this.content = content;
        this.pageNumber = pageable.getPageNumber();
        this.pageSize = pageable.getPageSize();
        this.totalElements = totalElements;
        this.pageable = pageable;
    }

    @Override
    public <S> Page<S> map(Function<? super T, ? extends S> converter) {
        List<S> mappedContent = (List<S>) this.getContent()
                .stream()
                .map(converter)
                .toList();

        return new GoogleSheetPage<>(mappedContent, this.getPageable(), this.getTotalElements());
    }

    @Override
    public int getNumber() {
        return pageNumber;
    }

    @Override
    public int getSize() {
        return pageSize;
    }

    @Override
    public int getNumberOfElements() {
        return content.size();
    }

    @Override
    public boolean hasContent() {
        return content.isEmpty();
    }

    @Override
    public Pageable getPageable() {
        return pageable;
    }

    @Override
    public List<T> getContent() {
        return content;
    }

    @Override
    public long getTotalElements() {
        return totalElements;
    }

    @Override
    public int getTotalPages() {
        return (int) Math.ceil((double) totalElements / pageSize);
    }

    @Override
    public boolean isFirst() {
        return pageNumber == 0;
    }

    @Override
    public boolean isLast() {
        return pageNumber >= getTotalPages() - 1;
    }

    @Override
    public boolean hasNext() {
        return pageNumber < getTotalPages() - 1;
    }

    @Override
    public boolean hasPrevious() {
        return pageNumber > 0;
    }

    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        Page.super.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return Page.super.spliterator();
    }
}
