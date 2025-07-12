package org.thanhpham.entity;

public class PageRequest implements Pageable{
    private final int page;
    private final int size;
    private final Sort sort;

    public PageRequest(int page, int size, Sort sort) {
        this.page = page;
        this.size = size;
        this.sort = sort;
    }

    public PageRequest(int page, int size) {
        this.page = page;
        this.size = size;
        this.sort = Sort.unsorted();
    }

    @Override
    public int getPageNumber() {
        return page;
    }

    @Override
    public int getPageSize() {
        return size;
    }

    @Override
    public long getOffset() {
        return (long) page * size;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public boolean isPaged() {
        return true;
    }

    @Override
    public Pageable next() {
        return new PageRequest(page + 1, size, sort);
    }

    @Override
    public Pageable previousOrFirst() {
        return page == 0 ? this : new PageRequest(page - 1, size, sort);
    }

    @Override
    public Pageable first() {
        return new PageRequest(0, size, sort);
    }

    @Override
    public boolean hasPrevious() {
        return page > 0;
    }

}
