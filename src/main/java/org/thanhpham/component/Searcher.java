package org.thanhpham.component;

import org.thanhpham.entity.Range;
import org.thanhpham.util.AddFormula;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Searcher {
    private final Reader reader;
    private final AddFormula addFormula;

    public Searcher(Reader reader, AddFormula addFormula) {
        this.reader = reader;
        this.addFormula = addFormula;
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

    public List<Object> finaById(String range, String column, String query) throws IOException {
        List<List<Object>> data = filterByKeyword(range, column, query);
        if (data == null || data.isEmpty()) {
            return new ArrayList<>();
        }

        if(data.size() != 1){
            throw new IllegalStateException("Expected 1 record with ID " + query + ", but found " + data.size());
        }

        return data.getFirst();
    }

    public List<List<Object>> finaAll(String range, String column, String query) throws IOException {
        List<List<Object>> data = filterByKeyword(range, column, query);

        if (data == null || data.isEmpty()) {
            return new ArrayList<>();
        }

        return data;
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

    public Integer match(String value, String range, Integer option) {
        String query = String.format("=MATCH(\"%s\"; %s; %d)", value, range, option);
        try {
            return Integer.valueOf(addFormula.getValue(query));
        } catch (Exception e){
            return -1;
        }
    }

    public Integer countRows(String value, String range) throws IOException {
        String query = String.format("=COUNTIFS(%s; \"%s\")", range, value);
        try{
            return Integer.parseInt(addFormula.getValue(query));
        }catch (Exception e){
            return 0;
        }
    }

    public List<List<Object>> filterByKeyword(String range, String column, String keyword) throws IOException {
        List<List<Object>> data = reader.readSheet(range);
        if (data == null || data.isEmpty()) {
            return new ArrayList<>();
        }

        int colIndex = column.charAt(0) - 'A';
        return data.stream()
                .filter(row -> row.size() > colIndex && row.get(colIndex) != null
                        && row.get(colIndex).toString().equals(keyword))
                .collect(Collectors.toList());
    }

    public List<List<Object>> filterIgnoreCase(String range, String column, String keyword) throws IOException {
        List<List<Object>> data = reader.readSheet(range);

        if (data == null || data.isEmpty()) {
            return new ArrayList<>();
        }

        int colIndex = column.charAt(0) - 'A';
        return data.stream()
                .filter(row -> row.size() > colIndex && row.get(colIndex) != null
                        && row.get(colIndex).toString().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Integer existById(String value, String range) throws IOException {
        return match(value, range, 0);
    }
}
