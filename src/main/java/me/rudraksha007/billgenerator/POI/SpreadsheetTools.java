package me.rudraksha007.billgenerator.POI;

import me.rudraksha007.billgenerator.Main;
import me.rudraksha007.billgenerator.utilities.DataManager;
import me.rudraksha007.billgenerator.utilities.Utils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

public class SpreadsheetTools {
    File template = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()+File.separator+
            "BillTemplate.xlsx");

    XSSFWorkbook workbook;
//    @SuppressWarnings("all")
    public boolean saveXLSX(String invoice, String invoiceDate, String transport, String vehicleNo, String party,
                         String partyAddress,String partyGstIn, String partyState, int total, Vector<Vector> data){
        try {
            File toSave = new File(DataManager.getDataFolder()+("Exports/"+invoice+".xlsx").replace("/", File.separator));
            System.out.println(toSave.getAbsolutePath());
            ZipSecureFile.setMinInflateRatio(0);
            if (toSave.exists())return false;
            if (!toSave.getParentFile().exists()) toSave.getParentFile().mkdir();
            toSave.createNewFile();
            workbook = new XSSFWorkbook();
            Sheet sheet;
            int i = 0;
            do{
                System.out.println("print");
                if (data.size()>15)sheet = workbook.createSheet((workbook.getNumberOfSheets() + 1 )+"-"+i);
                else sheet = workbook.createSheet(String.valueOf((workbook.getNumberOfSheets() + 1 )));
                sheet.setDefaultColumnWidth(5);
                pasteTemplate(sheet);
                for (int j = 0; j < 3; j++) {
                    int rowOffset = j*53;
                    pasteData(sheet, data, i, rowOffset);
                    sheet.getRow(9+rowOffset).getCell(2).setCellValue(invoice);
                    sheet.getRow(9+rowOffset).getCell(12).setCellValue(transport);
                    sheet.getRow(10+rowOffset).getCell(0).setCellValue(invoiceDate);
                    sheet.getRow(10+rowOffset).getCell(12).setCellValue(vehicleNo);
                    sheet.getRow(13+rowOffset).getCell(2).setCellValue(party);
                    sheet.getRow(14+rowOffset).getCell(2).setCellValue(partyAddress);
                    sheet.getRow(16+rowOffset).getCell(2).setCellValue(partyGstIn);
                    sheet.getRow(17+rowOffset).getCell(2).setCellValue(partyState.split("\\.")[1]);
                    sheet.getRow(17+rowOffset).getCell(8).setCellValue(partyState.split("\\.")[0]);
                    sheet.getRow(38+rowOffset).getCell(0).setCellValue(new Utils().getNumInWords(total));
                }
                i++;
            } while ((data.size()-15*i)>15);
            sheet.getRow(38).getCell(15).setCellFormula("IF(I18="+Main.data.getState().split("\\.")[0]+",P38*" +
                    Main.data.getCgst()+"%,0)");
            sheet.getRow(39).getCell(15).setCellFormula("IF(I18="+Main.data.getState().split("\\.")[0]+",P38*" +
                    Main.data.getCgst()+"%,0)");
            sheet.getRow(40).getCell(15).setCellFormula("IF(I18="+Main.data.getState().split("\\.")[0]+",0,P38*" +
                    Main.data.getIgst()+"%)");
            FileOutputStream stream = new FileOutputStream(toSave);
            workbook.write(stream);
            workbook.close();
            stream.flush();
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;

    }

    public void pasteTemplate(Sheet dest){
        try(Workbook templateBook = new XSSFWorkbook(template)) {
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
                        case STRING:destCell.setCellValue(cell.getRichStringCellValue());break;
                        default:
                            if (cell.getColumnIndex()==10||cell.getColumnIndex()==11)break;
                            destCell.setCellValue(cell.getRichStringCellValue());break;
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

    public void pasteData(Sheet sheet, Vector<Vector> data, int sheetNo, int rowOffset){
        for (int i = 0; i < Math.min(15, data.size()-15*sheetNo); i++) {
            Row row = sheet.getRow(i+22+rowOffset);
            row.getCell(1).setCellValue(String.valueOf(data.get(i+15*sheetNo).get(1)));
            row.getCell(7).setCellValue(String.valueOf(data.get(i+15*sheetNo).get(2)));
            row.getCell(10).setCellValue(Integer.parseInt(String.valueOf(data.get(i+15*sheetNo).get(3))));
            row.getCell(11).setCellValue(Float.parseFloat(String.valueOf(data.get(i+15*sheetNo).get(4)).split("/")[0]));
        }

        sheet.getRow(2+rowOffset).getCell(0).setCellValue(Main.data.getCompanyName());
        sheet.getRow(3+rowOffset).getCell(0).setCellValue(Main.data.getAddress());
        sheet.getRow(4+rowOffset).getCell(0).setCellValue("Tel: "+Main.data.getPhn());
        sheet.getRow(5+rowOffset).getCell(0).setCellValue("GSTIN: "+Main.data.getGstin());
        sheet.getRow(43+rowOffset).getCell(7).setCellValue("Bank Name: "+Main.data.getBankName());
        sheet.getRow(44+rowOffset).getCell(7).setCellValue("A/c No. "+Main.data.getAccNo());
        sheet.getRow(45+rowOffset).getCell(7).setCellValue("Branch: "+Main.data.getBranch());
        sheet.getRow(46+rowOffset).getCell(7).setCellValue("IFS Code: "+ Main.data.getIfsc());
        sheet.getRow(47+rowOffset).getCell(7).setCellValue("For "+Main.data.getCompanyName());
        sheet.getRow(38+rowOffset).getCell(10).setCellValue("Add: CGST "+Main.data.getCgst()+"%");
        sheet.getRow(39+rowOffset).getCell(10).setCellValue("Add: SGST "+Main.data.getSgst()+"%");
        sheet.getRow(40+rowOffset).getCell(10).setCellValue("Add: IGST "+Main.data.getIgst()+"%");
    }
}
