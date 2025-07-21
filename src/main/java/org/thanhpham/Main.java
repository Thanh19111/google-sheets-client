package org.thanhpham;

import com.google.api.services.sheets.v4.Sheets;
import org.thanhpham.config.GoogleSheetsConfig;
import org.thanhpham.service.GoogleSheetsClient;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Main {
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        System.out.println("Hello?, This is a Google Sheet Library! :D");
        // Khởi tạo GoogleSheetsConfig
        GoogleSheetsConfig config = new GoogleSheetsConfig("/credentials.json", "My App");
        Sheets sheetsService = config.createSheetsClient();

        // Khởi tạo GoogleSheetsClient
        String spreadsheetId = "1HQjrZaEN4ZrK8ekdM3XgRbhMEqbazRUwwrdPhm0j5_o";
        String sheetName = "Data2";
        GoogleSheetsClient client = new GoogleSheetsClient(sheetsService, spreadsheetId, sheetName);

        // Khởi tạo spreadsheet/sheet
        client.initialize();
        client.deleteById(client.getSheetId(), "A", "v4");
    }
}