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
    File template = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()+File.separator+
            "BillTemplate.xlsx");

//    @SuppressWarnings("all")
    public void saveXLSX(Vector<Vector> data){
        File toSave = new File(DataManager.getDataFolder()+("Exports/exported.xlsx").replace("/", File.separator));
        try {
            System.out.println(toSave.getAbsolutePath());
            Workbook workbook;
            if (!toSave.exists()) {
                if (!toSave.getParentFile().exists())toSave.getParentFile().mkdir();
                toSave.createNewFile();
                workbook = new XSSFWorkbook();
            }
            else workbook = new XSSFWorkbook(toSave);
            Sheet sheet;
            int i = 0;
            while ((data.size()-15*i)>15){
                if (data.size()>15)sheet = workbook.createSheet((workbook.getNumberOfSheets() + 1 )+"-"+i);
                else sheet = workbook.createSheet(String.valueOf((workbook.getNumberOfSheets() + 1 )));
                pasteTemplate(sheet);
                pasteData(sheet, data, i);
                i++;
            }
            FileOutputStream stream = new FileOutputStream(toSave);
            workbook.write(stream);
            workbook.close();
            stream.flush();
            stream.close();
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }

    }

    public void pasteTemplate(Sheet dest){
        try {
            Workbook templateBook = new XSSFWorkbook(template);
            Sheet temp = templateBook.getSheetAt(0);
            for (CellRangeAddress add: temp.getMergedRegions())dest.addMergedRegion(add);
            for (Row row: temp){
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

    public void pasteData(Sheet sheet, Vector<Vector> data, int sheetNo){
        for (int i = 22; i < 36; i++) {
            Row row = sheet.getRow(i);
            row.getCell(1).setCellValue(String.valueOf(data.get(i).get(1+15*sheetNo)));
            row.getCell(2).setCellValue(String.valueOf(data.get(i).get(2+15*sheetNo)));
            row.getCell(3).setCellValue(Integer.parseInt(String.valueOf(data.get(i).get(3+15*sheetNo))));
            row.getCell(4).setCellValue(Float.parseFloat(String.valueOf(data.get(i).get(4+15*sheetNo)).split("/")[0]));
        }
    }
}
