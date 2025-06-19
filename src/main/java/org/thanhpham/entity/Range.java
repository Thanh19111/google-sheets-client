package org.thanhpham.entity;

public class Range {
    private String startCell;
    private String endCell;

    public Range(String startCell, String endCell) {
        this.startCell = startCell;
        this.endCell = endCell;
    }

    public String getStartCell() {
        return startCell;
    }

    public String getEndCell() {
        return endCell;
    }

    public String getFullRange() {
        return startCell + ":" + endCell;
    }

    @Override
    public String toString() {
        return getFullRange();
    }
}
