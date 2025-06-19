package org.thanhpham.component;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.List;

public class Writer {
    private final Sheets sheetsService;
    private final String spreadsheetId;
    private final String sheetName;

    public Writer(Sheets sheetsService, String spreadsheetId, String sheetName) {
        this.sheetsService = sheetsService;
        this.spreadsheetId = spreadsheetId;
        this.sheetName = sheetName;
    }

    public UpdateValuesResponse updateRow(String range, List<List<Object>> values) throws IOException {
        String fullRange = sheetName + "!" + range;
        ValueRange body = new ValueRange().setValues(values);
        return sheetsService.spreadsheets().values()
                .update(spreadsheetId, fullRange, body)
                .setValueInputOption("RAW")
                .execute();
    }

    public void appendRow(List<Object> values) throws IOException {
        ValueRange body = new ValueRange()
                .setValues(List.of(values));

        sheetsService.spreadsheets().values()
                .append(spreadsheetId, sheetName + "!A1", body)
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .setIncludeValuesInResponse(true)
                .execute();
    }

}
