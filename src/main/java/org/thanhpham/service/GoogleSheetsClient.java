package org.thanhpham.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import org.thanhpham.component.*;
import org.thanhpham.util.AddFormula;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GoogleSheetsClient implements IGoogleSheetClient{
    private final Reader reader;
    private final Writer writer;
    private final Initializer initializer;
    private final Searcher searcher;
    private final AddFormula addFormula;
    private final Remover remover;

    public GoogleSheetsClient(Sheets sheetsService, String spreadsheetId, String sheetName) throws IOException {
        this.reader = new Reader(sheetsService, spreadsheetId, sheetName);
        this.writer = new Writer(sheetsService, spreadsheetId, sheetName);
        this.initializer = new Initializer(sheetsService, spreadsheetId, sheetName);
        this.addFormula = new AddFormula(sheetsService, spreadsheetId, sheetName);
        this.searcher = new Searcher(this.reader, this.addFormula);
        this.remover = new Remover(searcher, sheetsService, spreadsheetId);
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
    public UpdateValuesResponse updateRow(String rangeWithIndex, List<Object> values) throws IOException {
        return writer.updateRow(rangeWithIndex, values);
    }

    public UpdateValuesResponse updateRows(String rangeWithIndex, List<List<Object>> values) throws IOException {
        return writer.updateRows(rangeWithIndex, values);
    }

    public AppendValuesResponse appendRow(List<Object> data) throws IOException {
        return writer.appendRow(data);
    }

    public AppendValuesResponse appendRows(List<List<Object>> data) throws IOException {
        return writer.appendRows(data);
    }

    //---------------------------------remove---------------------------------//
    public void deleteById(Integer sheetId, String column, String id) throws IOException {
        remover.deleteById(sheetId, column, id);
    }

    public void deleteAll(Integer sheetId, String column, String keyword) throws IOException {
        remover.deleteAll(sheetId, column, keyword);
    }

    public void deleteRow(Integer sheetId, Integer index) throws IOException {
        remover.deleteRow(sheetId, index);
    }

    //---------------------------------search---------------------------------//

    public List<List<Object>> filterByKeyword(String range, String column, String keyword, boolean match) throws IOException {
        return searcher.filterByKeyword(range, column, keyword, match);
    }

    public List<Object> findById(String range, String column, String id, boolean match) throws IOException {
        return searcher.findById(range, column, id, match);
    }

    public List<List<Object>> findAll(String range, String column, String keyword, boolean match) throws IOException {
        return searcher.findAll(range, column, keyword, match);
    }

    public Integer match(String value, String range, String cell, Integer option) throws IOException {
        return searcher.match(value, range, cell, option);
    }

    public Integer existById (String value,String cell, String column) throws IOException {
        return searcher.existById(value, cell, column + ":" + column);
    }

    public Integer countRows(String value, String cell, String range) throws IOException {
        return searcher.countRows(value, cell, range);
    }

    public List<Map.Entry<Integer, List<Object>>> findRowsWithIndex(String range, String column, String keyword, boolean match, boolean findAll) throws IOException, InterruptedException {
        return searcher.findRowsWithIndex(range,column, keyword, match, findAll);
    }

    public List<Integer> findIndex(String column, String keyword, boolean match, boolean findAll) throws IOException {
        return searcher.findIndex(column, keyword, match, findAll);
    }

    //---------------------------------formula---------------------------------//
    public void clearValue(String cell) throws IOException {
        addFormula.clearValue(cell);
    }

    public void writeFormula(String cell, String formula) throws IOException {
        addFormula.writeFormula(cell, formula);
    }

    public String getValue(String formula, String cell) throws IOException {
        return addFormula.getValue(formula, cell);
    }
}
