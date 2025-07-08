package org.thanhpham.component;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

public class Remover {
    private final Searcher searcher;
    private final Sheets sheetsService;
    private final String spreadsheetId;

    public Remover(Searcher searcher, Sheets sheetsService, String spreadsheetId){
        this.searcher = searcher;
        this.sheetsService = sheetsService;
        this.spreadsheetId = spreadsheetId;
    }

    public void deleteById(Integer sheetId, String column, String keyword) throws IOException {
        List<Integer> arr = searcher.findIndex(column, keyword, true, true);

        if(arr.size() != 1){
            throw new IllegalStateException("Expected 1 record with ID " + keyword + ", but found " + arr.size());
        }

        deleteRow(sheetId, arr.getFirst());
    }

    public void deleteAll(Integer sheetId, String column, String keyword) throws IOException {
        List<Integer> arr = searcher.findIndex(column, keyword, true, true);

        if(arr != null){
            ListIterator<Integer> it = arr.listIterator(arr.size());
            while (it.hasPrevious()) {
                deleteRow(sheetId, it.previous());
            }
        }
    }

    public void deleteRow(Integer sheetId, Integer rowIndex) throws IOException {
        Request deleteRowRequest = new Request().setDeleteDimension(
                new DeleteDimensionRequest()
                        .setRange(new DimensionRange()
                                .setSheetId(sheetId)
                                .setDimension("ROWS")
                                .setStartIndex(rowIndex - 1)
                                .setEndIndex(rowIndex))
        );

        BatchUpdateSpreadsheetRequest batchRequest = new BatchUpdateSpreadsheetRequest()
                .setRequests(List.of(deleteRowRequest));

        sheetsService.spreadsheets().batchUpdate(spreadsheetId, batchRequest).execute();
    }
}
