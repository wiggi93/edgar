package application;

import models.EdgarEntry;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExcelHandler {

    private static String[] columns = {"Company Name", "CIK", "SIC"};
    private static String path = "";

    public ExcelHandler(String path){
        this.path = path;
    }

    public void createExcel(HashMap<String, EdgarEntry> lol) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Edgar");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        int rowNum = 1;

        for (Map.Entry<String, EdgarEntry> entry : lol.entrySet()) {

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(entry.getValue().getCompanyName());
            row.createCell(1).setCellValue(entry.getValue().getCik());
            row.createCell(2).setCellValue(entry.getValue().getSic());

        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        FileOutputStream fileOut = new FileOutputStream(this.path + "generated-edgar-sheet.xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();


    }
}