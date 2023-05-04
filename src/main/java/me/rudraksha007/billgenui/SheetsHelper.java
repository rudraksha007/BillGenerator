package me.rudraksha007.billgenui;

import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import me.rudraksha007.billgenui.Google.Connections;
import me.rudraksha007.billgenui.Google.RequestMaker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static me.rudraksha007.billgenui.Google.Connections.service;

public class SheetsHelper {

    RequestMaker maker = new RequestMaker();
    public void craftStructure(){
        List<Request>requests = new ArrayList<>();
        requests.add(maker.createMergeRequest("MERGE_ALL", 12, 2,17,2));
        for (int i = 3; i < 7; i++) {
            requests.add(maker.createMergeRequest("MERGE_ALL", 1, i,17,i));
        }
        requests.add(maker.createMergeRequest("MERGE_ALL", 1, 8,17,9));

        requests.add(maker.createMergeRequest("MERGE_ALL", 1, 10,2,10));
        requests.add(maker.createMergeRequest("MERGE_ALL", 3, 10,9,10));
        requests.add(maker.createMergeRequest("MERGE_ALL", 10, 10,12,10));
        requests.add(maker.createMergeRequest("MERGE_ALL", 13, 10,17,10));

        requests.add(maker.createMergeRequest("MERGE_ALL", 1, 11,2,11));
        requests.add(maker.createMergeRequest("MERGE_ALL", 3, 11,9,11));
        requests.add(maker.createMergeRequest("MERGE_ALL", 10, 11,12,11));
        requests.add(maker.createMergeRequest("MERGE_ALL", 13, 11,17,11));

        requests.add(maker.createMergeRequest("MERGE_ALL", 1, 12,17,12));

        requests.add(maker.createMergeRequest("MERGE_ALL", 1, 13,9,13));
        requests.add(maker.createMergeRequest("MERGE_ALL", 10, 13,17,13));

        requests.add(maker.createMergeRequest("MERGE_ALL", 1, 14,2,14));
        requests.add(maker.createMergeRequest("MERGE_ALL", 3, 14,9,14));
        requests.add(maker.createMergeRequest("MERGE_ALL", 10, 14,11,14));
        requests.add(maker.createMergeRequest("MERGE_ALL", 12, 14,17,14));

        requests.add(maker.createMergeRequest("MERGE_ALL", 1, 15,2,16));
        requests.add(maker.createMergeRequest("MERGE_ALL", 3, 15,9,16));
        requests.add(maker.createMergeRequest("MERGE_ALL", 10, 15,11,16));
        requests.add(maker.createMergeRequest("MERGE_ALL", 12, 15,17,16));

        requests.add(maker.createMergeRequest("MERGE_ALL", 1, 17,2,17));
        requests.add(maker.createMergeRequest("MERGE_ALL", 3, 17,9,17));
        requests.add(maker.createMergeRequest("MERGE_ALL", 10, 17,11,17));
        requests.add(maker.createMergeRequest("MERGE_ALL", 12, 17,17,17));

        requests.add(maker.createMergeRequest("MERGE_ALL", 1, 18,2,18));
        requests.add(maker.createMergeRequest("MERGE_ALL", 3, 18,7,18));
        requests.add(maker.createMergeRequest("MERGE_ALL", 10, 18,11,18));
        requests.add(maker.createMergeRequest("MERGE_ALL", 12, 18,15,18));

        requests.add(maker.createMergeRequest("MERGE_ALL", 1, 19,3,19));
        requests.add(maker.createMergeRequest("MERGE_ALL", 4, 19,15,19));

        requests.add(maker.createMergeRequest("MERGE_ALL", 1, 20,17,20));

        requests.add(maker.createMergeRequest("MERGE_ALL", 2, 21,7,22));
        requests.add(maker.createMergeRequest("MERGE_ALL", 8, 21,10,22));
        requests.add(maker.createMergeRequest("MERGE_ALL", 12, 21,15,22));
        requests.add(maker.createMergeRequest("MERGE_ALL", 16, 21,17,22));

        requests.add(maker.createBorder(1,2,17,52, true, true, true, true));
        System.out.println("made all");
        BatchUpdateSpreadsheetRequest request = new BatchUpdateSpreadsheetRequest().setRequests(requests);
        try {
            service.spreadsheets().batchUpdate(Connections.SheetId, request).execute();
            System.out.println("done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Spreadsheet createNewSheet(String title){
        Spreadsheet spreadsheet = new Spreadsheet().setProperties(new SpreadsheetProperties().setTitle(title));
        try {
            spreadsheet = service.spreadsheets().create(spreadsheet).setFields("spreadsheetId").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return spreadsheet;
    }
}
