package org.thanhpham.component;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;

import java.io.IOException;
import java.util.List;

public class Counter {
    private final Sheets sheetsService;
    private final String spreadsheetId;
    private final String sheetName;

    public Counter(Sheets sheetsService, String spreadsheetId, String sheetName) {
        this.sheetsService = sheetsService;
        this.spreadsheetId = spreadsheetId;
        this.sheetName = sheetName;
    }

    public void fetchSheetSize() throws IOException {
        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadsheetId, sheetName)
                .execute();

        List<List<Object>> rows = response.getValues();

        int rowCount = 0;
        int maxColumnCount = 0;

        if (rows != null) {
            for (List<Object> row : rows) {
                boolean hasData = row.stream().anyMatch(cell -> cell != null && !cell.toString().isBlank());
                if (hasData) {
                    rowCount++;
                    maxColumnCount = Math.max(maxColumnCount, row.size());
                }
            }
        }

        System.out.println("Số hàng có dữ liệu: " + rowCount);
        System.out.println("Số cột tối đa có dữ liệu: " + maxColumnCount);
    }
}
