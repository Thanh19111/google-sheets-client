package org.thanhpham.component;

import org.thanhpham.entity.Range;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Searcher {
    private final Reader reader;

    public Searcher(Reader reader) {
        this.reader = reader;
    }

    public List<List<Object>> search(String range, String query) throws IOException {
        List<List<Object>> data = reader.readSheet(range);

        if (data == null || data.isEmpty()) {
            return new ArrayList<>();
        }

        List<List<Object>> results = new ArrayList<>();
        for (List<Object> row : data) {
            for (Object cell : row) {
                if (cell != null && cell.toString().toLowerCase().contains(query.toLowerCase())) {
                    results.add(row);
                    break;
                }
            }
        }
        return results;
    }

    public List<Object> finaById(String range, String query) throws IOException {
        List<List<Object>> data = reader.readSheet(range);
        if (data == null || data.isEmpty()) {
            return new ArrayList<>();
        }

        if(data.size() != 1){
            throw new IllegalStateException("Expected 1 record with ID " + query + ", but found " + data.size());
        }

        List<Object> results = new ArrayList<>();
        for (List<Object> row : data) {
            for (Object cell : row) {
                if (cell != null && cell.toString().equals(query)) {
                    results = row;
                    break;
                }
            }
        }
        return results;
    }

    public List<List<Object>> finaAll(String range, String query) throws IOException {
        List<List<Object>> data = reader.readSheet(range);
        if (data == null || data.isEmpty()) {
            return new ArrayList<>();
        }

        List<List<Object>> results = new ArrayList<>();
        for (List<Object> row : data) {
            for (Object cell : row) {
                if (cell != null && cell.toString().equals(query)) {
                    results.add(row);
                }
            }
        }
        return results;
    }

    public List<Range> findPosition(String range, String query) throws IOException {
        List<List<Object>> data = reader.readSheet(range);
        List<Range> results = new ArrayList<>();

        if (data == null || data.isEmpty()) return results;

        for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
            List<Object> row = data.get(rowIndex);
            for (int colIndex = 0; colIndex < row.size(); colIndex++) {
                Object cell = row.get(colIndex);
                if (cell != null && cell.toString().equals(query)) {
                    String cellA1 = toNotation(rowIndex + 1, colIndex + 1);
                    results.add(new Range(cellA1, cellA1));
                }
            }
        }
        return results;
    }

    private String toNotation(int row, int col) {
        StringBuilder colName = new StringBuilder();

        while (col > 0) {
            int rem = (col - 1) % 26;
            colName.insert(0, (char) (rem + 'A'));
            col = (col - 1) / 26;
        }
        return colName.toString() + row;
    }
}
