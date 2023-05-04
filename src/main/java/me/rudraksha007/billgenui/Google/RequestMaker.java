package me.rudraksha007.billgenui.Google;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.sheets.v4.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequestMaker {
    
    public Request createMergeRequest(String MergeType, int ColumnStart, int RowStart, int ColumnEnd, int RowEnd){
        return new Request().setMergeCells(new MergeCellsRequest().setMergeType(MergeType)
                .setRange(new GridRange().setStartColumnIndex(ColumnStart-1).setEndColumnIndex(ColumnEnd)
                        .setStartRowIndex(RowStart-1).setEndRowIndex(RowEnd)));
    }

    public Request createBorder(int ColumnStart, int RowStart, int ColumnEnd, int RowEnd,
                                boolean top, boolean left, boolean right, boolean bottom){
        Request r = new Request().setUpdateBorders(new UpdateBordersRequest()
                .setRange(new GridRange().setStartColumnIndex(ColumnStart-1).setStartRowIndex(RowStart-1)
                        .setEndColumnIndex(ColumnEnd).setEndRowIndex(RowEnd)));
        if (top)r.getUpdateBorders().setTop(new Border().setStyle("SOLID_MEDIUM"));
        if (left)r.getUpdateBorders().setLeft(new Border().setStyle("SOLID_MEDIUM"));
        if (right)r.getUpdateBorders().setRight(new Border().setStyle("SOLID_MEDIUM"));
        if (bottom)r.getUpdateBorders().setBottom(new Border().setStyle("SOLID_MEDIUM"));
        return r;
    }

    public BatchGetValuesResponse read(String spreadsheetId, List<String> ranges) {
        BatchGetValuesResponse result = null;
        try {
            result = Connections.service.spreadsheets().values().batchGet(spreadsheetId).setRanges(ranges).execute();
            System.out.printf("%d ranges retrieved.", result.getValueRanges().size());
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 404) {
                System.out.printf("Spreadsheet not found with id '%s'.\n", spreadsheetId);
            } else {
                System.out.println("error while getting values");
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while getting values");
        }
        return result;
    }

    public BatchUpdateValuesResponse write(String spreadsheetId, String range, String valueInputOption,
                                           List<List<Object>> values) {
        List<ValueRange> data = new ArrayList<>();
        data.add(new ValueRange().setRange(range).setValues(values));

        BatchUpdateValuesResponse result = null;
        try {
            BatchUpdateValuesRequest body = new BatchUpdateValuesRequest()
                    .setValueInputOption(valueInputOption)
                    .setData(data);
            result = Connections.service.spreadsheets().values().batchUpdate(spreadsheetId, body).execute();
            System.out.printf("%d cells updated.", result.getTotalUpdatedCells());
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 404) {
                System.out.printf("Spreadsheet not found with id '%s'.\n", spreadsheetId);
            } else {
                System.out.println("error while writing values");
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public AppendValuesResponse append(String spreadsheetId, String range, String valueInputOption,
                                       List<List<Object>> values) {
        AppendValuesResponse result = null;
        try {
            ValueRange body = new ValueRange()
                    .setValues(values);
            result = Connections.service.spreadsheets().values().append(spreadsheetId, range, body)
                    .setValueInputOption(valueInputOption)
                    .execute();
            System.out.printf("%d cells appended.", result.getUpdates().getUpdatedCells());
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 404) {
                System.out.printf("Spreadsheet not found with id '%s'.\n", spreadsheetId);
            } else {
                System.out.println("error while updating values");
                e.printStackTrace();
            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("error while updating values");
        }
        return result;
    }
}
