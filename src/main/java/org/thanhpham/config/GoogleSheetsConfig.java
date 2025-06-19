package org.thanhpham.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleSheetsConfig {
    private final String credentialsFilePath;
    private final String applicationName;

    public GoogleSheetsConfig(String credentialsFilePath, String applicationName) {
        this.credentialsFilePath = credentialsFilePath;
        this.applicationName = applicationName;
    }

    public Sheets createSheetsClient() throws IOException, GeneralSecurityException {
        InputStream credentialsStream = getClass().getResourceAsStream(credentialsFilePath);

        if (credentialsStream == null) {
            throw new IOException("Credentials file not found: " + credentialsFilePath);
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream)
                .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        return new Sheets.Builder(httpTransport, jsonFactory, new HttpCredentialsAdapter(credentials))
                .setApplicationName(applicationName)
                .build();
    }
}
