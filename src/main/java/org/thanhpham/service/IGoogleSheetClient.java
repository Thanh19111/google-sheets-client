package org.thanhpham.service;

import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IGoogleSheetClient {
    //---------------------------------init---------------------------------//
    public void initialize() throws IOException;

    public Integer getSheetId() throws IOException;

    public String getSpreadsheetId() ;

    //---------------------------------read---------------------------------//
    public List<List<Object>> readSheet(String range) throws IOException;

    //---------------------------------write---------------------------------//
    public UpdateValuesResponse updateRow(String range, List<Object> values) throws IOException;

    public UpdateValuesResponse updateRows(String range, List<List<Object>> values) throws IOException;

    public AppendValuesResponse appendRow(List<Object> data) throws IOException;

    public AppendValuesResponse appendRows(List<List<Object>> data) throws IOException;

    //---------------------------------remove---------------------------------//
    public void deleteById(Integer sheetId, String column, String id) throws IOException;

    public void deleteAll(Integer sheetId, String column, String keyword) throws IOException;

    public void deleteRow(Integer sheetId, Integer index) throws IOException;

    //---------------------------------search---------------------------------//

    public List<List<Object>> filterByKeyword(String range, String column, String keyword, boolean match) throws IOException;

    public List<Object> findById(String range, String column, String id, boolean match) throws IOException;

    public List<List<Object>> findAll(String range, String column, String keyword, boolean match) throws IOException;

    public Integer match(String value, String range, String cell, Integer option) throws IOException;

    public Integer existById (String value,String cell, String column) throws IOException;

    public Integer countRows(String value, String cell, String range) throws IOException;

    public List<Map.Entry<Integer, List<Object>>> findRowsWithIndex(String range, String column, String keyword, boolean match, boolean findAll) throws IOException, InterruptedException;
    //---------------------------------formula---------------------------------//
    public void clearValue(String cell) throws IOException;

    public void writeFormula(String cell, String formula) throws IOException;

    public String getValue(String formula, String cell) throws IOException;
}
