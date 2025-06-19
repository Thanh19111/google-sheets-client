package org.thanhpham.component;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import org.thanhpham.entity.Range;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Remover {
    private final Searcher searcher;
    private final Sheets sheetsService;
    private final String spreadsheetId;
    private final Pattern pattern = Pattern.compile("\\d+");

    public Remover(Searcher searcher, Sheets sheetsService, String sheetName, String spreadsheetId){
        this.searcher = searcher;
        this.sheetsService = sheetsService;
        this.spreadsheetId = spreadsheetId;
    }

    public void deleteById(Integer sheetId, String range, String query) throws IOException {
        List<Range> rs = searcher.findPosition(range, query);

        if(rs.size() != 1){
            throw new IllegalStateException("Expected 1 record with ID " + query + ", but found " + rs.size());
        }

        deleteRow(sheetId, rs.getFirst().getStartCell());
    }

    public void deleteAll(Integer sheetId, String range, String query) throws IOException {
        List<Range> arr = searcher.findPosition(range, query);

        if(arr != null){
            ListIterator<Range> it = arr.listIterator(arr.size());

            while (it.hasPrevious()) {
                deleteRow(sheetId, it.previous().getStartCell());
            }
        }
    }

    public void deleteRow(Integer sheetId, String range) throws IOException {
        Matcher matcher = pattern.matcher(range);

        if (matcher.find()) {
            int rowIndex = Integer.parseInt(matcher.group());
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
}
