package me.rudraksha007.billgenerator.POI;

import me.rudraksha007.billgenerator.utilities.DataManager;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

public class SpreadsheetTools {
    File head = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()+File.separator+"" +
            "BillHeader.xlsx");
    File foot = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()+File.separator+"" +
            "BillFooter.xlsx");

    public void saveXLSX(Vector<Vector> data){
        System.out.println(head.getAbsolutePath());
        File toSave = new File(DataManager.getDataFolder()+("Exports/exported.xlsx").replace("/", File.separator));
        try {
            System.out.println(toSave.getAbsolutePath());
            if (!toSave.exists()) {
                if (!toSave.getParentFile().exists())toSave.getParentFile().mkdir();
                toSave.createNewFile();
            }
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(String.valueOf(workbook.getNumberOfSheets()+1));
//            pasteHeader(sheet);
//            pasteData(sheet, data);
            pasteFooter(sheet, Math.max(15, data.size()));
            FileOutputStream stream = new FileOutputStream(toSave);
            workbook.write(stream);
            workbook.close();
            stream.flush();
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void pasteHeader(Sheet dest){
        try {
            Workbook source = new XSSFWorkbook(head);
            Sheet sourceSheet = source.getSheetAt(0);
            for (Row row: sourceSheet){
                Row destRow = dest.createRow(row.getRowNum());
                int i = 0;
                for (Cell cell: row){
                    Cell destCell = destRow.createCell(cell.getColumnIndex());
                    switch (cell.getCellType()){
                        case NUMERIC:destCell.setCellValue(cell.getNumericCellValue());break;
                        case FORMULA: destCell.setCellFormula(cell.getCellFormula());break;
                        default: destCell.setCellValue(cell.getRichStringCellValue());
                    }
                    XSSFCellStyle style = (XSSFCellStyle) row.getCell(i).getCellStyle();
                    if (style==null)continue;
                    XSSFCellStyle destStyle = (XSSFCellStyle) dest.getWorkbook().createCellStyle();
                    destStyle.cloneStyleFrom(style);
                    destCell.setCellStyle(destStyle);
                    i++;
                }
            }
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public void pasteFooter(Sheet dest, int lastWrittenRow){
        try {
            Workbook source = new XSSFWorkbook(foot);
            Sheet sourceSheet = source.getSheetAt(0);
            for (Row row: sourceSheet){
                Row destRow = dest.createRow(row.getRowNum()+lastWrittenRow+1);
                int i = 0;
                for (Cell cell: row){
                    Cell destCell = destRow.createCell(cell.getColumnIndex());
                    switch (cell.getCellType()){
                        case NUMERIC:destCell.setCellValue(cell.getNumericCellValue());break;
                        case FORMULA: destCell.setCellFormula(cell.getCellFormula());break;
                        default: destCell.setCellValue(cell.getRichStringCellValue());
                    }
                    XSSFCellStyle style = (XSSFCellStyle) row.getCell(i).getCellStyle();
                    if (style==null)continue;
                    XSSFCellStyle destStyle = (XSSFCellStyle) dest.getWorkbook().createCellStyle();
                    destStyle.cloneStyleFrom(style);
                    destCell.setCellStyle(destStyle);
                    i++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public void pasteData(Sheet sheet, Vector<Vector> data){
        sheet.getRow(0).getCell(0).setCellValue(1);
        sheet.getRow(0).getCell(1).setCellValue(String.valueOf(data.get(0).get(1)));
        sheet.getRow(0).getCell(7).setCellValue(String.valueOf(data.get(0).get(2)));
        sheet.getRow(0).getCell(10).setCellValue(Double.parseDouble(String.valueOf(data.get(0).get(3))));
        sheet.getRow(0).getCell(11).setCellValue(Double.parseDouble(String.valueOf(data.get(0).get(4)).split("/")[0]));
        for (int row = 1; row < data.size(); row++) {
            sheet.getRow(row).setRowStyle(sheet.getRow(0).getRowStyle());
            Row r = sheet.createRow(row+22);
            r.createCell(0).setCellValue(1+row);
            r.createCell(1).setCellValue(String.valueOf(data.get(row).get(1)));
            r.createCell(7).setCellValue(String.valueOf(data.get(row).get(2)));
            r.createCell(10).setCellValue(Integer.parseInt(String.valueOf(data.get(row).get(3))));
            r.createCell(11).setCellValue(Integer.parseInt(String.valueOf(data.get(row).get(4)).split("/")[0]));
            sheet.addMergedRegion(new CellRangeAddress(row+22, row+22, 1,6));
            sheet.addMergedRegion(new CellRangeAddress(row+22, row+22, 7,9));
            sheet.addMergedRegion(new CellRangeAddress(row+22, row+22, 11,14));
        }
    }
}
