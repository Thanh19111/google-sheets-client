package org.thanhpham.component;

import org.thanhpham.util.AddFormula;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Searcher {
    private final Reader reader;
    private final AddFormula addFormula;

    public Searcher(Reader reader, AddFormula addFormula) {
        this.reader = reader;
        this.addFormula = addFormula;
    }

    public List<Object> findById(String range, String column, String keyword, boolean match) throws IOException {
        List<List<Object>> data = filterByKeyword(range, column, keyword, match);

        if(data.size() != 1){
            throw new IllegalStateException("Expected 1 record with ID " + keyword + ", but found " + data.size());
        }

        return data.getFirst();
    }

    public List<List<Object>> findAll(String range, String column, String keyword, boolean match) throws IOException {
        return filterByKeyword(range, column, keyword, match);
    }

    public Integer match(String value, String range, String cell, Integer option) {
        String query = String.format("=MATCH(\"%s\"; %s; %d)", value, range, option);

        try {
            return Integer.valueOf(addFormula.getValue(query,cell));
        } catch (Exception e){
            return -1;
        }
    }

    public Integer countRows(String value, String range, String cell) throws IOException {
        String query = String.format("=COUNTIFS(%s; \"%s\")", range, value);
        try{
            return Integer.parseInt(addFormula.getValue(query,cell));
        }catch (Exception e){
            return 0;
        }
    }

    public List<List<Object>> filterByKeyword(String range, String column, String keyword, boolean match) throws IOException {
        List<List<Object>> data = reader.readSheet(range);

        int colIndex = column.charAt(0) - 'A';
        if(match){
            return data.stream()
                    .filter(row -> row.size() > colIndex && row.get(colIndex) != null
                            && row.get(colIndex).toString().equals(keyword))
                    .collect(Collectors.toList());
        }
        return data.stream()
                .filter(row -> row.size() > colIndex && row.get(colIndex) != null
                        && row.get(colIndex).toString().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Integer existById(String value, String cell, String range) {
        return match(value, cell, range, 0);
    }

    public List<Map.Entry<Integer, List<Object>>> findRowsWithIndex(String range, String column, String keyword, boolean match, boolean findAll) throws IOException {
        List<Integer> arr = findIndex(column, keyword, match, findAll);
        List<Map.Entry<Integer, List<Object>>> results = new ArrayList<>();

        for (int index : arr) {
            String rowRange = range.split(":")[0].replaceAll("\\d+", "") + index + ":" + range.split(":")[1].replaceAll("\\d+", "") + index;
            List<List<Object>> rowData = reader.readSheet(rowRange);
            if (rowData != null && !rowData.isEmpty()) {
                results.add(Map.entry(index, rowData.getFirst()));
            }
        }

        return results;
    }

    public List<Integer> findIndex(String column, String keyword, boolean match, boolean findAll) throws IOException {
        List<List<Object>> columnData = reader.readSheet(column + ":" + column);
        List<Integer> results = new ArrayList<>();

        if(match){
            for (int i = 0; i < columnData.size(); i++) {
                List<Object> cell = columnData.get(i);
                if (!cell.isEmpty() && cell.getFirst() != null
                        && cell.getFirst().toString().equals(keyword)) {
                    results.add(i + 1);
                    if (!findAll) break;
                }
            }
        }else {
            for (int i = 0; i < columnData.size(); i++) {
                List<Object> cell = columnData.get(i);
                if (!cell.isEmpty() && cell.getFirst() != null
                        && cell.getFirst().toString().toLowerCase().contains(keyword.toLowerCase())) {
                    results.add(i + 1);
                    if (!findAll) break;
                }
            }
        }

        return results;
    }

}
