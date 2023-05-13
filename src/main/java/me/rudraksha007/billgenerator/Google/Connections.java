package me.rudraksha007.billgenerator.Google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.Request;
import me.rudraksha007.billgenerator.Main;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.List;

public class Connections {

    protected static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    public static final String TOKENS_DIRECTORY_PATH = "tokens";
    public static final List<String> SCOPES = List.of(SheetsScopes.DRIVE_FILE, SheetsScopes.SPREADSHEETS);
    protected static final String CREDENTIALS_FILE_PATH =
            Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()+"/credentials.json";
    public static final String APPLICATION_NAME = "Bill Generator";
    protected static NetHttpTransport HTTP_TRANSPORT = null;
    public static Sheets service = null;
    public static Drive drive = null;
    public static String SheetId;

    public  void initialize(){
        System.out.println("Starting System!");
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Credential credential = getCredentials(HTTP_TRANSPORT);
            service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
            drive = new Drive.Builder(HTTP_TRANSPORT,JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();

        }
        catch (GeneralSecurityException | IOException e) {e.printStackTrace();}
    }

    public Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) {
        try {
            InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                    .setAccessType("offline")
                    .build();
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
            return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("sheets utils error when creating credentials");
        }
        return null;
    }

    public void sendRequests(List<Request> requests){
        BatchUpdateSpreadsheetRequest request = new BatchUpdateSpreadsheetRequest().setRequests(requests);
        try {
            service.spreadsheets().batchUpdate(SheetId, request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
