package org.thanhpham.component;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Reader {
    private final Sheets sheetsService;
    private final String spreadsheetId;
    private final String sheetName;

    public Reader(Sheets sheetsService, String spreadsheetId, String sheetName) {
        this.sheetsService = sheetsService;
        this.spreadsheetId = spreadsheetId;
        this.sheetName = sheetName;
    }

    public List<List<Object>> readSheet(String range) throws IOException {
        String fullRange = sheetName + "!" + range;
        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadsheetId, fullRange)
                .execute();
        if(response.getValues() == null){return new ArrayList<>();}
        return response.getValues();
    }
}
