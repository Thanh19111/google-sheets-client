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
        this.searcher = new Searcher(this.reader);
        this.remover = new Remover(searcher, sheetsService, sheetName, spreadsheetId);
    }

    public void initialize() throws IOException {
        initializer.initialize();
    }

    public Integer getSheetId() throws IOException {
        return initializer.getSheetId();
    }

    public List<List<Object>> readSheet(String column, String row) throws IOException {
        return reader.readSheet(column + ":" + row);
    }

    public UpdateValuesResponse writeSheet(String column, String row, List<List<Object>> values) throws IOException {
        return writer.updateRow(column + ":" + row, values);
    }

    public List<List<Object>> search(String column, String row, String query) throws IOException {
        return searcher.search(column + ":" + row, query);
    }

    public void fetchSheetSize() throws IOException {
        counter.fetchSheetSize();
    }

    public String getSpreadsheetId() {
        return initializer.getSpreadsheetId();
    }

    public void appendRow(List<Object> data) throws IOException {
        writer.appendRow(data);
    }

    public List<Object> findById(String column, String id) throws IOException {
        return searcher.finaById(column + ":" + column, id);
    }

    public List<List<Object>> finaAll(String column, String query) throws IOException {
        return searcher.finaAll(column + ":" + column, query);
    }

    public Integer getColumnCount() throws IOException {
        return counter.getColumnCount();
    }

    public Integer getRowCount() throws IOException {
        return counter.getRowCount();
    }

    public List<Range> findPosition(String column, String range) throws IOException {
        return searcher.findPosition(column + ":" + column, range);
    }

    public void deleteById(Integer sheetId, String column, String id) throws IOException {
        remover.deleteById(sheetId, column + ":" + column, id);
    }

    public void deleteAll(Integer sheetId, String column, String query) throws IOException {
        remover.deleteAll(sheetId, column + ":" + column, query);
    }

    public void clearValue(String cell) throws IOException {
        addFormula.clearValue(cell);
    }

    public void writeFormula(String cell, String formula) throws IOException {
        addFormula.writeFormula(cell, formula);
    }

    public String getValue(String formula) throws IOException {
        return addFormula.getValue(formula);
    }
}
