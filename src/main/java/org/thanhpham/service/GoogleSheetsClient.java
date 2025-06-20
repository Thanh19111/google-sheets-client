package org.thanhpham.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import org.thanhpham.component.*;
import org.thanhpham.entity.Range;
import org.thanhpham.util.AddFormula;

import java.io.IOException;
import java.util.List;

public class GoogleSheetsClient {
    private final Reader reader;
    private final Writer writer;
    private final Initializer initializer;
    private final Searcher searcher;
    private final Counter counter;
    private final AddFormula addFormula;
    private final Remover remover;

    public GoogleSheetsClient(Sheets sheetsService, String spreadsheetId, String sheetName) throws IOException {
        this.reader = new Reader(sheetsService, spreadsheetId, sheetName);
        this.writer = new Writer(sheetsService, spreadsheetId, sheetName);
        this.initializer = new Initializer(sheetsService, spreadsheetId, sheetName);
        this.counter = new Counter(sheetsService, spreadsheetId, sheetName);
        this.addFormula = new AddFormula(sheetsService, spreadsheetId, sheetName);
        this.searcher = new Searcher(this.reader, this.addFormula);
        this.remover = new Remover(searcher, sheetsService, sheetName, spreadsheetId);
    }

    //---------------------------------init---------------------------------//
    public void initialize() throws IOException {
        initializer.initialize();
    }

    public Integer getSheetId() throws IOException {
        return initializer.getSheetId();
    }

    public String getSpreadsheetId() {
        return initializer.getSpreadsheetId();
    }

    //---------------------------------read---------------------------------//
    public List<List<Object>> readSheet(String range) throws IOException {
        return reader.readSheet(range);
    }

    //---------------------------------write---------------------------------//
    public UpdateValuesResponse writeSheet(String range, List<List<Object>> values) throws IOException {
        return writer.updateRow(range, values);
    }

    public void appendRow(List<Object> data) throws IOException {
        writer.appendRow(data);
    }

    //---------------------------------remove---------------------------------//
    public void deleteById(Integer sheetId, String column, String id) throws IOException {
        remover.deleteById(sheetId, column + ":" + column, id);
    }

    public void deleteAll(Integer sheetId, String column, String query) throws IOException {
        remover.deleteAll(sheetId, column + ":" + column, query);
    }

    public void deleteRow(Integer sheetId, String range) throws IOException {
        remover.deleteRow(sheetId, range);
    }

    //---------------------------------search---------------------------------//
    public List<List<Object>> search(String range, String query) throws IOException {
        return searcher.search(range, query);
    }

    public List<List<Object>> filterIgnoreCase(String range, String column, String keyword) throws IOException {
        return searcher.filterIgnoreCase(range, column, keyword);
    }

    public List<List<Object>> filterByKeyword(String range, String column, String keyword) throws IOException {
        return searcher.filterByKeyword(range, column, keyword);
    }

    public List<Object> findById(String range, String column, String id) throws IOException {
        return searcher.finaById(range, column, id);
    }

    public List<List<Object>> finaAll(String range, String column, String query) throws IOException {
        return searcher.finaAll(range, column , query);
    }

    public List<Range> findPosition(String column, String range) throws IOException {
        return searcher.findPosition(column + ":" + column, range);
    }

    public Integer match(String value, String range, Integer option) throws IOException {
        return searcher.match(value, range, option);
    }

    public Integer existById (String value, String column) throws IOException {
        return searcher.existById(value, column + ":" + column);
    }

    public Integer countRows(String value, String range) throws IOException {
        return searcher.countRows(value, range);
    }

    //---------------------------------formula---------------------------------//
    public void clearValue(String cell) throws IOException {
        addFormula.clearValue(cell);
    }

    public void writeFormula(String cell, String formula) throws IOException {
        addFormula.writeFormula(cell, formula);
    }

    public String getValue(String formula) throws IOException {
        return addFormula.getValue(formula);
    }

    //---------------------------------count---------------------------------//
    public void fetchSheetSize() throws IOException {
        counter.fetchSheetSize();
    }
}
