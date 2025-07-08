package org.thanhpham.util;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class AddFormula {
    private final String spreadsheetId;
    private final String sheetName;
    private final Sheets sheetsService;

    public AddFormula(Sheets sheetsService, String spreadsheetId, String sheetName){
        this.sheetsService = sheetsService;
        this.sheetName = sheetName;
        this.spreadsheetId = spreadsheetId;
    }

    public String getValue(String formula, String cell) throws IOException {
        ValueRange body = new ValueRange()
                .setValues(List.of(List.of(formula)));
        sheetsService.spreadsheets().values()
                .update(spreadsheetId, sheetName + "!" + cell, body)
                .setValueInputOption("USER_ENTERED")
                .execute();
        ValueRange result = sheetsService.spreadsheets().values()
                .get(spreadsheetId, sheetName + "!" + cell)
                .execute();
        String resultValue = result.getValues().getFirst().getFirst().toString();
        this.clearValue(cell);
        return resultValue;
    }

    public void clearValue(String cell) throws IOException {
        sheetsService.spreadsheets().values()
                .clear(spreadsheetId, sheetName + "!" + cell, new ClearValuesRequest())
                .execute();
    }

    public void writeFormula(String cell, String formula) throws IOException {
            ValueRange body = new ValueRange()
                    .setValues(List.of(List.of(formula)));

            sheetsService.spreadsheets().values()
                    .update(spreadsheetId, sheetName + "!" + cell, body)
                    .setValueInputOption("USER_ENTERED")
                    .execute();
    }
}
