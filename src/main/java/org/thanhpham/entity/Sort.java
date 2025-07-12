package org.thanhpham.entity;

public class Sort {
    private final String property;
    private final boolean descending;

    private Sort(String property, boolean descending) {
        this.property = property;
        this.descending = descending;
    }

    public static Sort by(String property) {
        return new Sort(property, true);
    }

    public Sort ascending() {
        return new Sort(this.property, false);
    }

    public Sort descending() {
        return new Sort(this.property, true);
    }

    public static Sort unsorted() {
        return null;
    }

    public String getProperty() {
        return property;
    }

    public boolean isDescending() {
        return descending;
    }
}
