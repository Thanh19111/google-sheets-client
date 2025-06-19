package org.thanhpham.component;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;

import java.io.IOException;
import java.util.List;

public class Initializer {
    private final Sheets sheetsService;
    private final String spreadsheetId;
    private final String sheetName;

    public Initializer(Sheets sheetsService, String spreadsheetId, String sheetName) throws IOException {
        this.sheetsService = sheetsService;
        this.spreadsheetId = spreadsheetId;
        this.sheetName = sheetName;
    }

    public void initialize() throws IOException {
        try {
            sheetsService.spreadsheets().get(spreadsheetId).execute();
            System.out.println("Spreadsheet already exists: " + spreadsheetId);
        } catch (IOException e) {
            if (e.getCause() instanceof GoogleJsonResponseException jsonException &&
                    jsonException.getStatusCode() == 404) {
                Spreadsheet spreadsheet = new Spreadsheet()
                        .setProperties(new SpreadsheetProperties().setTitle("My New Spreadsheet"));
                Spreadsheet created = sheetsService.spreadsheets().create(spreadsheet).execute();
                System.out.println("Created new spreadsheet: " + created.getSpreadsheetId());
            } else {
                throw new IOException("Failed to check spreadsheet: " + e.getMessage(), e);
            }
        }

        Spreadsheet spreadsheet = sheetsService.spreadsheets().get(spreadsheetId).execute();
        boolean sheetExists = spreadsheet.getSheets().stream()
                .anyMatch(sheet -> sheet.getProperties().getTitle().equals(sheetName));
        if (!sheetExists) {
            Request request = new Request()
                    .setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle(sheetName)));
            BatchUpdateSpreadsheetRequest batchRequest = new BatchUpdateSpreadsheetRequest()
                    .setRequests(List.of(request));
            sheetsService.spreadsheets().batchUpdate(spreadsheetId, batchRequest).execute();
            System.out.println("Created new sheet: " + sheetName);
        } else {
            System.out.println(sheetName + " already exists");
        }
    }

    public String getSpreadsheetId() {
        return spreadsheetId;
    }

    public Integer getSheetId() throws IOException {
        Spreadsheet spreadsheet = sheetsService.spreadsheets().get(spreadsheetId).execute();
        Sheet sheet = spreadsheet.getSheets().stream()
                .filter(s -> s.getProperties().getTitle().equals(sheetName))
                .findFirst()
                .orElseThrow(() -> new IOException("Sheet not found: " + sheetName));
        return sheet.getProperties().getSheetId();
    }
}
